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

import javax.xml.ws.WebFault;

/**
 * Describe una excepcion que ocurre durante el procesamiento de solicitudes
 * HTTP
 * 
 * @author Uriel Gonzalez
 * 
 */
@WebFault
public class HTTPException extends KarakuRuntimeException {

	private static final long serialVersionUID = 1L;

	private final String code;
	private final String shortDesciption;

	/**
	 * 
	 * @param code
	 *            Codigos de estatus especificados en el RFC 2616
	 */
	public HTTPException(String code, String shortDescription) {

		super(shortDescription);
		this.code = code;
		this.shortDesciption = shortDescription;
	}

	public HTTPException(String code, String shortDescription, Throwable cause) {

		super(shortDescription, cause);
		this.code = code;
		this.shortDesciption = shortDescription;
	}

	/**
	 * Recupera el codigo de estado de HTTP
	 * 
	 * @return codigo de estado
	 */
	public String getCode() {

		return this.code;
	}

	public String getShortDescription() {

		return this.shortDesciption;
	}

}
