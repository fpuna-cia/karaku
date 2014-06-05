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
package py.una.pol.karaku.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Esta clase es una factoria que crea (si necesita) y retorna una nueva
 * instancia ExceptionHandler.
 * 
 * @author Uriel González
 * @version 1.0, 07/01/13
 * @since 1.0
 */
public class KarakuJSFExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;

	public KarakuJSFExceptionHandlerFactory(ExceptionHandlerFactory parent) {

		this.parent = parent;
	}

	// creamos nuestro propio ExceptionHandler
	@Override
	public ExceptionHandler getExceptionHandler() {

		ExceptionHandler result = new KarakuJSFExceptionHandler(
				parent.getExceptionHandler());
		return result;
	}
}
