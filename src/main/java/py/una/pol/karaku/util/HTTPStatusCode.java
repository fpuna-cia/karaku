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
package py.una.pol.karaku.util;

/**
 * Lista de codigos de respuesta del HTTP. Estos codigos de estatus estan
 * especificados en el RFC 2616.
 * 
 * @author Uriel Gonzalez
 * 
 */
public final class HTTPStatusCode {

	/**
	 * El servidor no ha encontrado nada que coincida con el Request-URI
	 */
	public static final String NOT_FOUND = "404";

	/**
	 * Respuesta estandar para peticiones correctas.
	 */
	public static final String OK = "200";

	public static final String BAD_REQUEST = "400";

	private HTTPStatusCode() {

	}
}
