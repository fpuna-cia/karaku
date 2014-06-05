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
package py.una.pol.karaku.jsf.converters;

import java.lang.reflect.Field;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import py.una.pol.karaku.dao.annotations.CaseSensitive;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.util.ELHelper;

/**
 * Es un {@link Converter} que sanitiza las entidades, para que la sanitización
 * sea completa, la expresión que la define debe ser similar a
 * {@link #EXTRACT_FIELD_FROM_EXPRESSION_REGEX}. <br />
 * 
 * Bajo este contexto, sanitizar significa:
 * <ol>
 * <li>Eliminar espacios adelante y atras con {@link String#trim()}</li>
 * <li>Convertir a mayúsculas con {@link String#toUpperCase()}, siempre y cuando
 * la anotación {@link CaseSensitive} no este presente</li>
 * 
 * @author Arturo Volpe
 * @since 1.3.2
 * @version 1.0 Jun 6, 2013
 * 
 */
@FacesConverter(forClass = String.class)
public class StringConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (value == null || "".equals(value)) {
			return null;
		}

		String toRet = value.trim();

		ValueExpression val = component.getValueExpression("value");
		String beanExpression = val.getExpressionString();
		if (!(beanExpression == null || "".equals(beanExpression))) {
			Field f = ELHelper.getFieldByExpression(beanExpression);
			if (f == null || f.getAnnotation(CaseSensitive.class) == null) {
				toRet = toRet.toUpperCase();
			}
		}
		return toRet;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {

		if (object == null) {
			return null;
		}

		if (object instanceof String) {
			return ((String) object).trim();
		}
		throw new KarakuRuntimeException(
				"StringConverter is only for strings (not for"
						+ object.getClass() + ")");
	}

}
