/*
 * @Change.java 1.0 Nov 7, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.replication.server;

import static py.una.pol.karaku.util.Checker.isValid;
import javax.annotation.Nonnull;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
public class Change<T> {

	@Nonnull
	private String id;
	@Nonnull
	private T entity;

	/**
	 * 
	 */
	public Change(@Nonnull T entity, @Nonnull String identifier) {

		this.entity = entity;
		this.id = identifier;
	}

	/**
	 * @return entity
	 */
	@Nonnull
	public T getEntity() {

		return entity;
	}

	/**
	 * @return id
	 */
	@Nonnull
	public String getId() {

		return id;
	}

	/**
	 * @param entity
	 *            entity para setear
	 */
	void setEntity(@Nonnull T entity) {

		this.entity = entity;
	}

	/**
	 * @param id
	 *            id para setear
	 */
	void setId(@Nonnull String id) {

		this.id = isValid(id, "Id of a change can not be null");
	}
}
