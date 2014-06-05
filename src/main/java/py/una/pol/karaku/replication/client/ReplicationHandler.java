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
package py.una.pol.karaku.replication.client;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.EntityNotFoundException;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.replication.UriCache;
import py.una.pol.karaku.replication.client.ReplicationContextHolder.ReplicationContext;
import py.una.pol.karaku.services.Converter;
import py.una.pol.karaku.services.ConverterProvider;
import py.una.pol.karaku.services.client.WSEndpoint;
import py.una.pol.karaku.services.client.WSSecurityInterceptor;
import py.una.pol.karaku.util.Checker;
import py.una.pol.karaku.util.ListHelper;

/**
 * Servicio que se encarga de sincronizar las entidades periodicamente.
 * 
 * <p>
 * Para realizar test sobre la misma proveer un {@link WebServiceTemplate} con
 * el método
 * {@link WebServiceTemplate#marshalSendAndReceive(Object, org.springframework.ws.client.core.WebServiceMessageCallback)}
 * sobreescrito.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
@Service
public class ReplicationHandler {

	/**
	 * Define si la repliación esta activa o no.
	 */
	public static final String REPLICATION_ENABLED = "karaku.replication.enabled";

	public static final String LOGGER_NAME = "karaku.replication";

	private static final long CALL_DELAY = 60000;

	@Autowired
	private IReplicationLogic logic;

	@Autowired
	private PropertiesUtil util;

	@Log(name = LOGGER_NAME)
	private Logger log;

	@Autowired
	private WSSecurityInterceptor interceptor;

	@Autowired
	private ReplicationRequestFactory requestFactory;

	@Autowired
	private ReplicationResponseHandler responseHandlers;

	@Autowired
	private WebServiceTemplate template;

	@Autowired
	private ConverterProvider converterProvider;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private UriCache uriCache;

	private boolean skiped = false;

	private ReplicationHandler getThis() {

		return applicationContext.getBean(ReplicationHandler.class);
	}

	/**
	 * Realiza la sincronización de todas las entidades.
	 * 
	 * <p>
	 * Por defecto, la primera llamada es omitida, esta llamada se realiza
	 * cuando se levanta el contenedor y es improbable que otro sistema este
	 * disponible
	 * </p>
	 * <p>
	 * Todas aquellas entidades que deben ser sincronizadas serán sincronizadas,
	 * las mismas se ejecutan en una transacción por entidad, es decir que si
	 * una entidad falla, las demás pueden continuar sin problemas.
	 * </p>
	 */
	@Scheduled(fixedDelay = CALL_DELAY)
	public synchronized void doSync() {

		if (!isEnabled()) {
			// Eliminar de la cola
			return;
		}

		if (!skiped) {
			skiped = true;
			return;
		}

		Set<ReplicationInfo> toReplicate = logic.getReplicationsToDo();

		beginLocalThread();
		for (ReplicationInfo ri : toReplicate) {
			try {
				getThis().doSync(ri);
			} catch (Exception e) {
				log.warn("Can't sync entity {}", ri.getEntityClassName(), e);
			}
		}
		resetLocalThread();
	}

	/**
	 * @return
	 */
	private boolean isEnabled() {

		return util.getBoolean(REPLICATION_ENABLED, true)
				&& util.getBoolean("karaku.ws.client.enabled", false);
	}

	/**
	 * Sincroniza una sola entidad.
	 * 
	 * @param ri
	 */
	@Transactional
	public void doSync(ReplicationInfo ri) {

		Class<?> classToNotify = ri.getEntityClazz();
		notNull(classToNotify,
				"Can't sync a Replication info not loaded id: %s", ri.getId());
		updateLocalThread(ri);
		String lastId = replicate(ri);
		logic.notifyReplication(classToNotify, lastId);
	}

	private void updateLocalThread(ReplicationInfo ri) {

		notNull(ReplicationContextHolder.getContext(),
				"Null replication context, please initialize one");
		ReplicationContextHolder.getContext().setCurrentClassName(
				ri.getEntityClassName());
	}

	private void beginLocalThread() {

		ReplicationContext rc = new ReplicationContext();
		rc.setReplicationUser("REPLICATION_USER");
		ReplicationContextHolder.setContext(rc);
	}

	private void resetLocalThread() {

		ReplicationContextHolder.setContext(null);
		uriCache.clearCache();
	}

	private String replicate(ReplicationInfo ri) {

		WSEndpoint endpoint = ri.getWsEndpoint();

		Object request = requestFactory.createMessage(ri);

		Object response = sendRequest(endpoint, request);
		notNull(response, "Null response from WS, check connection");

		return handleResponse(ri, response);

	}

	/**
	 * @param ri
	 * @param response
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String handleResponse(ReplicationInfo ri, Object response) {

		Pair<String, Collection<?>> pair = responseHandlers
				.getChanges(response);
		Collection items = pair.getValue();
		String newLastId = pair.getKey();

		Converter converter = converterProvider.getConverter(
				ri.getEntityClazz(), ri.getDaoClazz());

		handleCacheAll(converter);

		int i = 0;
		for (Object obj : items) {
			DTO dto = (DTO) obj;
			i++;
			try {
				log.info("Process {}: {}/{} ({}%)",
						new Object[] { ri.getEntityClassName(), i,
								items.size(), i * 100 / items.size() });
				Shareable entity = converter.toEntity(dto);
				merge(ri, entity);
			} catch (EntityNotFoundException enf) {
				log.warn("Can't get entity from uri, entity with uri {}",
						dto.getUri());
				throw new KarakuRuntimeException("Can't replicate entity "
						+ obj.getClass().getSimpleName() + " uri = "
						+ dto.getUri(), enf);
			} catch (Exception e) {
				log.warn("Can't replicate entity with name {} and uri {}", obj
						.getClass().getSimpleName(), dto.getUri());
				throw new KarakuRuntimeException("Can't replicate entity "
						+ obj.getClass().getSimpleName() + " uri = "
						+ dto.getUri(), e);
			}
		}

		return newLastId;
	}

	/**
	 * @param converter
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void handleCacheAll(@Nonnull Converter converter) {

		if (converter.getClass().isAnnotationPresent(CacheAll.class)) {
			CacheAll ca = converter.getClass().getAnnotation(CacheAll.class);

			Class<Shareable>[] toCache = ca.value() != null ? ca.value()
					: ListHelper.asArray(Class.class,
							Collections.singleton(converter.getEntityType()));
			for (Class<Shareable> currentClass : toCache) {
				log.info("Caching: {} ", currentClass);
				uriCache.loadTable(currentClass);
			}
		}
	}

	/**
	 * @param entity
	 */
	private void merge(ReplicationInfo ri, Shareable entity) {

		notNull(entity, "Cant merge a null entity");

		Session s = sessionFactory.getCurrentSession();

		Long id = getPersistedIdByUri(s, ri, entity.getUri());

		if (id == null) {
			s.persist(entity);
		} else {
			BaseEntity toPersist = (BaseEntity) entity;
			toPersist.setId(id);
			s.merge(toPersist);
		}
		s.flush();
	}

	/**
	 * @param endpoint
	 * @param request
	 * @return
	 */
	private Object sendRequest(WSEndpoint endpoint, Object request) {

		log.debug("Calling Replication Service with URL {}", endpoint.getUrl());
		Object response = template.marshalSendAndReceive(endpoint.getUrl(),
				request, interceptor.getWebServiceMessageCallback(endpoint));
		log.debug("Web service call ended");
		return response;
	}

	/**
	 * @param s
	 * @param uri
	 * @return
	 */
	private Long getPersistedIdByUri(Session s, ReplicationInfo ri, String uri) {

		try {

			BaseEntity cached = uriCache.getByUriOrNull(
					checkAndCast(ri.getEntityClazz()), uri);
			if (cached != null) {
				return cached.getId();
			}
			String query = "select id from " + getEntityName(ri)
					+ " where uri = :uri";
			Query q = s.createQuery(query);
			q.setParameter("uri", uri);

			return (Long) q.uniqueResult();
		} catch (SQLGrammarException sge) {
			throw new KarakuRuntimeException(Checker.format(
					"Can't find column 'uri' of entity %s, "
							+ "please use the annotation @URI, "
							+ "or create a field with name"
							+ "URI of type String.", ri.getEntityClazz()
							.getSimpleName()), sge);
		}

	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity & Shareable> Class<T> checkAndCast(
			Class<?> param) {

		if (Shareable.class.isAssignableFrom(param)
				&& BaseEntity.class.isAssignableFrom(param)) {
			return (Class<T>) param;
		}
		throw new KarakuRuntimeException(
				"Can't sync a not BaseEntity and Not Shareable entity: "
						+ param.getName());
	}

	/**
	 * @param ri
	 * @param uri
	 * @return
	 */
	private String getEntityName(ReplicationInfo ri) {

		notNull(ri, "Can't get entity from null Replicationinfo");
		return ri.getEntityClazz().getSimpleName();
	}
}
