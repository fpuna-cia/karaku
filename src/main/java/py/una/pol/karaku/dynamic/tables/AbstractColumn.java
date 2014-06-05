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
package py.una.pol.karaku.dynamic.tables;

import javax.faces.component.UIComponent;
import org.richfaces.component.UIColumn;
import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;
import py.una.pol.karaku.util.ELHelper;

/**
 * Clase que representa una columna, por defecto trata con una cabecera y un
 * cuerpo {@link HtmlOutputText} a los cuales se les puede asignar valores.
 * 
 * <br>
 * </br>TODO:
 * <ol>
 * <li>Agregar posibilidad de imágenes</li>
 * <li>Agregar label providers</li>
 * <li>Agregar Colspan</li>
 * <li>Agregar Rowspan</li>
 * <li>Agregar Varios tipos de body</li>
 * <li>Agregar Varios tipos de header</li>
 * </ol>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 11, 2013
 * 
 */
public abstract class AbstractColumn {

	public static final String DEFAULT_VALUE_EXPRESSION = "#{item.ATTRIBUTE}";
	public static final String DEFAULT_I18N_EXPRESSION = "#{msg['KEY']}";
	private final UIColumn bind;
	private final ELHelper elHelper;
	private boolean builded;

	private static int idCount = 0;

	/**
	 * Retorna un id único para ser utilizado por un componente.
	 * 
	 * @return Cadena única para la columna
	 */
	protected static String getNextId() {

		return AbstractColumn.class.getSimpleName() + ++idCount;
	}

	public AbstractColumn() {

		bind = KarakuComponentFactory.getRichColumn();
		bind.setId(getNextId());
		elHelper = ELHelper.INSTANCE;
	}

	public void setStyleClass(final String newClass) {

		bind.setStyleClass(newClass);
	}

	/**
	 * Retorna una cadena EL que sirve para acceder a atributos de la variable
	 * de la {@link DataTable}, la cadena retornada por este metido tiene el
	 * formato #{item.ATTRIBUTE}, donde ATTRIBUTE es la cadena pasada como
	 * parametro
	 * 
	 * @param attribute
	 * @return
	 */
	public static String getAttributeStringExpression(final String attribute) {

		return DEFAULT_VALUE_EXPRESSION.replaceFirst("ATTRIBUTE", attribute);
	}

	/**
	 * @return bind
	 */
	public UIColumn getBind() {

		return bind;
	}

	/**
	 * Método en el que se construye la columna, sucesivas clases deben heredar
	 * este método y agregar código de inicialización
	 */
	public void build() {

		if (builded) {
			return;
		}
		builded = true;
		bind.getFacets().put("header", getHeader());
		if (hasBoddy()) {
			bind.getChildren().add(getBody());
		}
	}

	/**
	 * Retorna true si tiene un hijo en particular, o es una colección de hijos
	 * que son hermanos, para este caso agregarlos directamente al
	 * {@link #getBind()}
	 * 
	 * @return true si tiene hijos, false en otro caso
	 */
	protected boolean hasBoddy() {

		return true;
	}

	public abstract UIComponent getBody();

	public abstract UIComponent getHeader();

	public ELHelper getHelper() {

		return elHelper;
	}
}
