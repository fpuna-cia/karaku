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

/**
 * Clase que implementa funciones de parseo para las expresiones EL manejadas
 * por el sistema, las cuales pueden ser de dos tipos <br>
 * <ol>
 * <li><b>Parámetros del método:</b> cuando se desea que la expression
 * corresponda a un parámetro del método, para obtener el numero de parámetro
 * utilizar {@link ELParser#getParamNumber(String)} y para obtener la expresión
 * {@link ELParser#removeParamNumber(String)}.
 *
 * <b>Ejemplo</b>:
 *
 * <pre>
 * 	{1}.id
 * </pre>
 *
 * <p>
 * Los parámetros empiezan con 1
 * </p>
 * <li><b>Atributos de la clase:</b> cuando se desea que la expresión
 * corresponda al objeto donde se encuentra la anotación
 * </ol>
 *
 * @author Arturo Volpe
 * @author Nathalia Ochoa
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
					expression.indexOf('}')));
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Los parametros deben tener formato {NUMBER}", e);
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
	 * Verifica si una expresión pasada cumple con el formato del sistema para
	 * expresiones de parámetros de métodos, las cuales son del tipo {0}.id
	 *
	 * @param string
	 *            expresión original
	 * @return <code>true</code> si es una expresión de método y
	 *         <code>false</code> en otro caso
	 */
	public static boolean isMethodParamExpression(String string) {

		return string.startsWith(START_WITH);
	}

	/**
	 * Dada una expresión, retorna el field correspondiente a dicha columna<br>
	 * <b>Por ejemplo</b><br>
	 * <ol>
	 * <li><b>#{item.descripcion}</b>, retorna description
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
		if ("item".equals(format)) {
			columnName = columnName.substring(5, columnName.length());
			return columnName;
		}

		return null;
	}

	/**
	 * Dada una expresión, retorna el header correspondiente a dicha columna<br>
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
