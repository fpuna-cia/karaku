package py.una.med.base.util;

/**
 * Clase que contiene cadenas constantes de expresiones regulares que se pueden
 * utilizar para realizar validaciones en atributos de la capa de dominio. En el
 * parámetro <code>regex</code> de la anotación <code>@Pattern</code> por
 * ejemplo. <br>
 * Las constantes que incluyen caracteres utilizados por el idioma Guaraní,
 * contienen el prefijo <code>GN_</code> y los mismos incluyen también
 * caracteres utilizados también por el idioma castellano
 * 
 * @author Héctor Martínez
 * @since 1.0
 * @version 1.0 Feb 28, 2013
 * 
 */
public final class ValidationConstants {

	private static final String SPANISH_LETTERS = "[a-zA-ZñÑ";
	private static final String SPANISH_LETTERS_WITH_SINGLE_QUOTE = "[a-zA-Z'ñÑ";
	private static final String END = " ]*";

	private ValidationConstants() {

		// No-op
	}

	private static final String LC_GRAVE = "áéíóú";
	private static final String UC_GRAVE = "ÁÉÍÓÚ";
	private static final String GRAVE = LC_GRAVE + UC_GRAVE;

	private static final String LC_NASAL = "ãẽĩõũỹäëïöüÿâêîôûŷ";
	private static final String UC_NASAL = "ÃẼĨÕŨỸÄËÏÖÜŸÂÊÎÔÛŶ";
	private static final String NASAL = LC_NASAL + UC_NASAL;

	private static final String SPECIAL = "\"¿?¡!\\(\\),\\.=\\+\\-\\_\\*\\$&%\\/\\\\";

	/**
	 * Expresión que admite caracteres del castellano y guaraní (incluído el ')
	 * tanto en mayúsculas o minúsculas
	 */
	public static final String GN_WORDS = SPANISH_LETTERS_WITH_SINGLE_QUOTE
			+ GRAVE + NASAL + END;
	/**
	 * Expresión que admite caracteres del castellano y guaraní (incluído el ')
	 * sólo en mayúsculas, de ahí el sufijo UC (UpperCase)
	 */
	public static final String GN_WORDS_UC = "[A-Z'Ñ" + UC_GRAVE + UC_NASAL
			+ END;
	/**
	 * Expresión que admite caracteres del castellano y guaraní (incluído el ')
	 * sólo en minúsculas, de ahí el sufijo LC (LowerCase)
	 */
	public static final String GN_WORDS_LC = "[A-Z'Ñ" + LC_GRAVE + LC_NASAL
			+ END;
	/**
	 * Expresión que admite caracteres del castellano y guaraní (incluído el ')
	 * tanto en mayúsculas o minúsculas además de caracteres especiales como por
	 * ejemplo [<code>, . + - * $ / \</code>]
	 * 
	 */
	public static final String GN_WORDS_SPE = SPANISH_LETTERS_WITH_SINGLE_QUOTE
			+ GRAVE + NASAL + SPECIAL + END;

	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas
	 */
	public static final String WORDS = SPANISH_LETTERS + GRAVE + END;

	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas
	 */
	public static final String WORDS_DOT = SPANISH_LETTERS + GRAVE + "\\."
			+ END;

	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas sin espacios
	 */
	public static final String WORD = SPANISH_LETTERS + GRAVE + "]*";
	/**
	 * Expresión que admite caracteres del castellano sólo en mayúsculas
	 */
	public static final String WORDS_UC = "[A-ZÑ" + UC_GRAVE + END;
	/**
	 * Expresión que admite caracteres del castellano sólo en minúsculas
	 */
	public static final String WORDS_LC = "[A-ZÑ" + LC_GRAVE + END;
	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas además de caracteres especiales como por ejemplo [
	 * <code>, . + - * $ / \</code>]
	 * 
	 */
	public static final String WORDS_SPE = SPANISH_LETTERS + GRAVE + SPECIAL
			+ END;
	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas (sin espacios)además de caracteres especiales como por ejemplo
	 * [ <code>, . + - * $ / \</code>]
	 * 
	 */
	public static final String WORD_SPE = SPANISH_LETTERS + GRAVE + SPECIAL
			+ "]*";

	/**
	 * Expresión que admite una cantidad arbitraria de dígitos
	 */
	public static final String DIGITS = "[0-9]*";

	/**
	 * Expresión que admite una cadena arbitraria de dígitos y guiones medios
	 */
	public static final String DIGITS_HYPHEN = "[0-9\\-]*";

	/**
	 * Expresión que admite una cadena arbitraria de dígitos separados por punto
	 * que no puede comenzar ni terminar con punto
	 */
	public static final String DIGITS_POINT = "([0-9]+(\\.[0-9]+)+[0-9]+)?";

	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas además de dígitos
	 */
	public static final String ALPHANUMERIC = "[a-zA-Z0-9ñÑ" + GRAVE + END;

	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas además de dígitos, espacio en blanco y guiones medios.
	 */
	public static final String ALPHANUMERIC_HYPHEN = "[a-zA-Z0-9ñÑ" + GRAVE
			+ " \\-]*";

	/**
	 * Expresión que admite caracteres del castellano tanto en mayúsculas o
	 * minúsculas además de dígitos y caracteres especiales como por ejemplo [
	 * <code>, . + - * $ / \</code>]
	 * 
	 */
	public static final String ALPHANUMERIC_SPE = "[a-zA-Z0-9ñÑ" + GRAVE
			+ SPECIAL + END;

	/**
	 * Expresión que admite caracteres del castellano en mayúsculas además de
	 * dígitos
	 */
	public static final String ALPHANUMERIC_UC = "[A-Z0-9Ñ" + UC_GRAVE + END;

	/**
	 * Expresión que admite caracteres del castellano en mayúsculas además de
	 * dígitos y caracteres especiales como por ejemplo [
	 * <code>, . + - * $ / \</code>]
	 */
	public static final String ALPHANUMERIC_UC_SPE = "[A-Z0-9Ñ" + UC_GRAVE
			+ SPECIAL + END;

	public static final String GN_WORDS_SPE_DIG = "[a-zA-Z0-9'ñÑ" + GRAVE
			+ NASAL + SPECIAL + END;

	/**
	 * Expresión que admite caracteres del castellano y guaraní tanto en
	 * mayúsculas o minúsculas además de dígitos y caracteres especiales como
	 * por ejemplo [ <code>, . + - * $ / \</code>]
	 * 
	 */
	public static final String GN_ALPHANUMERIC_SPE = "[a-zA-Z'0-9ñÑ" + GRAVE
			+ SPECIAL + NASAL + END;

	/**
	 * Expresión regular para validar cadenas que representan URL's, en su forma
	 * más general. <br />
	 * <b>Protocolos soportados</b>, todos son soportados tanto en mayúsculas
	 * como en minúsculas.
	 * <ol>
	 * <li>http, https</li>
	 * <li>ftp</li>
	 * <li>file</li>
	 * </ol>
	 */
	public static final String URL = "^(https?|ftp|file|HTTPS?|FTP|FILE)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
}
