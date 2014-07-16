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
package py.una.pol.karaku.jsf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import org.richfaces.component.UICalendar;
import org.springframework.stereotype.Component;

/**
 * Componente que provee funcionalidades para la manipulación de componentes
 * JSF.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 27, 2013
 * 
 */
@Component
public class JSFUtils {

	/**
	 * Formatea fechas:
	 * 
	 * <pre>
	 * Tue Nov 22 00:00:00 EET 2011
	 * </pre>
	 * 
	 * Que son el formato que es proveído por {@link UICalendar}.
	 */
	public static final String CALENDAR_DATE_PATTERN = "EE MMM dd HH:mm:ss zz yyyy";

	/**
	 * Formatea fechas:
	 * 
	 * <pre>
	 * Tue Nov 22 00:00:00 EET 2011
	 * </pre>
	 * 
	 * Que son el formato que es proveído por {@link UICalendar} (en los eventos
	 * que son del tiempo {@link UICalendar#isImmediate()}.
	 * 
	 * <h3>Ejemplo:</h3>
	 * 
	 * <pre>
	 * Date date = new SimpleDateFormat().parse(&quot;11/22/11 00:00 AM&quot;);
	 * Date jsf = jsfUtils.asDate(&quot;Tue Nov 22 00:00:00 PYST 2011&quot;);
	 * date.equals(jsf) ==> True
	 * </pre>
	 * 
	 * @param string
	 *            cadena con el formato de {@value #CALENDAR_DATE_PATTERN}
	 * @return {@link Date}, nunca <code>null</code>
	 * @throws ParseException
	 *             si no se puede parsear la expresión.
	 */
	public Date asDate(String string) throws ParseException {

		return new SimpleDateFormat(CALENDAR_DATE_PATTERN).parse(string);
	}

	/**
	 * Método necesario, pues {@link #findComponent(String)} no recupera
	 * correctamente los elementos anidados en un cc.
	 * 
	 * @param id
	 * @param uiComponent
	 * @return
	 */
	public static UIComponent find(String id, UIComponent uiComponent) {

		Iterator<UIComponent> it = uiComponent.getFacetsAndChildren();
		while (it.hasNext()) {
			UIComponent ui = it.next();
			if (id.equals(ui.getId())) {
				return ui;
			}
			UIComponent finded = find(id, ui);
			if (finded != null) {
				return finded;
			}
		}
		return null;
	}
}
