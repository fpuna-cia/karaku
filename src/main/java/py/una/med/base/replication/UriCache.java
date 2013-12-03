/*
 * @UriCache.java 1.0 Nov 27, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.replication;

import static py.una.med.base.util.Checker.notNull;
import java.util.HashMap;
import java.util.Map;
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
import py.una.med.base.domain.BaseEntity;
import py.una.med.base.log.Log;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 27, 2013
 * 
 */
@Component
public class UriCache {

	UriCache cache;

	Map<String, Object> map;

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
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity & Shareable> T getByUri(Class<T> clazz,
			String uri) throws EntityNotFoundException {

		notNull(clazz, "Can't get entity by null clazz");
		notNull(uri, "Can't get entity with null uri");
		if (!getMap().containsKey(uri)) {
			addToCache(uri, cache.getEntityByUri(clazz, uri));
		}
		Object toRet = map.get(uri);
		Validate.validState(toRet.getClass().equals(clazz),
				"Cache uri return incorrect types of entity, same uri?");
		return (T) toRet;
	}

	protected void addToCache(String uri, Object entity) {

		getMap().put(uri, entity);
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
			Class<T> clazz, String uri) throws EntityNotFoundException {

		Session s = factory.getCurrentSession();

		String query = "select e.id from " + getEntityName(clazz)
				+ " e where e.uri = :uri";
		Query q = s.createQuery(query);
		q.setParameter("uri", uri);

		Long l = (Long) q.uniqueResult();
		if (l == null) {
			throw new EntityNotFoundException(uri, getEntityName(clazz));
		}

		try {
			T entity = clazz.newInstance();
			entity.setId(l);
			return entity;
		} catch (Exception e) {
			log.warn("Can't create a empty entity for cache, clazz: {}",
					clazz.getName(), e);
			throw new RuntimeException("Can't create a empty clazz ("
					+ clazz.getName() + ")", e);
		}

	}

	protected String getEntityName(Class<?> clazz) {

		return clazz.getSimpleName();
	}
}
