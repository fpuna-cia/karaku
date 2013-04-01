package py.una.med.base.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.Id;
import org.hibernate.NonUniqueResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.util.EntityExample;

/**
 * Clase que implementa {@link ISIGHBaseLogic} y que debe ser el punto base de
 * todas las logicas.
 * 
 * @author Arturo Volpe
 * 
 * @param <T>
 *            Clase de la entidad
 * @param <ID>
 *            Clase del id de la entidad
 */
@Service
@Transactional
public abstract class SIGHBaseLogic<T, ID extends Serializable> implements
		ISIGHBaseLogic<T, ID> {

	@Override
	@Transactional(readOnly = true)
	public T getByExample(T example) throws NonUniqueResultException {

		return getDao().getByExample(example);
	}

	@Override
	@Transactional(readOnly = true)
	public T getById(ID id) {

		return getDao().getById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAll(ISearchParam params) {

		return getDao().getAll(params);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAll(Where<T> where, ISearchParam params) {

		return getDao().getAll(where, params);
	}

	@Override
	@Transactional(readOnly = false)
	public T update(T entity) {

		return getDao().update(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public T add(T entity) {

		return getDao().add(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void remove(T entity) {

		getDao().remove(entity);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ID getIdValue(T obj) {

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
			ex.printStackTrace();
			return null;
		}
	};

	@Override
	public T getNewInstance() {

		try {
			return getDao().getClassOfT().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAllByExample(EntityExample<T> example, ISearchParam params) {

		return getDao().getAllByExample(example, params);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> getAllByExample(T ejemplo, ISearchParam sp) {

		return getDao().getAllByExample(ejemplo, sp);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getCountByExample(EntityExample<T> example) {

		return getDao().getCountByExample(example);
	}

	@Override
	@Transactional(readOnly = true)
	public Long getCount() {

		return getDao().getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * py.una.med.base.business.ISIGHBaseLogic#getCount(py.una.med.util.dao.
	 * restrictions.Where)
	 */
	@Override
	public Long getCount(Where<T> where) {

		return getDao().getCount(where);
	}

}
