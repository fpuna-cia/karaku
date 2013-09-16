/*
 * @FormatProvider.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import javax.el.Expression;
import javax.faces.component.FacesComponent;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.stereotype.Component;

/**
 * Singleton que se encarga de realizar los formatos de {@link Date} y
 * {@link BigDecimal} en el sistema.
 * <p>
 * Todas las conversiones de {@link String} a {@link Date} o {@link BigDecimal}
 * <b>deben</b> ser realizadas por esta clase.
 * </p>
 * <p>
 * Para acceder a este componente se pueden utilizar alguno de los siguientes
 * mecanismos:
 * <ol>
 * <li> {@link Expression}: #{fp}</li>
 * <li> {@link Component}: {@literal @}{@link Autowire} {@link FormatProvider}
 * fp;</li>
 * <li> {@link FacesComponent}: Util.getSpringBeanByJSFContext(context,
 * FormatProvider.class)</li>
 * 
 * </ol>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * @see Util#getSpringBeanByJSFContext(javax.faces.context.FacesContext, Class)
 */
@Component(value = FormatProvider.FORMAT_PROVIDER_NAME)
public class FormatProvider {

	/**
	 * Nombre de este bean, se reduce el nombre para disminuir la verbosidad
	 * desde jsf.
	 */
	public static final String FORMAT_PROVIDER_NAME = "fp";

	/**
	 * 
	 * Formato de las fechas en el sistema. Este formato incluye el año
	 * completo, y esta separado por guiones.
	 * <p>
	 * Si se desea que se formateen hora y minutos ver {@link #DATETIME_FORMAT}
	 * </p>
	 * 
	 * @see #DATE_SHORT_FORMAT
	 */
	public static final String DATE_FORMAT = "dd-MM-yyyy";

	/**
	 * Formato de las fechas cortas, este formato incluye los dos últimos
	 * numeros de la fecha.
	 * <p>
	 * Si se desea que se formateen hora y los minutos, ver
	 * {@link #DATETIME_SHORT_FORMAT}
	 * </p>
	 * 
	 * @see #DATE_FORMAT
	 */
	public static final String DATE_SHORT_FORMAT = "dd-MM-yy";

	/**
	 * Formato de las fechas que representan horas y minutos, no incluye los
	 * segundos.
	 * 
	 */
	public static final String TIME_FORMAT = "HH:mm";

	/**
	 * Formato de las fechas con horas y minutos.
	 * <p>
	 * Este formato incluye el año completo separado por guiones.
	 * </p>
	 * 
	 * 
	 * @see #DATE_SHORT_FORMAT
	 */
	public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";

	/**
	 * Formato de las fechas cortas con horas y minutos.
	 * <p>
	 * Este formato incluye el año completo separado por guiones.
	 * </p>
	 */
	public static final String DATETIME_SHORT_FORMAT = "dd-MM-yy HH:mm";

	private static final String EMPTY_STRING = "";

	/**
	 * Formato para los números. Se compone de una cantidad variable de numeros
	 * a la izquierda, y a lo sumo dos a la derecha.
	 * <p>
	 * La cadena mas pequeña que puede generar este formato es <code>"0"</code>,
	 * notar que el método {@link #asNumber(BigDecimal)} puede retornar
	 * <code>""</code> si se le pasa como parámetro <code>null</code>.
	 * </p>
	 */
	public static final String NUMBER_FORMAT = "###,###,###,###,###,###,###,###,###,###,##0.##";

	/**
	 * Formato para las monedas. Se compone de una cantidad variable de numeros
	 * a la izquierda, y a lo sumo dos a la derecha.
	 */

	public static final String MONEY_FORMAT = "###,###,###,###,###,###,###,###,###,###,##0";

	private Map<String, SimpleDateFormat> formats;

	private boolean initialized = false;

	private DecimalFormatSymbols dfs;

	private synchronized void init() {

		if (initialized) {
			return;
		}
		initialized = true;
		formats = new HashMap<String, SimpleDateFormat>(5);
		formats.put(DATE_FORMAT, new SimpleDateFormat(DATE_FORMAT));
		formats.put(DATE_SHORT_FORMAT, new SimpleDateFormat(DATE_SHORT_FORMAT));
		formats.put(TIME_FORMAT, new SimpleDateFormat(TIME_FORMAT));
		formats.put(DATETIME_FORMAT, new SimpleDateFormat(DATETIME_FORMAT));
		formats.put(DATETIME_SHORT_FORMAT, new SimpleDateFormat(
				DATETIME_SHORT_FORMAT));
		for (SimpleDateFormat sdf : formats.values()) {
			sdf.setLenient(false);
		}
	}

	/**
	 * Formatea una fecha con el formato {@link #DATE_SHORT_FORMAT}
	 * <p>
	 * Esta fecha debe ser usada en grillas y en lugares donde el espacio es
	 * reducido. Si es el caso en que se deben mostrar horas y minutos, se debe
	 * usar {@link #asShortDateTime(Date)} o {@link #asTime(Date)}.
	 * </p>
	 * 
	 * <pre>
	 * 	Dado:
	 * 		Date = 11-11-2013 22:15:00
	 * 	Retorna
	 * 		11-11-13
	 * </pre>
	 * 
	 * @param date
	 *            fecha que se desea parsear.
	 * @return cadena con el formato {@value #DATE_SHORT_FORMAT}, retorna
	 *         <code>""</code> si el parámetro es <code>null</code>
	 */
	public String asShortDate(@Nullable Date date) {

		return dateToString(date, DATE_SHORT_FORMAT);
	}

	/**
	 * Parsea una fecha con el formato {@link #DATE_SHORT_FORMAT}.
	 * <p>
	 * Ejemplo de uso:
	 * 
	 * <pre>
	 * 	fp.parseShortDate("11-11-13")
	 * Retorna:
	 * 	Date con:
	 * 		Fecha:	11-11-2013
	 * 		Hora:	00:00:00:00
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param string
	 *            cadena con el formato {@value #DATE_SHORT_FORMAT}
	 * @return {@link Date} correspondiente
	 * @throws ParseException
	 *             si <b>string</b> no es una fecha válida.
	 */
	public Date parseShortDate(@Nullable String string) throws ParseException {

		return stringToDate(string, DATE_SHORT_FORMAT);
	}

	/**
	 * Formatea una fecha con el formato {@link #DATE_FORMAT}
	 * <p>
	 * Esta fecha debe ser usada en detalles y en lugares donde el espacio no es
	 * reducido. Si es el caso en que se deben mostrar horas y minutos, se debe
	 * usar {@link #asDateTime(Date)} o {@link #asTime(Date)}.
	 * </p>
	 * 
	 * <pre>
	 * 	Dado:
	 * 		Date = 11-11-2013 22:15:00
	 * 	Retorna
	 * 		11-11-2013
	 * </pre>
	 * 
	 * @param date
	 *            fecha que se desea parsear.
	 * @return cadena con el formato {@value #DATE_FORMAT}, retorna
	 *         <code>""</code> si el parámetro es <code>null</code>
	 */
	public String asDate(@Nullable Date date) {

		return dateToString(date, DATE_FORMAT);

	}

	/**
	 * Parsea una fecha con el formato {@link #DATE_FORMAT}.
	 * <p>
	 * Ejemplo de uso:
	 * 
	 * <pre>
	 * 	fp.parseShortDate("11-11-2013")
	 * Retorna:
	 * 	Date con:
	 * 		Fecha:	11-11-2013
	 * 		Hora:	00:00:00:00
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param string
	 *            cadena con el formato {@value #DATE_FORMAT}
	 * @return {@link Date} correspondiente
	 * @throws ParseException
	 *             si <b>string</b> no es una fecha válida.
	 */
	public Date parseDate(@Nullable String string) throws ParseException {

		return stringToDate(string, DATE_FORMAT);
	}

	/**
	 * Formatea una fecha con el formato {@link #TIME_FORMAT}
	 * <p>
	 * Este formato debe ser usado cuando al fecha no es importante, solo la
	 * hora y los minutos.
	 * </p>
	 * 
	 * <pre>
	 * 	Dado:
	 * 		Date = 11-11-2013 22:15:00
	 * 	Retorna
	 * 		22:15
	 * </pre>
	 * 
	 * @param date
	 *            fecha que se desea parsear.
	 * @return cadena con el formato {@value #TIME_FORMAT}, retorna
	 *         <code>""</code> si el parámetro es <code>null</code>
	 */
	public String asTime(@Nullable Date date) {

		return dateToString(date, TIME_FORMAT);

	}

	/**
	 * Parsea una fecha con el formato {@link #TIME_FORMAT}.
	 * <p>
	 * Ejemplo de uso:
	 * 
	 * <pre>
	 * 	fp.parseShortDate("15:30")
	 * Retorna:
	 * 	Date con:
	 * 		Fecha:	01-01-1970
	 * 		Hora:	15:39:00:00
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param string
	 *            cadena con el formato {@value #DATE_FORMAT}
	 * @return {@link Date} correspondiente
	 * @throws ParseException
	 *             si <b>string</b> no es una fecha válida.
	 */
	public Date parseTime(@Nullable String string) throws ParseException {

		return stringToDate(string, TIME_FORMAT);
	}

	/**
	 * Formatea una fecha con el formato {@link #DATETIME_FORMAT}
	 * <p>
	 * Este formato debe ser usado cuando al fecha no es importante, solo la
	 * hora y los minutos.
	 * </p>
	 * 
	 * <pre>
	 * 	Dado:
	 * 		Date = 11-11-2013 22:15:00
	 * 	Retorna
	 * 		11-11-2013 22:15
	 * </pre>
	 * 
	 * @param date
	 *            fecha que se desea parsear.
	 * @return cadena con el formato {@value #DATETIME_FORMAT}, retorna
	 *         <code>""</code> si el parámetro es <code>null</code>
	 */
	public String asDateTime(@Nullable Date date) {

		return dateToString(date, DATETIME_FORMAT);
	}

	/**
	 * Parsea una fecha con el formato {@link #DATETIME_FORMAT}.
	 * <p>
	 * Ejemplo de uso:
	 * 
	 * <pre>
	 * 	fp.parseShortDate("2013-11-11 15:39")
	 * Retorna:
	 * 	Date con:
	 * 		Fecha:	11-11-2013
	 * 		Hora:	15:39:00:00
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param string
	 *            cadena con el formato {@value #DATE_FORMAT}
	 * @return {@link Date} correspondiente
	 * @throws ParseException
	 *             si <b>string</b> no es una fecha válida.
	 */
	public Date parseDateTime(@Nullable String string) throws ParseException {

		return stringToDate(string, DATETIME_FORMAT);
	}

	/**
	 * Formatea una fecha con el formato {@link #DATETIME_SHORT_FORMAT}
	 * <p>
	 * Este formato debe ser usado cuando al fecha no es importante, solo la
	 * hora y los minutos.
	 * </p>
	 * 
	 * <pre>
	 * 	Dado:
	 * 		Date = 11-11-13 22:15:00
	 * 	Retorna
	 * 		11-11-13 22:15
	 * </pre>
	 * 
	 * @param date
	 *            fecha que se desea parsear.
	 * @return cadena con el formato {@value #DATETIME_SHORT_FORMAT}, retorna
	 *         <code>""</code> si el parámetro es <code>null</code>
	 */
	public String asShortDateTime(@Nullable Date date) {

		return dateToString(date, DATETIME_SHORT_FORMAT);
	}

	/**
	 * Parsea una fecha con el formato {@link #DATETIME_SHORT_FORMAT}.
	 * <p>
	 * Ejemplo de uso:
	 * 
	 * <pre>
	 * 	fp.parseShortDate("13-11-11 15:39")
	 * Retorna:
	 * 	Date con:
	 * 		Fecha:	11-11-2013
	 * 		Hora:	15:39:00:00
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param string
	 *            cadena con el formato {@value #DATETIME_SHORT_FORMAT}
	 * @return {@link Date} correspondiente
	 * @throws ParseException
	 *             si <b>string</b> no es una fecha válida.
	 */
	public Date parseShortDateTime(@Nullable String string)
			throws ParseException {

		return stringToDate(string, DATETIME_SHORT_FORMAT);
	}

	/**
	 * Parsea un número con el formato estandar.
	 * 
	 * <p>
	 * 
	 * <table>
	 * <tr>
	 * <td><b>Numero</b></td>
	 * <td><b>Convertido</b></td>
	 * </tr>
	 * <tr>
	 * <td>1000000000000</td>
	 * <td>1.000.000.000.000</td>
	 * </tr>
	 * <tr>
	 * <td>100000</td>
	 * <td>100.000</td>
	 * </tr>
	 * <tr>
	 * <td>100</td>
	 * <td>100</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>0.111</td>
	 * <td>0,11</td>
	 * </tr>
	 * <tr>
	 * <td>0.119</td>
	 * <td>0,12</td>
	 * </tr>
	 * <tr>
	 * <td>0.0111</td>
	 * <td>0,01</td>
	 * </tr>
	 * <tr>
	 * <td>0.0159</td>
	 * <td>0,02</td>
	 * </tr>
	 * </table>
	 * </p>
	 * 
	 * @param bd
	 *            {@link BigDecimal} a convertir.
	 * @return cadena formateada, segun los ejemplos anteriores
	 */
	public String asNumber(@NotNull BigDecimal bd) {

		if (bd == null) {
			throw new IllegalArgumentException("Cant parse null BigIntegers");
		}
		return getNumberFormat(NUMBER_FORMAT).format(bd);

	}

	/**
	 * Parsea una cadena con el formato pasado.
	 * 
	 * @param date
	 *            fecha a serializar
	 * @param format
	 *            formato deseado
	 * @return {@link String} con el formato deseado.
	 */
	protected synchronized String dateToString(Date date, String format) {

		if (date == null) {
			return EMPTY_STRING;
		}
		return getFormat(format).format(date);
	}

	/**
	 * Formatea una fecha con el formato dado
	 * 
	 * @param string
	 *            cadena a formatear
	 * @param format
	 *            formato deseado
	 * @return {@link Date} con los demas parámetros a cero.
	 * @throws ParseException
	 *             si el formato no es adecuado.
	 */
	protected synchronized Date stringToDate(String string, String format)
			throws ParseException {

		if (string == null || EMPTY_STRING.equals(string)) {
			return null;
		}
		SimpleDateFormat sdf = getFormat(format);
		ParsePosition psp = new ParsePosition(0);
		if (string.length() != format.length()) {
			throwException(string, format);
		}
		Date toRet = sdf.parse(string, psp);
		if (psp.getIndex() != string.length()) {
			throwException(string, format);
		}
		return toRet;
	}

	private void throwException(String string, String format)
			throws ParseException {

		throw new ParseException("Imposible parsear cadena: " + string
				+ " con formato " + format, 0);
	}

	/**
	 * Provee un formato
	 * 
	 * @param format
	 * @return
	 */
	private SimpleDateFormat getFormat(String format) {

		if (!initialized) {
			init();
		}
		if (!formats.containsKey(format)) {
			throw new IllegalArgumentException("Unknow format " + format);
		}
		return formats.get(format);
	}

	private NumberFormat getNumberFormat(String format) {

		DecimalFormat df = new DecimalFormat(format);
		df.setDecimalFormatSymbols(getDFS());
		return df;
	}

	/**
	 * @return
	 */
	private DecimalFormatSymbols getDFS() {

		if (dfs == null) {
			dfs = new DecimalFormatSymbols(Locale.getDefault());
			dfs.setDecimalSeparator(',');
			dfs.setGroupingSeparator('.');
		}

		return dfs;
	}
}
