/*
 * @ReplicationHandler.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import static py.una.med.base.util.Checker.notNull;
import java.util.Collection;
import java.util.Set;
import javax.persistence.Table;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.med.base.domain.BaseEntity;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.log.Log;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.Shareable;
import py.una.med.base.services.Converter;
import py.una.med.base.services.ConverterProvider;
import py.una.med.base.services.client.WSEndpoint;
import py.una.med.base.services.client.WSSecurityInterceptor;
import py.una.med.base.util.Checker;
import py.una.med.base.util.StringUtils;

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

	private static final long CALL_DELAY = 1000;

	@Autowired
	private IReplicationLogic logic;

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

	@Scheduled(fixedDelay = CALL_DELAY)
	public void doSync() {

		Set<ReplicationInfo> toReplicate = logic.getReplicationsToDo();

		for (ReplicationInfo ri : toReplicate) {

			doSync(ri);
		}
	}

	public void doSync(ReplicationInfo ri) {

		String lastId = replicate(ri);
		logic.notifyReplication(ri.getEntityClazz(), lastId);
	}

	/**
	 * @param ri
	 */
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

		for (Object dto : items) {
			Shareable entity = converter.toEntity((DTO) dto);
			merge(ri, entity);
		}

		return newLastId;
	}

	/**
	 * @param entity
	 */
	private void merge(ReplicationInfo ri, Shareable entity) {

		notNull(entity, "Cant merge a null entity");

		Session s = sessionFactory.getCurrentSession();

		BaseEntity realEntity = getPersistedByUri(s, ri, entity.getUri());

		if (realEntity == null) {
			s.persist(entity);
		} else {
			BaseEntity toPersist = (BaseEntity) entity;
			toPersist.setId(realEntity.getId());
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
	private BaseEntity getPersistedByUri(Session s, ReplicationInfo ri,
			String uri) {

		try {
			String query = "from " + getTableName(ri, uri)
					+ " where uri = :uri";
			Query q = s.createQuery(query);
			q.setParameter("uri", uri);

			return (BaseEntity) q.uniqueResult();
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
	private String getTableName(ReplicationInfo ri, String uri) {

		Table t = ri.getEntityClazz().getAnnotation(Table.class);
		if ((t == null) || StringUtils.isInvalid(t.name())) {
			return ri.getEntityClazz().getSimpleName();
		} else {
			return t.name();
		}
	};
}
