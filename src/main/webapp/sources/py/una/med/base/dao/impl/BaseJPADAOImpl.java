package py.una.med.base.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import py.una.med.base.dao.BaseDAO;
import py.una.med.base.dao.helper.SearchParamJPAHelper;
import py.una.med.base.dao.search.SearchParam;

public abstract class BaseJPADAOImpl<T, ID extends Serializable> implements
		BaseDAO<T, ID> {

	@PersistenceContext(name = "primary")
	EntityManager entityManager;

	@PersistenceContext(name = "primary")
	EntityManager persisteContext;

	Class<T> clazz;

	SearchParamJPAHelper<T> helper = new SearchParamJPAHelper<T>();

	private Class<T> clazzT;

	public EntityManager getEntityManager() {

		return entityManager;
	}

	public T getById(ID id) {

		return getEntityManager().find(getClassOfT(), id);
	}

	public T getByExample(T example) {

		return null;
	}

	public List<T> getAll(SearchParam params) {

		CriteriaBuilder criteriaBuilder = getEntityManager()
				.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder
				.createQuery(getClassOfT());
		Root<T> root = criteriaQuery.from(getClassOfT());
		CriteriaQuery<T> select = criteriaQuery.select(root);
		helper.apply(params, criteriaQuery, root, criteriaBuilder);
		TypedQuery<T> typedQuery = getEntityManager().createQuery(select);
		helper.apply(params, typedQuery);
		return typedQuery.getResultList();
	}

	public List<T> getAllByExample(T example, SearchParam params) {

		return null;
	}

	public T update(T entity) {

		getEntityManager().merge(entity);
		return entity;
	}

	public T add(T entity) {

		try {
			entityManager.persist(entity);
			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}

	public void remove(T entity) {

		getEntityManager().remove(update(entity));

	}

	public void remove(ID id) {

		getEntityManager().refresh(getById(id));
	}

	@SuppressWarnings("unchecked")
	public Class<T> getClassOfT() {

		if (clazzT == null) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			clazzT = (Class<T>) type.getActualTypeArguments()[0];
		}
		return clazzT;
	}

	public String getTableName() {

		Class<T> clase = getClassOfT();
		Table t = clase.getAnnotation(Table.class);
		if (t == null) {
			return clase.getSimpleName().toLowerCase();
		} else {
			return t.name();
		}
	}

}
