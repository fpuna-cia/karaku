/*
 * @BaseDaoImpl
 * 
 * Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.BaseDAO;
import py.una.med.base.dao.helper.RestrictionHelper;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.search.OrderParam;
import py.una.med.base.dao.search.SearchParam;
import py.una.med.base.dao.util.CaseSensitiveHelper;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.util.MainInstanceHelper;

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
public abstract class BaseDAOImpl<T, ID extends Serializable> implements
		BaseDAO<T, ID> {

	private SessionFactory sessionFactory;

	@Autowired
	private EntityManager em;

	@Autowired
	private CaseSensitiveHelper sensitiveHelper;

	private Logger logger = LoggerFactory.getLogger(BaseDAOImpl.class);

	private Class<T> clazz;

	private RestrictionHelper<T> helper;

	@Override
	public EntityManager getEntityManager() {

		return em;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T add(T entity) {

		sensitiveHelper.analize(entity);
		entity = (T) getSession().merge(entity);
		getSession().flush();
		return entity;
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
			logger.error("Error al agregar la relación", e);
		}
		return criteria;
	}

	@Override
	public List<T> getAll(final ISearchParam params) {

		return getAll(null, params);
	}

	@Override
	public List<T> getAll(final Where<T> where, final ISearchParam params) {

		Map<String, String> alias = new HashMap<String, String>();
		Criteria criteria = generateWhere(where, params, alias);

		MainInstanceHelper helper2 = new MainInstanceHelper();

		try {
			return helper2.configureAndReturnList(getSession(), criteria,
					getClassOfT(), alias);
		} catch (Exception e) {
			logger.error("Imposible obtener lista de elementos", e);
			return new ArrayList<T>();
		}
	}

	protected Criteria generateWhere(final Where<T> where,
			final ISearchParam params, final Map<String, String> alias) {

		Criteria criteria = getCriteria();
		if (where != null) {
			EntityExample<T> example = where.getExample();
			if ((example != null) && (example.getEntity() != null)) {
				Example ejemplo = Example.create(example.getEntity());
				ejemplo.enableLike(MatchMode.ANYWHERE);
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
				configureExample(criteria, example.getEntity());
			}
		}
		configureParams(params, criteria);

		getRestrictionHelper().applyRestrictions(criteria, where, alias);
		return criteria;
	}

	protected void configureParams(final ISearchParam params,
			final Criteria criteria) {

		if (params != null) {
			if (params.getOrders() != null) {
				for (OrderParam order : params.getOrders()) {
					if (order.isAsc()) {
						criteria.addOrder(Order.asc(order.getColumnName()));
					} else {
						criteria.addOrder(Order.desc(order.getColumnName()));
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

	private RestrictionHelper<T> getRestrictionHelper() {

		if (helper == null) {
			helper = new RestrictionHelper<T>();
		}
		return helper;
	}

	@Override
	public List<T> getAllByExample(final EntityExample<T> example,
			final ISearchParam params) {

		Where<T> where = new Where<T>();
		where.setExample(example);
		return getAll(where, params);
	}

	@Override
	public List<T> getAllByExample(final T example, final ISearchParam params) {

		EntityExample<T> entityExample = new EntityExample<T>(example);
		return getAllByExample(entityExample, params);
	}

	@Override
	public T getByExample(final T example) {

		Where<T> where = new Where<T>();
		where.setExample(new EntityExample<T>(example));
		ISearchParam isp = new SearchParam();
		isp.setLimit(2);
		isp.setOffset(0);

		List<T> result = getAll(where, isp);
		if ((result == null) || (result.size() == 0)) {
			return null;
		}
		if (result.size() == 1) {
			return result.get(0);
		}

		throw new NonUniqueResultException(result.size());
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getById(final ID id) {

		return (T) getSession().get(getClassOfT(), id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getClassOfT() {

		if (clazz == null) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			clazz = (Class<T>) type.getActualTypeArguments()[0];
		}
		return clazz;
	}

	@Override
	public Long getCount(final Where<T> where) {

		HashMap<String, String> alias = new HashMap<String, String>();
		Criteria criteria = generateWhere(where, null, alias);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	@Override
	public Long getCount() {

		return getCount(null);
	}

	@Override
	public Long getCountByExample(final EntityExample<T> example) {

		Where<T> where = new Where<T>();
		where.setExample(example);
		return getCount(where);
	}

	private Criteria getCriteria() {

		Criteria criteria = getSession().createCriteria(getClassOfT(),
				Criteria.ROOT_ALIAS);
		return criteria;
	}

	/**
	 * Retorna una instancia del {@link org.springframework.stereotype.Service}
	 * que se encarga de realizar controles de formato en cadenas
	 * 
	 * @return {@link CaseSensitiveHelper} atado al contexto actual
	 */
	public CaseSensitiveHelper getSensitiveHelper() {

		return sensitiveHelper;
	}

	/**
	 * @param sensitiveHelper
	 *            sensitiveHelper para setear
	 */
	public void setSensitiveHelper(final CaseSensitiveHelper sensitiveHelper) {

		this.sensitiveHelper = sensitiveHelper;
	}

	/**
	 * Obtiene una session del contexto actual, si no hay una sesion abierta,
	 * lanza una excepcion {@link HibernateException} con el mensaje
	 * "Not session found in the current thread"
	 * 
	 * @return {@link Session} del contexto actual
	 */
	public Session getSession() {

		return sessionFactory.getCurrentSession();
	}

	/**
	 * Retorna el {@link Component} creador de Sessiones
	 * 
	 * @return SessionFactory del thread actual
	 */
	protected SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	@Override
	public String getTableName() {

		Class<T> clase = getClassOfT();
		Table t = clase.getAnnotation(Table.class);
		if (t == null) {
			return clase.getSimpleName().toLowerCase();
		} else {
			return t.name();
		}
	};

	@Override
	public void remove(final ID id) {

		// TODO lograr que no haga una consulta antes de eliminar
		Object o = getSession().load(getClassOfT(), id);
		getSession().delete(o);
	}

	@Override
	public void remove(final T entity) {

		// TODO lograr que no haga una consulta antes de eliminar
		Object o = getSession().load(getClassOfT(), getIdValue(entity));
		getSession().delete(o);
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

	@SuppressWarnings("unchecked")
	@Override
	public T update(T entity) {

		sensitiveHelper.analize(entity);
		sensitiveHelper.analize(entity);
		entity = ((T) getSession().merge(entity));
		getSession().flush();
		return entity;
	}

	@SuppressWarnings("unchecked")
	private ID getIdValue(final T obj) {

		try {
			for (Field field : obj.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Id.class)) {
					field.setAccessible(true);
					ID data = (ID) field.get(obj);
					field.setAccessible(false);
					return data;
				}
			}
			return null;
		} catch (Exception ex) {
			logger.error("Error al obtener el Id", ex);
			return null;
		}
	};

}
