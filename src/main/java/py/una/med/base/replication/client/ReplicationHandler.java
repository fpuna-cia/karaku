/*
 * @ReplicationHandler.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import static py.una.med.base.util.Checker.notNull;
import java.util.Collection;
import java.util.Set;
import javax.annotation.PostConstruct;
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
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.domain.BaseEntity;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.log.Log;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.EntityNotFoundException;
import py.una.med.base.replication.Shareable;
import py.una.med.base.replication.client.ReplicationContextHolder.ReplicationContext;
import py.una.med.base.services.Converter;
import py.una.med.base.services.ConverterProvider;
import py.una.med.base.services.client.WSEndpoint;
import py.una.med.base.services.client.WSSecurityInterceptor;
import py.una.med.base.util.Checker;

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
	private static final String REPLICATION_ENABLED = "karaku.replication.enabled";

	private static final long CALL_DELAY = 60000;

	@Autowired
	private IReplicationLogic logic;

	@Autowired
	private PropertiesUtil util;

	@Log
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

	private ReplicationHandler replicationHandler;

	private boolean skiped = false;

	@PostConstruct
	private void getThis() {

		replicationHandler = applicationContext
				.getBean(ReplicationHandler.class);
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

		if (!util.getBoolean(REPLICATION_ENABLED, true)) {
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
				replicationHandler.doSync(ri);
			} catch (Exception e) {
				log.warn("Can't sync entity {}", ri.getEntityClassName(), e);
			}
		}
		resetLocalThread();
	}

	/**
	 * Sincroniza una sola entidad.
	 * 
	 * @param ri
	 */
	@Transactional
	public void doSync(ReplicationInfo ri) {

		updateLocalThread(ri);
		String lastId = replicate(ri);
		logic.notifyReplication(ri.getEntityClazz(), lastId);
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

		for (Object obj : items) {
			DTO dto = (DTO) obj;
			try {
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
