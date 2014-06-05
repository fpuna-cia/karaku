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
package py.una.pol.karaku.dynamic.forms;

import py.una.pol.karaku.util.I18nHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public abstract class LabelField extends Field {

	private String label;

	/**
	 * @return label
	 */
	public String getLabel() {

		return label;
	}

	/**
	 * Busca en el archivo de internacionalización y luego asigna el valor a la
	 * etiqueta de este {@link Field}
	 * 
	 * @param label
	 *            key del archivo de internacionalización.
	 */
	public void setLabel(String label) {

		this.label = (I18nHelper.getMessage(label));
	}

	/**
	 * A diferencia de {@link #setLabel(String)} no busca en el archivo de
	 * bundles, útil para cuando se buscan cadenas personalizadas.
	 * 
	 * @param label
	 */
	public void setLabelEscaped(String label) {

		this.label = label;
	}
}
