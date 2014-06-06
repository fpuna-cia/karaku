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
