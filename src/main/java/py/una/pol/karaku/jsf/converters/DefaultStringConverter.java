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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Es un {@link FacesConverter} que no realiza ninguna acción, es utilizado
 * cuando no se desea utilizar el {@link StringConverter}, su uso es asi:
 * 
 * <pre>
 * &lt;h:selectOneMenu "
 * converter = &quot;oldStringConverter&quot; /&gt;
 *     ...
 * &lt;/h:selectOneMenu&gt;
 * </pre>
 * 
 * @author Arturo Volpe
 * @since 1.3.2
 * @version 1.0 Jun 6, 2013
 * 
 */
@FacesConverter(value = "oldStringConverter")
public class DefaultStringConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {

		if (object == null) {
			return "";
		}
		return object.toString();
	}

}
