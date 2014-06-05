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

import java.util.List;
import javax.annotation.Nonnull;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 21, 2013
 * 
 */
public final class Checker {

	private Checker() {

	}

	/**
	 * Chequea si un parámetro es nulo.
	 * 
	 * <p>
	 * Si el parámetro es nulo, entonces lanza una excepción del tipo
	 * {@link IllegalArgumentException}.
	 * </p>
	 * 
	 * @param object
	 *            objeto a verificar
	 */
	@Nonnull
	public static <T> T notNull(T object) {

		return notNull(object, "Object not null is not allowed");
	}

	@Nonnull
	public static <T> T notNull(T object, String message, Object ... arguments) {

		if (object == null) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return object;
	}

	/**
	 * Chequea que una lista no posea elementos.
	 * 
	 * @param list
	 *            lista a examinar
	 * @param message
	 *            mensaje, con <code>%s</code> como <i>wildcard</i> para los
	 *            argumentos.
	 * @param arguments
	 *            argumentos para formatear la cadena
	 * @return la misma lista
	 */
	public static <T> List<T> isEmpty(List<T> list, String message,
			Object ... arguments) {

		if (ListHelper.hasElements(list)) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return list;
	}

	/**
	 * Chequea si una lista posee elementos.
	 * 
	 * @param list
	 *            lista a verificar
	 * @param message
	 *            mensaje, con <code>%s</code> como <i>wildcard</i> para los
	 *            argumentos.
	 * @param arguments
	 *            argumentos para formatear la cadena
	 * @throws IllegalArgumentException
	 *             si la cadena no es válida
	 * @return la lista pasada
	 */
	public static <T> List<T> isNotEmpty(List<T> list, String message,
			Object ... arguments) {

		if (!ListHelper.hasElements(list)) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return list;
	}

	/**
	 * @deprecated use {@link #isValid(CharSequence, String, Object...)}
	 * @param string
	 * @param message
	 * @param arguments
	 * @return
	 */
	@Nonnull
	@Deprecated
	public static <T extends CharSequence> T notValid(T string, String message,
			Object ... arguments) {

		if (string == null || StringUtils.isInvalid(string.toString())) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return string;
	}

	/**
	 * Verifica que una cadena sea válida.
	 * <p>
	 * Dentro de este contexto, válida significa que no sea nula y que tenga al
	 * menos un carácter distinto a un espacio vacío.
	 * </p>
	 * 
	 * @param string
	 *            cadena a verificar
	 * @param message
	 *            mensaje, con <code>%s</code> como <i>wildcard</i> para los
	 *            argumentos.
	 * @param arguments
	 *            argumentos para formatear la cadena
	 * @return la cadena validada.
	 * @throws IllegalArgumentException
	 *             si la cadena no es válida
	 */
	@Nonnull
	public static <T extends CharSequence> T isValid(T string, String message,
			Object ... arguments) {

		if (string == null || StringUtils.isInvalid(string.toString())) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return string;
	}

	public static String format(String message, Object ... arguments) {

		// TODO cambiar por un mecanismo mas eficiente, ver guava Preconditions.
		return String.format(message, arguments);
	}
}
