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
package py.una.pol.karaku.replication;

import py.una.pol.karaku.exception.KarakuException;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 28, 2013
 * 
 */
public class EntityNotFoundException extends KarakuException {

	private static final String MESSAGE = "Can't get entity %s with the uri %s";

	/**
	 * 
	 */
	private static final long serialVersionUID = 3735749631559302678L;

	/**
	 * Crea una nueva excepción con un mensaje descriptivo para la entidad
	 * especifica que no fue encontrada.
	 * 
	 * @param uri
	 * @param className
	 */
	public EntityNotFoundException(String uri, String className) {

		super(String.format(MESSAGE, className, uri));
	}
}
