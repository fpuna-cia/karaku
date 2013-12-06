/*
 * @Change.java 1.0 Nov 7, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.replication.server;

import org.apache.commons.lang3.Validate;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
public class Change<T> {

	private String id;
	private T entity;

	/**
	 * @return entity
	 */
	public T getEntity() {

		return entity;
	}

	/**
	 * @return id
	 */
	public String getId() {

		return id;
	}

	/**
	 * @param entity
	 *            entity para setear
	 */
	void setEntity(T entity) {

		this.entity = entity;
	}

	/**
	 * @param id
	 *            id para setear
	 */
	void setId(String id) {

		Validate.notEmpty(id, "Id of a change can not be null");
		this.id = id;
	}

}
