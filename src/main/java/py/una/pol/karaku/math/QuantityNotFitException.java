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
package py.una.pol.karaku.math;

import py.una.pol.karaku.exception.KarakuRuntimeException;

/**
 * Conversión de tipos que reducen la precisión no se permiten.
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 8, 2013
 *
 */
public final class QuantityNotFitException extends KarakuRuntimeException {

	private static final long serialVersionUID = -3053472034060084904L;

	/**
	 * Nueva instancia con el mensaje definido
	 */
	public QuantityNotFitException(Quantity from, Number to) {

		super(buildMessage(from, to));
	}

	private static String buildMessage(Quantity from, Number to) {

		StringBuilder sb = new StringBuilder(
				"Precision loss detected, the quantity ");
		sb.append(from.toString());
		sb.append(" does not fit in").append(to.getClass());
		sb.append("(Converted value is: ").append(to);
		sb.append(")");
		return sb.toString();
	}
}
