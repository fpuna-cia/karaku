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
package py.una.pol.karaku.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.select.Select;
import py.una.pol.karaku.dao.util.EntityExample;
import py.una.pol.karaku.replication.Shareable;

/**
 * Clase que implementa {@link IKarakuBaseLogic} y que debe ser el punto base de
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
public abstract class KarakuBaseLogic<T, K extends Serializable> implements
		IKarakuBaseLogic<T, K> {

	private Logger log = LoggerFactory.getLogger(KarakuBaseLogic.class);

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
	public List<T> get(Select select, Where<T> where, ISearchParam params) {

		return getDao().get(select, where, params);
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
	}

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

	@Override
	public Long getCount(Where<T> where) {

		return getDao().getCount(where);
	}
}
