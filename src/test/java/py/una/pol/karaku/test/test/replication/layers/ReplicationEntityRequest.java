/*
 * @ReplicationEntityRequest.java 1.0 Nov 8, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.replication.layers;

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
@XmlType(name = "", propOrder = "lastId")
@XmlRootElement(name = "replicationEntityRequest")
public class ReplicationEntityRequest {

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

}
