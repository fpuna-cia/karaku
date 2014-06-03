/*
 * @ReplicationEntityEndpoint.java 1.0 Nov 8, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.replication.layers;

import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import py.una.pol.karaku.replication.server.AbstractReplicationEndpoint;
import py.una.pol.karaku.replication.server.Bundle;
import py.una.pol.karaku.services.server.WebServiceDefinition;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 8, 2013
 * 
 */
@WebServiceDefinition(xsds = "")
public class ReplicationEntityEndpoint extends
		AbstractReplicationEndpoint<ReplicatedEntity, ReplicatedEntity> {

	@PayloadRoot(localPart = "replicationEntityRequest", namespace = "http://sigh.med.una.py/2013/schemas/test")
	@ResponsePayload
	public ReplicationEntityResponse replicationEntityRequest(
			@RequestPayload ReplicationEntityRequest request) {

		ReplicationEntityResponse toRet = new ReplicationEntityResponse();
		Bundle<ReplicatedEntity> bundle = getChanges(request.getLastId());
		toRet.setEntities(bundle.getEntities());
		toRet.setLastId(bundle.getLastId());
		return toRet;
	}
}
