package py.una.med.base.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.replication.Shareable;

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
public abstract class SIGHBaseLogic<T, K extends Serializable> implements
		ISIGHBaseLogic<T, K> {

	private Logger log = LoggerFactory.getLogger(SIGHBaseLogic.class);

	@Override
	@Transactional(readOnly = true)
	public T getByExample(T example) {

		return getDao().getByExample(example);
	}

	@Override
	@Transactional(readOnly = true)
	public T getById(K id) {

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
	public K getIdValue(T obj) {

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
		} catch (Exception ex) {
			log.error("Error al obtener el Id", ex);
			return null;
		}
	};

	@Override
	public T getNewInstance() {

		try {
			T instance = getDao().getClassOfT().newInstance();
			if (instance instanceof Shareable) {
				((Shareable) instance).activate();
			}
			return instance;
		} catch (Exception e) {
			log.error("Error al obtener una instancia", e);
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
	 * py.una.med.base.business.ISIGHBaseLogic#getCount(py.una.med.base.dao.
	 * restrictions.Where)
	 */
	@Override
	public Long getCount(Where<T> where) {

		return getDao().getCount(where);
	}
}
