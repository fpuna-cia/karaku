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

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Provee funcionalidades básicas para todas las cadenas del sistema
 * 
 * @author Arturo Volpe
 * @author Nathalia Ochoa
 * 
 * @since 1.0
 * @version 1.1 08/02/2013
 * 
 */
public final class StringUtils {

	private static final List<Character> VOCALES = Arrays.asList('a', 'e', 'i',
			'o', 'u', 'A', 'E', 'I', 'O', 'U');
	private static final List<Character> VOCALES_FUERTES_TONICAS = Arrays
			.asList('á', 'é', 'ó');
	private static final List<Character> VOCALES_DEBILES_TONICAS = Arrays
			.asList('í', 'ú');
	private static final List<Character> CONSONANTES_ESPECIALES = Arrays
			.asList('d', 'j', 'l', 'n', 'r', 'D', 'J', 'L', 'N', 'R');
	private static final List<Character> CONSONANTES = Arrays.asList('b', 'c',
			'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's',
			't', 'v', 'w', 'x', 'y', 'z', 'B', 'C', 'D', 'F', 'G', 'H', 'J',
			'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y',
			'Z');
	private static final List<Character> MAYUSCULAS = Arrays.asList('A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');

	private StringUtils() {

		// No-op
	}

	/**
	 * Valida si una cadena es o no válida (tomando en cuenta cadenas recibidas
	 * desde la interfaz), una cadena es válidada si no es <code>null</code> y
	 * si tiene al menos un carácter distinto de un espacio
	 * 
	 * @param string
	 *            a validar
	 * @return <code>true</code> si es válida, y <code>false</code> en caso
	 *         contrario
	 */
	public static boolean isValid(final String string) {

		if (string == null) {
			return false;
		}
		if ("".equals(string.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * Valida si una cadena es o no válida (tomando en cuenta cadenas recibidas
	 * desde la interfaz), una cadena es válidada si no es <code>null</code> y
	 * si tiene al menos un carácter distinto de un espacio
	 * 
	 * @param object
	 *            a validar
	 * @return <code>true</code> si es válida, y <code>false</code> en caso
	 *         contrario
	 */
	public static boolean isValid(final Object object) {

		if (object == null) {
			return false;
		}
		if (object instanceof String) {
			return isValid((String) object);
		}
		return true;
	}

	/**
	 * Invoca al método {@link #isValid(String)} por cada cadena del vector y
	 * retorna <code>false</code> si alguna de las llamadas retorna
	 * <code>false</code>
	 * 
	 * @see #isInvalid(String)
	 * @param strings
	 *            cadenas a validar
	 * @return <code>true</code> si todas las cadenas son válidas y
	 *         <code>false</code> si alguna no es válida o el vector es
	 *         <code>null</code> o vacío
	 */
	public static boolean isValid(String ... strings) {

		if (strings == null || strings.length == 0) {
			return false;
		}

		for (String s : strings) {
			if (!isValid(s)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Valida si una cadena es o no inválida (tomando en cuenta cadenas
	 * recibidas desde la interfaz), una cadena es válida si no es
	 * <code>null</code> y si tiene al menos un carácter distinto de un espacio
	 * 
	 * @param string
	 *            a validar
	 * @return <code>true</code> si es válida, y <code>false</code> en caso
	 *         contrario
	 */
	public static boolean isInvalid(final String string) {

		return !isValid(string);
	}

	/**
	 * Une un grupo de palabras con un separador.
	 * 
	 * @param separator
	 *            cadena que servira de pegamento entre las demás cadenas
	 * @param strings
	 *            lista de palabras a unir
	 * @return una simple cadena compuesta de las demás cadenas separadas por un
	 *         separador. <code>""</code> si no hay ninguna cadena.
	 */
	public static String join(final String separator, final String ... strings) {

		return join(separator, false, strings);
	}

	/**
	 * Une un grupo de palabras con un separador.
	 * 
	 * @param separator
	 *            cadena que servira de pegamento entre las demás cadenas
	 * @param strings
	 *            lista de palabras a unir
	 * @return una simple cadena compuesta de las demás cadenas separadas por un
	 *         separador. <code>""</code> si no hay ninguna cadena.
	 */
	public static String join(final String separator, boolean skipInvaild,
			final String ... strings) {

		if (!checkParams(separator, strings)) {
			return "";
		}
		return join(separator, 0, strings.length - 1, skipInvaild, strings);
	}

	/**
	 * Une un grupo de palabras con un separador.
	 * 
	 * @param separator
	 *            cadena que servirá de pegamento entre las demas cadenas
	 * @param strings
	 *            lista de palabras a unir
	 * @return una simple cadena compuesta de las demás cadenas separadas por un
	 *         separador. <code>""</code> si no hay ninguna cadena.
	 */
	public static String join(final String separator,
			final Collection<String> strings) {

		if (!checkParams(separator, strings)) {
			return "";
		}
		return join(separator, 0, strings.size() - 1,
				strings.toArray(new String[strings.size()]));
	}

	/**
	 * Une un grupo de palabras con un separador.
	 * 
	 * @param separator
	 *            cadena que servirá de pegamento entre las demas cadenas
	 * @param strings
	 *            lista de palabras a unir
	 * @param start
	 *            primer elemento a unir
	 * @param end
	 *            ultimo elemento a unir
	 * @return una simple cadena compuesta de las demás cadenas separadas por un
	 *         separador. <code>""</code> si no hay ninguna cadena.
	 */
	public static String join(final String separator, final int start,
			final int end, final String ... strings) {

		return join(separator, start, end, false, strings);
	}

	/**
	 * Une un grupo de palabras con un separador.
	 * 
	 * @param separator
	 *            cadena que servirá de pegamento entre las demas cadenas
	 * @param strings
	 *            lista de palabras a unir
	 * @param start
	 *            primer elemento a unir
	 * @param end
	 *            ultimo elemento a unir
	 * @param skipInvalid
	 *            permite omitir las cadenas nulas en la lista de cadenas
	 * @return una simple cadena compuesta de las demás cadenas separadas por un
	 *         separador. <code>""</code> si no hay ninguna cadena.
	 */
	public static String join(final String separator, final int start,
			final int end, boolean skipInvalid, final String ... strings) {

		checkParams(separator, strings);

		if (strings.length == 1) {
			return strings[0];
		}
		int begin = Math.max(start, 0);
		int to = Math.min(end, strings.length - 1);
		StringBuilder sb = new StringBuilder();
		for (int i = begin; i <= to; i++) {
			if (skipInvalid && isInvalid(strings[i])) {
				continue;
			}
			sb.append(strings[i]);
			if (i < to) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * @param separator
	 * @param strings
	 */
	private static boolean checkParams(final String separator,
			final String ... strings) {

		notNull(separator, "Separator can't be null");
		if (ArrayUtils.isEmpty(strings)) {
			return false;
		}
		return true;
	}

	/**
	 * @param separator
	 * @param strings
	 */
	private static boolean checkParams(final String separator,
			final Collection<String> strings) {

		notNull(separator, "Separator can't be null");
		if (!ListHelper.hasElements(strings)) {
			return false;
		}
		return true;
	}

	/**
	 * Dada una cadena escrita en camelCase retorna una lista con los elementos
	 * que forman la cadena
	 * <p>
	 * <b>Por ejemplo</b>
	 * <ol>
	 * <li><b>HolaMundo</b>, retorna [Hola,Mundo]</li>
	 * </ol>
	 * 
	 * </p>
	 * 
	 * @param string
	 *            cadena a partir
	 * @return list de elementos
	 */
	public static List<String> split(final String string) {

		List<String> list = new ArrayList<String>();
		int j = 0;
		for (int i = 1; i < string.length(); i++) {
			if (MAYUSCULAS.contains(string.charAt(i))) {
				list.add(string.substring(j, i));
				j = i;
			}
		}
		list.add(string.substring(j, string.length()));
		return list;
	}

	/**
	 * Dada una cadena retorna el plural de la misma
	 * <p>
	 * <b>Por ejemplo</b>
	 * <ol>
	 * <li><b>Lapiz</b>, retorna Lapices</li>
	 * <li><b>Pais</b>, retorna Paises</li>
	 * </ol>
	 * </p>
	 * 
	 * @param singular
	 *            cadena en singular
	 * @return plural cadena pluralizada
	 */
	public static String pluralize(final String singular) {

		String plural = "";
		Character terminate = singular.charAt(singular.length() - 1);
		if (VOCALES.contains(terminate)
				|| VOCALES_FUERTES_TONICAS.contains(terminate)) {
			plural = singular.concat("s");
			return plural;
		}
		if (VOCALES_DEBILES_TONICAS.contains(terminate)) {
			plural = singular.concat("es");
			return plural;
		}
		return terminaConConsonante(singular, terminate);
	}

	/**
	 * @param singular
	 * @param terminate
	 * @return
	 */
	private static String terminaConConsonante(final String singular,
			Character terminate) {

		String plural;
		if (CONSONANTES_ESPECIALES.contains(terminate)) {
			if (!CONSONANTES.contains(singular.charAt(singular.length() - 2))) {
				plural = singular.concat("es");
				return plural;
			} else {
				return singular;
			}
		}
		if (is(terminate, 's', 'S')) {
			plural = singular.concat("es");
			return plural;
		}
		if (is(terminate, 'y', 'Y')) {
			if (VOCALES.contains(singular.charAt(singular.length() - 2))) {
				plural = singular.concat("es");
				return plural;
			} else {
				plural = singular.concat("s");
				return plural;
			}
		}
		if (is(terminate, 'z', 'Z')) {
			plural = singular.substring(0, singular.length() - 1).concat("ces");
			return plural;
		}
		if (!CONSONANTES_ESPECIALES.contains(terminate)) {
			plural = singular.concat("s");
			return plural;
		}
		return null;
	}

	private static boolean is(char first, char ... c) {

		if (c == null || c.length < 1) {
			return true;
		}
		if (c.length < 1) {
			return false;
		}
		for (char element : c) {

			if (first == element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Pluraliza una serie de palabras separandolas por un espacio.
	 * 
	 * @param terms
	 *            grupo de palabras a pluralizar
	 * @return <code>null</code> si no hay terms o terms es <code>null</code>,
	 *         en otro caso una cadena formada por las cadenas pasadas separadas
	 *         por un espacio.
	 * @see #pluralize(String, Collection)
	 */
	public static String pluralize(final Collection<String> terms) {

		if (terms == null || terms.isEmpty()) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		String[] array = terms.toArray(new String[terms.size()]);
		for (int i = 0; i < terms.size(); i++) {
			buf.append(pluralize(array[i]));
			if (i + 1 != terms.size()) {
				buf.append(" ");
			}
		}
		return buf.toString();
	}

	/**
	 * Pluraliza una serie de palabras separandolas por un token separador.
	 * 
	 * @param terms
	 *            grupo de palabras a pluralizar
	 * @param separator
	 *            separador de palabras
	 * @return <code>null</code> si no hay terms o terms es <code>null</code>,
	 *         en otro caso una cadena formada por las cadenas pasadas separadas
	 *         por un separator.
	 */
	public static String pluralize(String separator,
			final Collection<String> terms) {

		if (terms == null || terms.isEmpty()) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		String[] array = terms.toArray(new String[terms.size()]);
		for (int i = 0; i < terms.size(); i++) {
			buf.append(pluralize(array[i]));
			if (i + 1 != terms.size()) {
				buf.append(separator);
			}
		}
		return buf.toString();
	}

}
