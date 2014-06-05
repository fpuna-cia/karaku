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

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;

/**
 * Columna que tiene como cabecera y cuerpo un {@link HtmlInputText}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
public class SimpleColumn extends SimpleHeaderColumn {

	private final HtmlOutputText body;

	public SimpleColumn() {

		super();
		body = KarakuComponentFactory.getHtmlOutputText();

	}

	/**
	 * Asigna a esta columna la expresión pasada como valor para el atributo
	 * <code>value</code>. Si el cuerpo no es un {@link HtmlOutputText} lanza
	 * una excepción
	 * 
	 * @param valueExpression
	 */
	public void bindExpressionValue(final ValueExpression valueExpression) {

		getBody().setValueExpression("value", valueExpression);
	}

	public void bindAttribute(final String attribute, final Class<?> returnType) {

		bindExpressionValue(getHelper().makeValueExpression(
				getAttributeStringExpression(attribute), returnType));
	}

	@Override
	public HtmlOutputText getBody() {

		return body;
	}

}
