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
package py.una.pol.karaku.dynamic.tables;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;

import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;
import py.una.pol.karaku.util.I18nHelper;

/**
 * Columna que tiene como cabecera un {@link HtmlInputText}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
public abstract class SimpleHeaderColumn extends AbstractColumn {

	private HtmlOutputText header;

	public void setHeader(final HtmlOutputText header) {

		this.header = header;
	}

	@Override
	public HtmlOutputText getHeader() {

		return header;
	}

	public void setHeaderText(final String key) {

		if (getHeader() == null) {
			setHeader(KarakuComponentFactory.getHtmlOutputText());
		}
		getHeader().setValue(I18nHelper.getMessage(key));
	}

}
