/*
 * @JSFUtils.java 1.0 Sep 27, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.jsf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
}
