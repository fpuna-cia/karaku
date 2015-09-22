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
package py.una.pol.karaku.util;

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
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.math.Quantity;

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
 * <li> {@link javax.el.Expression}: #{fp}</li>
 * <li> {@link Component}: {@literal @}Autowire {@link FormatProvider} fp;</li>
 * <li> {@link javax.faces.component.FacesComponent}:
 * Util.getSpringBeanByJSFContext(context, FormatProvider.class)</li>
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

    private static final Logger LOG = LoggerFactory
            .getLogger(FormatProvider.class);
    private static final String CANT_PARSE_NULL_QUANTITY = "Cant parse null quantity";

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
     * Formato para los números. Se compone de una cantidad variable de numeros
     * a la izquierda, y a lo sumo dos a la derecha.
     * <p>
     * La cadena mas pequeña que puede generar este formato es <code>"0"</code>,
     * notar que el método {@link #asNumber(BigDecimal)} puede retornar
     * <code>""</code> si se le pasa como parámetro <code>null</code>.
     * </p>
     */
    public static final String LONG_NUMBER_FORMAT = NUMBER_FORMAT;

    /**
     * Formato para las monedas. Se compone de una cantidad variable de numeros
     * a la izquierda, y a lo sumo dos a la derecha.
     */
    public static final String MONEY_FORMAT = "###,###,###,###,###,###,###,###,###,###,##0";

    private Map<String, SimpleDateFormat> formats;

    private boolean initialized = false;

    private DecimalFormatSymbols dfs;

    private synchronized void init() {

        if (this.initialized) {
            return;
        }

        this.initialized = true;
        this.formats = new HashMap<String, SimpleDateFormat>();
        this.formats.put(DATE_FORMAT, new SimpleDateFormat(DATE_FORMAT));
        this.formats.put(DATE_SHORT_FORMAT, new SimpleDateFormat(
                DATE_SHORT_FORMAT));
        this.formats.put(TIME_FORMAT, new SimpleDateFormat(TIME_FORMAT));
        this.formats
                .put(DATETIME_FORMAT, new SimpleDateFormat(DATETIME_FORMAT));
        this.formats.put(DATETIME_SHORT_FORMAT, new SimpleDateFormat(
                DATETIME_SHORT_FORMAT));
        for (SimpleDateFormat sdf : this.formats.values()) {
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

        return this.dateToString(date, DATE_SHORT_FORMAT);
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

        return this.stringToDate(string, DATE_SHORT_FORMAT);
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

        return this.dateToString(date, DATE_FORMAT);

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

        return this.stringToDate(string, DATE_FORMAT);
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

        return this.dateToString(date, TIME_FORMAT);

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

        return this.stringToDate(string, TIME_FORMAT);
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

        return this.dateToString(date, DATETIME_FORMAT);
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

        return this.stringToDate(string, DATETIME_FORMAT);
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

        return this.dateToString(date, DATETIME_SHORT_FORMAT);
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

        return this.stringToDate(string, DATETIME_SHORT_FORMAT);
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
            throw new IllegalArgumentException(CANT_PARSE_NULL_QUANTITY);
        }
        return this.getNumberFormat(NUMBER_FORMAT).format(bd);

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
     * bd
     * </table>
     * </p>
     * 
     * @param bd
     *            {@link BigDecimal} a convertir.
     * @return cadena formateada, segun los ejemplos anteriores
     */
    public String asNumber(@NotNull Quantity bd) {

        if (bd == null) {
            throw new IllegalArgumentException(CANT_PARSE_NULL_QUANTITY);
        }
        return this.getNumberFormat(NUMBER_FORMAT).format(bd);

    }

    public Quantity parseQuantity(String number) throws ParseException {

        if (number == null || EMPTY_STRING.equals(number)) {
            return null;
        }
        return new Quantity(getNumberFormat(NUMBER_FORMAT).parse(number)
                .doubleValue());
    }

    public String asLongNumber(@NotNull Quantity bd) {

        if (bd == null) {
            throw new IllegalArgumentException(CANT_PARSE_NULL_QUANTITY);
        }
        return this.getNumberFormat(LONG_NUMBER_FORMAT).format(bd);

    }

    public Quantity parseLongQuantity(String number) throws ParseException {

        if (number == null || EMPTY_STRING.equals(number)) {
            return null;
        }

        return new Quantity(this.getNumberFormat(LONG_NUMBER_FORMAT)
                .parse(number).toString());
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
        return this.getFormat(format).format(date);
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
        SimpleDateFormat sdf = this.getFormat(format);
        ParsePosition psp = new ParsePosition(0);
        if (string.length() != format.length()) {
            this.throwException(string, format);
        }
        Date toRet = sdf.parse(string, psp);
        if (psp.getIndex() != string.length()) {
            this.throwException(string, format);
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

        if (!this.initialized) {
            this.init();
        }
        if (!this.formats.containsKey(format)) {
            throw new IllegalArgumentException("Unknow format " + format);
        }
        return this.formats.get(format);
    }

    private NumberFormat getNumberFormat(String format) {

        DecimalFormat df = new DecimalFormat(format);
        df.setDecimalFormatSymbols(this.getDFS());
        return df;
    }

    /**
     * @return
     */
    private DecimalFormatSymbols getDFS() {

        if (this.dfs == null) {
            this.dfs = new DecimalFormatSymbols(Locale.getDefault());
            this.dfs.setDecimalSeparator(',');
            this.dfs.setGroupingSeparator('.');
        }

        return this.dfs;
    }

    /**
     * Verifica si una fecha tiene el formato correspondiente a
     * {@link #DATE_FORMAT}.
     * 
     * @param date
     * @return
     */
    public boolean isDate(String date) {

        if (date == null) {
            return false;
        }
        try {
            this.parseDate(date);
            return true;
        } catch (ParseException pe) {
            LOG.trace("Can' parse date:", pe);
            return false;
        }
    }

    public String getDateFormat() {

        return DATE_FORMAT;
    }

    public String getDateTimeFormat() {

        return DATETIME_FORMAT;
    }

    public String getTimeFormat() {

        return TIME_FORMAT;
    }

    /**
     * Formato de las monedas.
     * 
     * @see #MONEY_FORMAT
     * @return {@value #MONEY_FORMAT}
     */
    public String getMoneyFormat() {

        return MONEY_FORMAT;
    }
}
