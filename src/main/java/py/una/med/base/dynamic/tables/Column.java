/*
 * @Column.java 1.0 Mar 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.tables;

import javax.faces.component.UIComponent;
import org.richfaces.component.UIColumn;
import py.una.med.base.dynamic.forms.SIGHComponentFactory;
import py.una.med.base.util.ELHelper;

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
public abstract class Column {

	public static final String defaultValueExpression = "#{item.ATTRIBUTE}";
	public static final String defaultI8NExpression = "#{msg['KEY']}";
	UIColumn bind;
	UIComponent body;
	UIComponent header;
	ELHelper elHelper;
	boolean builded;

	public Column() {

		bind = SIGHComponentFactory.getRichColumn();
		elHelper = ELHelper.INSTANCE;
	}

	public void setStyleClass(final String newClass) {

		bind.setStyleClass(newClass);
	}

	/**
	 * Retorna una cadena que se una expression de EL, esta cadena sirve para
	 * poder internacionalizar la llave pasada como parámetro, la cadena
	 * resultado tiene el formato #{msg['KEY']} donde KEY es el parámetro
	 * pasado.
	 * 
	 * @param key
	 *            coligo del archivo de internacionalizasen
	 * @return valor del archivo de internacionalizasen
	 */
	public static String getI18nStringExpression(final String key) {

		return defaultI8NExpression.replaceFirst("KEY", key);
	}

	/**
	 * Retorna una cadena que es una expression del lenguaje que sirve para
	 * acceder a atributos de la variable de la {@link DataTable}, la cadena
	 * retornada por este metido tiene el formato #{item.ATTRIBUTE}, donde
	 * ATTRIBUTE es la cadena pasada como parametro
	 * 
	 * @param attribute
	 * @return
	 */
	public static String getAttributeStringExpression(final String attribute) {

		return defaultValueExpression.replaceFirst("ATTRIBUTE", attribute);
	}

	/**
	 * @return bind
	 */
	public UIColumn getBind() {

		return bind;
	}

	/**
	 * Metodo de acceso a una columna, en este metodo se realiza la union de la
	 * cabecera (como facet) y el cuerpo a una columna.
	 * 
	 * @return
	 */
	public final UIColumn getColumn() {

		if (builded)
			return bind;
		builded = true;
		bind.getFacets().put("header", getHeader());
		if (hasBoddy()) {
			bind.getChildren().clear();
			bind.getChildren().add(getBody());
		}
		return bind;
	}

	/**
	 * Método en el que se construye la columna, sucesivas clases deben heredar
	 * este método y agregar código de inicialización
	 */
	public void build() {

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
