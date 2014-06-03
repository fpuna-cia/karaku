/*
 * @ReplicatedEntityResponse.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.replication.layers;

import java.util.List;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
public class ReplicatedEntityResponse {

	List<ReplicatedEntity> entities;

	String id = "ZERO";

	/**
	 * @param entities
	 *            entities para setear
	 */
	public void setEntities(List<ReplicatedEntity> entities) {

		this.entities = entities;
	}
}
