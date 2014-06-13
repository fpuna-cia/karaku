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
 * Utilidad para acceder a los diferentes mensajes de validación.
 * 
 * <p>
 * Se listan todos los mensajes disponibles para agregar en una entidad.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 * 
 */
public final class ValidationMessages {

	/**
	 * Mensaje para longitud máxima.
	 */
	public static final String LENGTH_MAX = "{LENGTH_MAX}";

	/**
	 * Mensaje para longitud.
	 */
	public static final String LENGTH = "{LENGTH}";

	/**
	 * Mensaje para definir que solo se admiten cadenas.
	 * 
	 * @see ValidationConstants#GN_WORDS_SPE
	 */
	public static final String ONLY_STRING = "{ONLY_STRING}";

	private ValidationMessages() {

	}

}
