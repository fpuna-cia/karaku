/*
 * @BaseDaoImpl
 * 
 * Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.impl;

import static py.una.med.base.util.Checker.notNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.BaseDAO;
import py.una.med.base.dao.entity.Operation;
import py.una.med.base.dao.entity.interceptors.InterceptorHandler;
import py.una.med.base.dao.entity.watchers.WatcherHandler;
import py.una.med.base.dao.helper.BaseClauseHelper;
import py.una.med.base.dao.helper.RestrictionHelper;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.search.OrderParam;
import py.una.med.base.dao.search.SearchParam;
import py.una.med.base.dao.select.KarakuAliasToBeanTransformer;
import py.una.med.base.dao.select.Select;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.util.MainInstanceHelper;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.log.Log;
import py.una.med.base.util.KarakuReflectionUtils;

/**
 * Clase que implementa la interfaz {@link BaseDAO} utilizando {@link Session},
 * Debería migrar paulatinamente a {@link EntityManager} para una mayor
 * portabilidad hacia otros motores que no sean hibernate, y para utilizar mejor
 * hibernate 4
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 14, 2013
 * @param <T>
 *            Clase de la entidad a ser utilizada
 * @param <ID>
 *            ID de la entidad
 * 
 * 
 */
public abstract class BaseDAOImpl<T, K extends Serializable> implements
		BaseDAO<T, K> {

	/**
	 * Cadena para que se marque un método que no es type-safe
	 */
	private static final String UNCHECKED = "unchecked";

	private SessionFactory sessionFactory;

	@Autowired
	private RestrictionHelper helper;

	@Autowired
	private MainInstanceHelper mainInstanceHelper;

	@Autowired
	private WatcherHandler watcherHandler;

	@Log
	private Logger log;

	@Autowired
	private transient InterceptorHandler interceptorHandler;

	private Class<T> clazz;

	public EntityManager getEntityManager() {

		return null;
	}

	@Override
	@Nonnull
	public T add(@Nonnull final T entity) {

		return notNull(doIt(Operation.CREATE, entity));
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public void remove(@NotNull final K id) {

		T o = (T) this.getSession().get(this.getClassOfT(), id);
		remove(notNull(o));
	}

	@Override
	@Nonnull
	public T update(@Nonnull final T entity) {

		return notNull(doIt(Operation.UPDATE, entity));
	}

	@Override
	public void remove(@Nonnull final T entity) {

		doIt(Operation.DELETE, entity);
	}

	private T doIt(@Nonnull Operation op, @Nonnull T entity) {

		Operation origin = op;
		Operation real = watcherHandler.redirect(origin, getClassOfT(), entity);
		watcherHandler.process(origin, real, getClassOfT(), entity);
		interceptorHandler.intercept(real, entity);

		switch (real) {
			case CREATE:
				return realAdd(entity);
			case UPDATE:
				return realUpdate(entity);
			default:
				realDelete(entity);
				return null;
		}

	}

	@SuppressWarnings(UNCHECKED)
	private T realAdd(T entity) {

		T entidad = (T) this.getSession().merge(entity);
		this.getSession().flush();
		this.setId(entity, getIdValue(entidad));
		return entidad;
	}

	@SuppressWarnings(UNCHECKED)
	private T realUpdate(T entity) {

		T entidad = entity;
		entidad = (T) this.getSession().merge(entidad);
		this.getSession().flush();
		return entidad;
	}

	private void realDelete(T entity) {

		// TODO lograr que no haga una consulta antes de eliminar
		Object o = this.getSession().load(this.getClassOfT(),
				this.getIdValue(entity));
		this.getSession().delete(o);
	}

	/**
	 * Metodo que agrega relaciones
	 * 
	 * @param criteria
	 * @param example
	 * @return
	 */
	private Criteria configureExample(final Criteria criteria, final T example) {

		if (example == null) {
			return criteria;
		}
		try {
			for (final Field f : example.getClass().getDeclaredFields()) {
				if ((f.getAnnotation(OneToOne.class) == null)
						&& (f.getAnnotation(ManyToOne.class) == null)) {
					continue;
				}
				f.setAccessible(true);
				if (f.get(example) == null) {
					continue;
				}
				criteria.add(Restrictions.eq(f.getName(), f.get(example)));
			}
		} catch (final Exception e) {
			this.log.error("Error al agregar la relación", e);
		}
		return criteria;
	}

	@Override
	public List<T> getAll(final ISearchParam params) {

		return this.getAll(null, params);
	}

	@Override
	public List<T> getAll(final Where<T> where, final ISearchParam params) {

		Map<String, String> alias = new HashMap<String, String>();
		Criteria criteria = this.generateWhere(where, alias);
		configureParams(params, criteria, alias);

		try {
			return mainInstanceHelper.configureAndReturnList(this.getSession(),
					criteria, this.getClassOfT(), alias, where);
		} catch (Exception e) {
			this.log.error("Imposible obtener lista de elementos", e);
			throw new KarakuRuntimeException(e);
		}
	}

	protected Criteria generateWhere(final Where<T> where,
			final Map<String, String> alias) {

		Criteria criteria = this.getCriteria();
		if (where != null) {
			EntityExample<T> example = where.getExample();
			if ((example != null) && (example.getEntity() != null)) {
				Example ejemplo = Example.create(example.getEntity());
				ejemplo.enableLike(example.getMatchMode().getMatchMode());
				if (example.isIgnoreCase()) {
					ejemplo.ignoreCase();
				}
				if (example.isExcludeZeroes()) {
					ejemplo.excludeZeroes();
				}
				criteria.add(ejemplo);
				if (example.getExcluded() != null) {
					for (String excluded : example.getExcluded()) {
						ejemplo.excludeProperty(excluded);
					}
				}
				this.configureExample(criteria, example.getEntity());
			}
		}
		this.helper.applyClauses(criteria, where, alias);
		return criteria;
	}

	protected void configureParams(final ISearchParam params,
			final Criteria criteria, Map<String, String> alias) {

		if (params != null) {
			if (params.getOrders() != null) {
				for (OrderParam order : params.getOrders()) {
					String property = BaseClauseHelper.configureAlias(criteria,
							order.getColumnName(), alias);
					if (order.isAsc()) {
						criteria.addOrder(Order.asc(property));
					} else {
						criteria.addOrder(Order.desc(property));
					}
				}
			}
			if (params.getLimit() != null) {
				criteria.setMaxResults(params.getLimit().intValue());
			}
			if (params.getOffset() != null) {
				criteria.setFirstResult(params.getOffset().intValue());
			}
		}
	}

	@Override
	public List<T> getAllByExample(final EntityExample<T> example,
			final ISearchParam params) {

		Where<T> where = new Where<T>();
		where.setExample(example);
		return this.getAll(where, params);
	}

	@Override
	public List<T> getAllByExample(final T example, final ISearchParam params) {

		EntityExample<T> entityExample = new EntityExample<T>(example);
		return this.getAllByExample(entityExample, params);
	}

	@Override
	public T getByExample(final T example) {

		return this.getByExample(new EntityExample<T>(example));

	}

	@Override
	public T getByExample(EntityExample<T> example) {

		Where<T> where = new Where<T>();
		where.setExample(example);
		ISearchParam isp = new SearchParam();
		isp.setLimit(2);
		isp.setOffset(0);

		List<T> result = this.getAll(where, isp);
		if ((result == null) || (result.isEmpty())) {
			return null;
		}
		if (result.size() == 1) {
			return result.get(0);
		}

		throw new NonUniqueResultException(result.size());

	}

	@Override
	@SuppressWarnings(UNCHECKED)
	public T getById(final K id) {

		try {
			return (T) this.getSession().get(getClassOfT(), id);
		} catch (ObjectNotFoundException onfe) {
			return null;
		}
	}

	@Override
	public Class<T> getClassOfT() {

		if (this.clazz == null) {
			clazz = KarakuReflectionUtils.getParameterizedClass(this, 0);
		}
		return this.clazz;
	}

	@Override
	public Long getCount(final Where<T> where) {

		HashMap<String, String> alias = new HashMap<String, String>();
		Criteria criteria = this.generateWhere(where, alias);
		if ((where != null) && where.isDistinct()) {
			criteria.setProjection(Projections.countDistinct("id"));
		} else {
			criteria.setProjection(Projections.rowCount());
		}
		Object result = criteria.uniqueResult();
		if (result == null) {
			throw new KarakuRuntimeException("The class "
					+ this.getClassOfT().getSimpleName() + " is not mapped");
		}
		return (Long) result;
	}

	@Override
	public Long getCount() {

		return this.getCount(null);
	}

	@Override
	public Long getCountByExample(final EntityExample<T> example) {

		Where<T> where = new Where<T>();
		where.setExample(example);
		return this.getCount(where);
	}

	private Criteria getCriteria() {

		Criteria criteria = this.getSession()
				.createCriteria(this.getClassOfT());
		return criteria;
	}

	/**
	 * Obtiene una session del contexto actual, si no hay una sesión abierta,
	 * lanza una excepción con el mensaje "Not session found in the current
	 * thread".
	 * 
	 * @return {@link Session} del contexto actual
	 */
	public Session getSession() {

		return this.getSessionFactory().getCurrentSession();
	}

	/**
	 * Retorna el Componente creador de Sessiones
	 * 
	 * @return SessionFactory del thread actual
	 */
	protected SessionFactory getSessionFactory() {

		return this.sessionFactory;
	}

	@Override
	public String getTableName() {

		Class<T> clase = this.getClassOfT();
		Table t = clase.getAnnotation(Table.class);
		if (t == null) {
			return clase.getSimpleName().toLowerCase();
		} else {
			return t.name();
		}
	}

	/**
	 * Asigna un sessionFactory para ser usado de ahora en mas para obtener
	 * sessiones y mantener transacciones
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(final SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings(UNCHECKED)
	private K getIdValue(final T obj) {

		Exception ex;
		try {
			if (this.getClassOfT().getMethod("getId") != null) {
				return ((K) obj.getClass().getMethod("getId")
						.invoke(obj, (Object[]) null));
			}
		} catch (Exception nsme) {
			ex = nsme;
		}
		try {
			for (Field field : obj.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Id.class)) {
					field.setAccessible(true);
					K data = (K) field.get(obj);
					field.setAccessible(false);
					return data;
				}
			}
			return null;
		} catch (Exception exe) {
			ex = exe;
		}
		this.log.error("Error al obtener el Id", ex);
		return null;
	}

	private void setId(final T dst, final K key) {

		Exception ex = null;
		try {
			Method set = this.getClassOfT().getMethod("setId", Long.class);
			if (set != null) {
				set.invoke(dst, key);
				return;
			}
		} catch (Exception nsme) {
			ex = nsme;
		}
		try {
			for (Field field : this.getClassOfT().getDeclaredFields()) {
				if (field.isAnnotationPresent(Id.class)) {
					field.setAccessible(true);
					field.set(dst, key);
					field.setAccessible(false);
					return;
				}
			}
		} catch (Exception exe) {
			ex = exe;
		}
		if (ex != null) {
			this.log.error("Error al asignar el Id (not found)", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> get(Select select) {

		return get(select, null, null);
	}

	@SuppressWarnings(UNCHECKED)
	@Override
	public List<T> get(Select select, Where<T> where, ISearchParam params) {

		Map<String, String> alias = new HashMap<String, String>();
		Criteria criteria = this.generateWhere(where, alias);
		configureParams(params, criteria, alias);

		boolean isDistinct = where != null ? where.isDistinct() : false;

		if (select != null) {
			ProjectionList projections = Projections.projectionList();
			for (String column : select.getAttributes()) {

				String property = BaseClauseHelper.configureAlias(criteria,
						column, alias);

				projections.add(Projections.property(property), column);

			}
			criteria.setProjection(projections);
			criteria.setResultTransformer(new KarakuAliasToBeanTransformer<T>(
					getClassOfT(), isDistinct));
		}

		return criteria.list();
	}
}
