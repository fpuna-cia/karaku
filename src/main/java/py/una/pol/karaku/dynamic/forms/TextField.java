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

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlInputText;
import py.una.pol.karaku.util.I18nHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class TextField extends LabelField {

	/**
	 * Tipo de este componente
	 */
	private static final String TYPE = "py.una.pol.karaku.dynamic.forms.TextField";

	private HtmlInputText bind;

	public TextField() {

		bind = KarakuComponentFactory.getHtmlInputText();
		bind.setId(getId());

	}

	@Override
	public void setId(String id) {

		getBind().setId(id);
		super.setId(id);
	}

	/**
	 * Retorna la longitud maxima que permite el textfield
	 * 
	 * @return maxlength
	 * @see javax.faces.component.html.HtmlInputText#getMaxlength()
	 */
	public int getMaxlength() {

		return bind.getMaxlength();
	}

	public boolean isRequired() {

		return bind.isRequired();
	}

	public void setRequiredMessage(String key) {

		bind.setRequiredMessage(I18nHelper.getMessage(key));
	}

	public void setRequired(boolean required) {

		bind.setRequired(required);
	}

	public String getRequiredMessage() {

		return bind.getRequiredMessage();
	}

	/**
	 * Asigna una longitud máxima para el componente en el lado del cliente,
	 * esta propiedad debe usarse en conjunto con un control en la logica de
	 * negocios.
	 * 
	 * @param maxlength
	 *            maxlength
	 * @see javax.faces.component.html.HtmlInputText#setMaxlength(int)
	 */
	public void setMaxlength(final int maxlength) {

		bind.setMaxlength(maxlength);
	}

	@Override
	public String getType() {

		return TYPE;
	}

	public void setValue(final ValueExpression expression) {

		bind.setValueExpression("value", expression);
	}

	/**
	 * @return value
	 */
	public HtmlInputText getBind() {

		return bind;
	}

	/**
	 * @param value
	 *            value para setear
	 */
	public void setBind(final HtmlInputText value) {

		this.bind = value;
	}

	@Override
	public boolean enable() {

		getBind().setDisabled(false);
		return true;
	}

	@Override
	public boolean disable() {

		getBind().setDisabled(true);
		return true;
	}

}
