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
package py.una.pol.karaku.replication;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.Validate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.log.Log;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 27, 2013
 * 
 */
@Component
public class UriCache {

	private UriCache cache;

	private Map<String, Object> map;

	@Log
	private Logger log;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private SessionFactory factory;

	@PostConstruct
	void addUriCache() {

		cache = context.getBean(UriCache.class);
	}

	/**
	 * Elimina todos los objetos de la cache.
	 */
	public void clearCache() {

		map = null;
	}

	/**
	 * Retorna una instancia basada en su URI.
	 * 
	 * <p>
	 * Este método (que es el método central de la clase) se encarga de buscar
	 * en la base de datos una entidad del tipo pasado que tenga la uri
	 * especificada.
	 * </p>
	 * <p>
	 * <b>Notar además, que la entidad retornada nunca tiene algún atributo
	 * adicional a su identificador</b>, el único uso que se le debe dar a la
	 * entidad recuperada es el de referencia a otra tabla.
	 * </p>
	 * 
	 * @param clazz
	 *            clase del objeto que se busca
	 * @param uri
	 *            identificador único del objeto
	 * @return entidad obtenida, nunca <code>null</code>
	 * @throws EntityNotFoundException
	 *             cuando la entidad no es encontrada.
	 */
	public <T extends BaseEntity & Shareable> T getByUri(Class<T> clazz,
			String uri) throws EntityNotFoundException {

		T toRet = getByUriOrNull(clazz, uri);
		if (toRet == null) {
			throw new EntityNotFoundException(uri, getEntityName(clazz));
		}
		return toRet;
	}

	/**
	 * Retorna una instancia basada en su URI.
	 * 
	 * <p>
	 * Se encarga de buscar en la base de datos una entidad del tipo pasado que
	 * tenga la uri especificada.
	 * </p>
	 * <p>
	 * <b>Notar además, que la entidad retornada nunca tiene algún atributo
	 * adicional a su identificador</b>, el único uso que se le debe dar a la
	 * entidad recuperada es el de referencia a otra tabla.
	 * </p>
	 * 
	 * @param clazz
	 *            clase del objeto que se busca
	 * @param uri
	 *            identificador único del objeto
	 * @return entidad obtenida, nunca <code>null</code>
	 * @throws EntityNotFoundException
	 *             cuando la entidad no es encontrada.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity & Shareable> T getByUriOrNull(Class<T> clazz,
			String uri) {

		notNull(clazz, "Can't get entity by null clazz");
		notNull(uri, "Can't get entity with null uri");
		if (!getMap().containsKey(uri)) {
			Object toAdd = cache.getEntityByUri(clazz, uri);
			if (toAdd == null) {
				return null;
			}
			addToCache(uri, toAdd);
		}
		Object toRet = map.get(uri);
		Validate.validState(toRet.getClass().equals(clazz),
				"Cache uri return incorrect types of entity, same uri?");
		return (T) toRet;
	}

	/**
	 * Agrega una entidad a la cache actual.
	 * 
	 * 
	 * @param uri
	 *            identificador de la entidad
	 * @param entity
	 *            entidad a agregar
	 * 
	 */
	protected void addToCache(String uri, Object entity) {

		getMap().put(uri, entity);
	}

	/**
	 * Carga todas las filas de una entidad para su posterior acceso eficiente.
	 * 
	 * <p>
	 * Esto es un simple <code>select * from entity</code>, tener especial
	 * cuidado con tablas muy grandes.
	 * </p>
	 * 
	 * @param entity
	 *            entidad a ser cacheada.
	 */
	public void loadTable(@Nonnull Class<? extends Shareable> entity) {

		List<?> currentAll = factory.getCurrentSession().createCriteria(entity)
				.list();
		for (Object object : currentAll) {
			Shareable s = (Shareable) object;
			addToCache(s.getUri(), object);
		}
	}

	/**
	 * @return map
	 */
	protected Map<String, Object> getMap() {

		if (map == null) {
			map = new HashMap<String, Object>();

		}

		return map;
	}

	@Transactional
	protected <T extends BaseEntity & Shareable> T getEntityByUri(
			Class<T> clazz, String uri) {

		Session s = factory.getCurrentSession();

		String query = "select e.id from " + getEntityName(clazz)
				+ " e where e.uri = :uri";
		Query q = s.createQuery(query);
		q.setParameter("uri", uri);

		Long l = (Long) q.uniqueResult();
		if (l == null) {
			return null;
		}

		try {
			T entity = clazz.newInstance();
			entity.setId(l);
			return entity;
		} catch (Exception e) {
			log.warn("Can't create a empty entity for cache, clazz: {}",
					clazz.getName(), e);
			throw new KarakuRuntimeException("Can't create a empty clazz ("
					+ clazz.getName() + ")", e);
		}

	}

	protected String getEntityName(Class<?> clazz) {

		return clazz.getName();
	}
}
