package py.una.med.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Provee funcionalidades basicas para todas las cadenas del sistema
 * 
 * @author Arturo Volpe, Nathalia Ochoa
 * @since 1.0
 * @version 1.1 08/02/2013
 * 
 */
public class StringUtils {

	private static List<Character> Vocales = Arrays.asList('a', 'e', 'i', 'o',
			'u', 'A', 'E', 'I', 'O', 'U');
	private static List<Character> VocalesFuertesTonicas = Arrays.asList('á',
			'é', 'ó');
	private static List<Character> VocalesDebilesTonicas = Arrays.asList('í',
			'ú');
	private static List<Character> ConsonantesEspeciales = Arrays.asList('d',
			'j', 'l', 'n', 'r', 'D', 'J', 'L', 'N', 'R');
	private static List<Character> Consonantes = Arrays.asList('b', 'c', 'd',
			'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't',
			'v', 'w', 'x', 'y', 'z', 'B', 'C', 'D', 'F', 'G', 'H', 'J', 'K',
			'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z');
	private static List<Character> Mayus = Arrays.asList('A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');

	public static boolean isValid(final String string) {

		if (string == null)
			return false;
		if ("".equals(string.trim()))
			return false;
		return true;
	}

	public static boolean isInvalid(final String string) {

		return !isValid(string);
	}

	public static String join(final String separator, final String ... strings) {

		return join(separator, 0, strings.length - 1, strings);
	}

	public static String join(final String separator,
			final Collection<String> strings) {

		return join(separator, 0, strings.size() - 1,
				strings.toArray(new String[strings.size()]));
	}

	public static String join(final String separator, final int start,
			final int end, final String ... strings) {

		if (strings == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = start; i <= end; i++) {
			sb.append(strings[i]);
			if (i < end) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	/**
	 * Dada una cadena escrita en camelCase retorna una lista con los elementos
	 * que forman la cadena<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>HolaMundo</b>, retorna [Hola,Mundo]
	 * </ol>
	 * 
	 * @param string
	 * @return list de elementos
	 */
	public static List<String> split(final String strings) {

		List<String> list = new ArrayList<String>();
		int j = 0;
		for (int i = 1; i < strings.length(); i++) {
			if (Mayus.contains(strings.charAt(i))) {
				list.add(strings.substring(j, i));
				j = i;
			}
		}
		list.add(strings.substring(j, strings.length()));
		return list;
	}

	/**
	 * Dada una cadena retorna el plural de la misma<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>Lapiz</b>, retorna Lapices
	 * <li><b>Pais</b>, retorna Paises
	 * </ol>
	 * 
	 * @param singular
	 * @return plural
	 */
	public static String pluralize(final String singular) {

		String plural = "";
		Character terminate = singular.charAt(singular.length() - 1);
		if (Vocales.contains(terminate)
				|| VocalesFuertesTonicas.contains(terminate)) {
			plural = singular.concat("s");
			return plural;
		}
		if (VocalesDebilesTonicas.contains(terminate)) {
			plural = singular.concat("es");
			return plural;
		}
		if (ConsonantesEspeciales.contains(terminate)) {
			if (!Consonantes.contains(singular.charAt(singular.length() - 2))) {
				plural = singular.concat("es");
				return plural;
			} else
				return singular;
		}
		if (singular.endsWith("s") || singular.endsWith("S")
				|| singular.endsWith("x") || singular.endsWith("X")) {
			if ((singular.length() <= 4) || (singular.length() > 8)) {
				plural = singular.concat("es");
				return plural;
			} else
				return singular;
		}
		if (singular.endsWith("y") || singular.endsWith("Y")) {
			if (Vocales.contains(singular.charAt(singular.length() - 2))) {
				plural = singular.concat("es");
				return plural;
			} else {
				plural = singular.concat("s");
				return plural;
			}
		}
		if (singular.endsWith("z") || singular.endsWith("Z")) {
			plural = singular.substring(0, singular.length() - 1).concat("ces");
			return plural;
		}
		if (!ConsonantesEspeciales.contains(terminate)) {
			plural = singular.concat("s");
			return plural;
		}
		return null;
	}

	public static String pluralize(final List<String> terms) {

		StringBuffer buf = new StringBuffer();
		for (String term : terms) {
			buf.append(pluralize(term) + " ");
		}
		return buf.toString();
	}

}
