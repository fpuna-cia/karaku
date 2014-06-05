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

import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 8, 2013
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "entities", "lastId" })
@XmlRootElement(name = "replicationEntityResponse")
public class ReplicationEntityResponse {

	@XmlElement(required = true)
	private Set<ReplicatedEntity> entities;

	@XmlElement(required = true)
	private String lastId;

	/**
	 * @return lastId
	 */
	public String getLastId() {

		return lastId;
	}

	/**
	 * @param lastId
	 *            lastId para setear
	 */
	public void setLastId(String lastId) {

		this.lastId = lastId;
	}

	/**
	 * @return entities
	 */
	public Set<ReplicatedEntity> getEntities() {

		return entities;
	}

	/**
	 * @param entities
	 *            entities para setear
	 */
	public void setEntities(Set<ReplicatedEntity> entities) {

		this.entities = entities;
	}

}
