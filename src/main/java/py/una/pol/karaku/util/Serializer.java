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

public final class Serializer {

	private Serializer() {

		// No-op
	}

	/**
	 * Dado un stringBuilder, un key y un value retorna el sb concatenado con el
	 * key, seguido de dos puntos y finalizado con ;<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>Nombre: algun nombre ; Apellido ; algun apellido</b>, retorna
	 * Nombre: algun nombre; Apellido: algun apellido
	 * </ol>
	 * 
	 * @param sb
	 * @param key
	 * @param value
	 * 
	 * @return sb
	 */
	public static StringBuilder contruct(StringBuilder sb, String key,
			String value) {

		if (sb.length() > 0) {
			sb.append("; ");
		}
		sb.append(key);
		sb.append(": ");
		sb.append(value);
		return sb;

	}
}
