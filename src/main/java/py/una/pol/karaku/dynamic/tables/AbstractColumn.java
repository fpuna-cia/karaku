/*
 * @Column.java 1.0 Mar 11, 2013 Sistema Integral de Gestion Hospitalaria
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
