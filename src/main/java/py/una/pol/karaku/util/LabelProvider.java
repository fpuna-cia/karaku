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
 * Define un formato para un objeto dado.
 *
 * <p>
 * Sirve para poder mostrar información al usuario, de tal forma a no reescribir
 * el método.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 *
 */
public interface LabelProvider<T> {

	/**
	 * Define una cadena legible para un objeto dado.
	 *
	 * <p>
	 * Este método debe ser <code>null</code> safe, es decir no debería fallar
	 * cuando se le pase <code>null</code>.
	 * </p>
	 *
	 * <h3>Ejemplo:</h3>
	 *
	 * <pre>
	 * public class PaisLabelProvider implements LabelProvider{@literal <}Pais> {
	 * 	public String getAsString(Pais p) {
	 * 		if (p == null) return "";
	 * 		return p.getDescripcion();
	 * 	}
	 * }
	 * </pre>
	 *
	 * @param object
	 *            objeto a formatear
	 * @return cadena formateada.
	 */
	String getAsString(T object);

	/**
	 * Label provider que retorna el {@link #toString()} de un objeto.
	 *
	 * @author Arturo Volpe
	 * @since 2.2.8
	 * @version 1.0 Oct 17, 2013
	 *
	 */
	class StringLabelProvider<T> implements LabelProvider<T> {

		@Override
		public String getAsString(T object) {

			return object == null ? "" : object.toString();
		}
	}

}
