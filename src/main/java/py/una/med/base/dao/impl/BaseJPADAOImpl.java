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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.med.base.dao.BaseDAO;
import py.una.med.base.dao.helper.SearchParamJPAHelper;
import py.una.med.base.dao.search.SearchParam;

public abstract class BaseJPADAOImpl<T, K extends Serializable> implements
		BaseDAO<T, K> {

	private Logger log = LoggerFactory.getLogger(BaseJPADAOImpl.class);

	@PersistenceContext(name = "primary")
	private EntityManager entityManager;

	@PersistenceContext(name = "primary")
	private EntityManager persisteContext;

	private Class<T> clazz;

	private SearchParamJPAHelper<T> helper = new SearchParamJPAHelper<T>();

	private Class<T> clazzT;

	@Override
	public EntityManager getEntityManager() {

		return entityManager;
	}

	@Override
	public T getById(K id) {

		return getEntityManager().find(getClassOfT(), id);
	}

	@Override
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

	@Override
	public T update(T entity) {

		getEntityManager().merge(entity);
		return entity;
	}

	@Override
	public T add(T entity) {

		try {
			entityManager.persist(entity);
			entityManager.flush();
		} catch (Exception e) {
			log.error("Error al crear la entidad", e);
		}
		return entity;
	}

	@Override
	public void remove(T entity) {

		getEntityManager().remove(update(entity));

	}

	@Override
	public void remove(K id) {

		getEntityManager().refresh(getById(id));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getClassOfT() {

		if (clazzT == null) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			clazzT = (Class<T>) type.getActualTypeArguments()[0];
		}
		return clazzT;
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
	}

}
