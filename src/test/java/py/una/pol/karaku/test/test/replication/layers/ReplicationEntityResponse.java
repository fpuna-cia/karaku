/*
 * @ReplicationEntityResponse.java 1.0 Nov 8, 2013 Sistema Integral de Gestion
 * Hospitalaria
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
