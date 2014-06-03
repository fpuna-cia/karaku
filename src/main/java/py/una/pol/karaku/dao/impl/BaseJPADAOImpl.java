package py.una.med.base.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.med.base.dao.BaseDAO;
import py.una.med.base.dao.helper.SearchParamJPAHelper;
import py.una.med.base.dao.search.SearchParam;

public abstract class BaseJPADAOImpl<T, K extends Serializable> implements
		BaseDAO<T, K> {

	private final Logger log = LoggerFactory.getLogger(BaseJPADAOImpl.class);

	@PersistenceContext(name = "primary")
	private EntityManager entityManager;

	private final SearchParamJPAHelper<T> helper = new SearchParamJPAHelper<T>();

	private Class<T> clazzT;

	public EntityManager getEntityManager() {

		return this.entityManager;
	}

	@Override
	public T getById(K id) {

		return this.getEntityManager().find(this.getClassOfT(), id);
	}

	@Override
	public T getByExample(T example) {

		return null;
	}

	public List<T> getAll(SearchParam params) {

		CriteriaBuilder criteriaBuilder = this.getEntityManager()
				.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder
				.createQuery(this.getClassOfT());
		Root<T> root = criteriaQuery.from(this.getClassOfT());
		CriteriaQuery<T> select = criteriaQuery.select(root);
		this.helper.apply(params, criteriaQuery, root, criteriaBuilder);
		TypedQuery<T> typedQuery = this.getEntityManager().createQuery(select);
		this.helper.apply(params, typedQuery);
		return typedQuery.getResultList();
	}

	public List<T> getAllByExample(T example, SearchParam params) {

		return Collections.emptyList();
	}

	@Override
	public T update(T entity) {

		this.getEntityManager().merge(entity);
		return entity;
	}

	@Override
	public T add(T entity) {

		try {
			this.entityManager.persist(entity);
			this.entityManager.flush();
		} catch (Exception e) {
			this.log.error("Error al crear la entidad", e);
		}
		return entity;
	}

	@Override
	public void remove(T entity) {

		this.getEntityManager().remove(this.update(entity));

	}

	@Override
	public void remove(K id) {

		this.getEntityManager().refresh(this.getById(id));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getClassOfT() {

		if (this.clazzT == null) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			this.clazzT = (Class<T>) type.getActualTypeArguments()[0];
		}
		return this.clazzT;
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

}
