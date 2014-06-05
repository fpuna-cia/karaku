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
package py.una.pol.karaku.dao.entity;

import javax.annotation.Nonnull;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 * 
 */
public enum Operation {

	/**
	 * Define una operación de persistencia.
	 * 
	 * @see py.una.pol.karaku.dao.BaseDAO#add(Object)
	 */
	@Nonnull
	CREATE,
	/**
	 * Define una operación de actualización.
	 * 
	 * @see py.una.pol.karaku.dao.BaseDAO#update(Object)
	 */
	@Nonnull
	UPDATE,
	/**
	 * Define una operación de eliminación.
	 * 
	 * <p>
	 * Hay ciertos casos, donde la operación de eliminación puede ser omitida,
	 * por ejemplo en los casos de entidades que implementan la interfaz
	 * {@link py.una.pol.karaku.replication.Shareable}. Donde existe un
	 * <i>interceptor</i> que se encarga de modificarla y actualizarla.
	 * </p>
	 * 
	 * @see py.una.pol.karaku.dao.BaseDAO#remove(Object)
	 */
	@Nonnull
	DELETE;
}
