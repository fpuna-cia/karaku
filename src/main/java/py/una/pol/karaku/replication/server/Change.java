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
