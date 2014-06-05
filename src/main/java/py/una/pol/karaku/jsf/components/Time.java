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
package py.una.pol.karaku.jsf.components;

import java.util.Date;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

/**
 * FacesComponent que funciona de back end bean para el tag &lt time &gt
 *
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 *
 */
@FacesComponent(value = "customTimeBean")
@SuppressWarnings("deprecation")
public class Time extends UINamingContainer {

	private Date date;

	private Date getDate() {

		if (date == null) {

			Date d = (Date) getAttributes().get("date");
			if (d == null) {
				d = new Date();
			}
			date = d;
		}
		return date;
	}

	/**
	 * Cambia la cantidad de minutos del bean
	 *
	 * @param value
	 */

	public void setMinutes(final int value) {

		getDate().setMinutes(value);
	}

	/**
	 * cambia la cantidad de horas del bean
	 *
	 * @param value
	 */
	public void setHours(final int value) {

		getDate().setHours(value);
	}

	/**
	 * Retorna la cantidad de minutos del componente
	 *
	 * @return minutos actuales del componente
	 */
	public int getMinutes() {

		return getDate().getMinutes();
	}

	/**
	 * Retorna la cantidad de horas del componente
	 *
	 * @return horas actuales del componente
	 */
	public int getHours() {

		return getDate().getHours();
	}

	/**
	 * Configura la cantidad de segundos manejados por el componente
	 *
	 * @param value
	 */
	public void setSeconds(final int value) {

		getDate().setSeconds(value);
	}

	/**
	 * Retorna la cantidad de segundos
	 *
	 * @return seconds
	 */
	public int getSeconds() {

		return getDate().getSeconds();
	}

}
