/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.dao.impl;

import static py.una.pol.karaku.util.Checker.notNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.NonUniqueResultException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.dao.BaseDAO;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.interceptors.InterceptorHandler;
import py.una.pol.karaku.dao.entity.watchers.WatcherHandler;
import py.una.pol.karaku.dao.helper.BaseClauseHelper;
import py.una.pol.karaku.dao.helper.RestrictionHelper;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.search.OrderParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.dao.select.KarakuAliasToBeanTransformer;
import py.una.pol.karaku.dao.select.Select;
import py.una.pol.karaku.dao.util.EntityExample;
import py.una.pol.karaku.dao.util.MainInstanceHelper;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.util.KarakuReflectionUtils;
import py.una.pol.karaku.util.ListHelper;

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
	@Nonnull
	private Criteria configureExample(@Nonnull final Criteria criteria,
			final T example) {

		if (example == null) {
			return criteria;
		}
		try {
			for (final Field f : example.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.getAnnotation(OneToOne.class) == null
						&& f.getAnnotation(ManyToOne.class) == null
						|| f.get(example) == null) {
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
	@Nonnull
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
			@Nonnull final Map<String, String> alias, Criteria criteria) {

		if (where != null) {
			EntityExample<T> example = where.getExample();
			if (example != null && example.getEntity() != null) {
				Example ejemplo = Example.create(example.getEntity());
				ejemplo.enableLike(example.getMatchMode().getMatchMode());
				ejemplo.setEscapeCharacter('\\');
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
			for (String s : where.getFetchs()) {
				criteria.setFetchMode(s, FetchMode.JOIN);
			}
			helper.applyClauses(criteria, where, alias);
		}

		return criteria;
	}

	protected Criteria generateWhere(final Where<T> where,
			@Nonnull final Map<String, String> alias) {

		return generateWhere(where, alias, getCriteria());
	}

	protected void configureParams(final ISearchParam params,
			final Criteria criteria, @Nonnull Map<String, String> alias) {

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
		if (!ListHelper.hasElements(result)) {
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
			log.trace("Can't find entity with id {}", id, onfe);
			return null;
		}
	}

	@Override
	@Nonnull
	public Class<T> getClassOfT() {

		if (clazz == null) {
			clazz = KarakuReflectionUtils.getParameterizedClass(this, 0);
		}
		return notNull(clazz);
	}

	@Override
	public Long getCount(final Where<T> where) {

		Map<String, String> alias = new HashMap<String, String>();
		Criteria criteria = this.generateWhere(where, alias);
		if (where != null && where.isDistinct()) {
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

	@Nonnull
	private Criteria getCriteria() {

		Criteria criteria = this.getSession()
				.createCriteria(this.getClassOfT());
		return notNull(criteria, "can't create criteria");
	}

	/**
	 * Obtiene una session del contexto actual, si no hay una sesión abierta,
	 * lanza una excepción con el mensaje "Not session found in the current
	 * thread".
	 * 
	 * @return {@link Session} del contexto actual
	 */
	@Nonnull
	public Session getSession() {

		return notNull(this.getSessionFactory().getCurrentSession(),
				"Cant get session");
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
		}
		return t.name();
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

		return (K) getSessionFactory().getClassMetadata(getClassOfT())
				.getIdentifier(obj, (SessionImplementor) getSession());
	}

	private void setId(final T dst, final K key) {

		getSessionFactory().getClassMetadata(getClassOfT()).setIdentifier(dst,
				key, (SessionImplementor) getSession());
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
	@Nonnull
	public List<T> get(Select select, Where<T> where, ISearchParam params) {

		if (!BaseEntity.class.isAssignableFrom(getClassOfT())) {
			throw new UnsupportedOperationException(
					"Only works with baseEntity");
		}

		ISearchParam isp = params != null ? params : new SearchParam();
		isp.addOrder("id");
		Criteria criteria = getCriteria();
		Map<String, String> alias = new HashMap<String, String>();

		if (where != null && !where.isDistinct()) {
			log.debug("Making a not distinct query with Select, it's allways distinct");
		}

		if (select != null) {
			ProjectionList projections = Projections.projectionList();
			select.addAttribute("id");
			for (String projection : select.getAttributes()) {
				if (projection == null) {
					continue;
				}
				String property = BaseClauseHelper.configureAlias(criteria,
						projection, alias);

				projections.add(Projections.property(property), projection);

			}
			criteria.setProjection(projections);
			criteria.setResultTransformer(new KarakuAliasToBeanTransformer<T>(
					getClassOfT()));
		}

		generateWhere(where, alias, criteria);
		configureParams(isp, criteria, alias);

		List<T> toRet = criteria.list();
		if (toRet == null) {
			return new ArrayList<T>();
		}
		return toRet;
	}
}
