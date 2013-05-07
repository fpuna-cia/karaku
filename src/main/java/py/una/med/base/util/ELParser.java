/**
 * @ELHelper 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.util;

/**
 * Clase que implementa funciones de parseo para las expresiones EL manejadas
 * por el sistema, las cuales pueden ser de dos tipos <br>
 * <ol>
 * <li><b>Parametros del metodo:</b> cuando se desea que la expression
 * corresponda a un parametro del metodo, para obtener el numero de parametro
 * utilizar {@link ELParser#getParamNumber(String)} y para obtener la expresion
 * {@link ELParser#removeParamNumber(String)}
 * <li><b>Atributos de la clase:</b> cuando se desea que la expresion
 * corresponda al objeto donde se encuentra la anotacion
 * </ol>
 * 
 * @author Arturo Volpe, Nathalia Ochoa
 * @since 1
 * @version 2.0 19/02/2013
 * 
 */
public final class ELParser {

	private ELParser() {

		// No-op
	}

	/**
	 * Separador que se utiliza para partir expresiones
	 */
	public static final String SEPARATOR = ".";

	private static final String START_WITH = "{";

	/**
	 * Dada una expression retorna el numero de parametro <br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>{0}.nombre.descripcion</b>, retorna 0
	 * <li><b>{4}.descripcion</b>, retorna 4
	 * </ol>
	 * 
	 * @param expression
	 *            EL
	 * @return numero de parametro
	 */
	public static Integer getParamNumber(String expression) {

		try {
			return Integer.parseInt(expression.substring(1,
					expression.indexOf("}")));
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Los parametros deben tener formato {NUMBER}");
		}
	}

	/**
	 * Dada una expression retorna la expression EL limpia <br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>{0}.nombre.descripcion</b>, retorna nombre.descripcion
	 * <li><b>{4}.descripcion</b>, retorna descripcion
	 * </ol>
	 * 
	 * @param string
	 *            EL expresion a ser manejada
	 * @return expresion EL limpia
	 */
	public static String removeParamNumber(String string) {

		int index = string.indexOf(SEPARATOR);
		if (index == -1) {
			return null;
		}

		return string.substring(index + 1);
	}

	/**
	 * Verifica si una expresion pasada cumple con el formato del sistema para
	 * expresiones de parametros de metodos, las cuales son del tipo {0}.id
	 * 
	 * @param string
	 *            expresin original
	 * @return true si es una expresin de metodo y false en otro caso
	 */
	public static boolean isMethodParamExpression(String string) {

		return string.startsWith(START_WITH);
	}

	/**
	 * Dada una expresion, retorna el field correspondiente a dicha columna<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>#{item.descripcion}</b>, retorna descripcion
	 * <li><b>#{item.pais.descripcion}</b>, retorna pais.descripcion
	 * </ol>
	 * 
	 * @param expression
	 * 
	 * @return field
	 */
	public static String getFieldByExpression(String expression) {

		String columnName = expression.substring(2, expression.length() - 1);
		String format = columnName.substring(0, 4);
		if (format.equals("item")) {
			columnName = columnName.substring(5, columnName.length());
			return columnName;
		}

		return null;
	}

	/**
	 * Dada una expresion, retorna el header correspondiente a dicha columna<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>#{msg['PAIS_DESCRIPCION']}</b>, retorna la internacionalizacion de
	 * la cadena PAIS_DESCRIPCION
	 * </ol>
	 * 
	 * @param expression
	 * 
	 * @return string internacionalizado
	 */
	public static String getHeaderColumn(String expression) {

		String columnHeader = expression.substring(7, expression.length() - 3);
		return I18nHelper.getMessage(columnHeader);
	}
}
