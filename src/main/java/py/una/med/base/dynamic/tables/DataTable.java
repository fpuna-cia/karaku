/*
 * @DataTable.java 1.0 Mar 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.tables;

import java.io.Serializable;
import java.util.List;
import org.richfaces.component.UIExtendedDataTable;
import py.una.med.base.dynamic.forms.SIGHComponentFactory;
import py.una.med.base.util.SIGHListHelper;
import py.una.med.base.util.StringUtils;

/**
 * Clase que representa una tabla <br>
 * </br>TODO:
 * <ol>
 * <li>Agregar configuración de span</li>
 * </ol>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 11, 2013
 * 
 */
/*
 * <rich:extendedDataTable value="#{cc.attrs.items}" var="item"
 * id="#{cc.attrs.idDataTable}" rows="15" selectionMode="single"
 * style="height:#{cc.attrs.height}; width:#{cc.attrs.width}"
 * selection="#{extTableSelectionBean.selection}">
 */
public class DataTable<T, ID extends Serializable> {

	/**
	 * 
	 */
	public static final String DATA_TABLE_VARIABLE = "item";
	private static final String DEFAULT_STYLE = "height:HEIGHTpx; width:WIDTHpx";
	private static final int DEFAULT_WIDTH = 350;
	private static final int DEFAULT_HEIGH = 620;

	private SIGHListHelper<T, ID> listHelper;
	private boolean idAdded;
	private boolean actionsAdded;
	private boolean exportsAdded;
	private List<Column> columns;
	private int width = DEFAULT_WIDTH;
	private int heigh = DEFAULT_HEIGH;
	private UIExtendedDataTable table;
	private boolean withExports = false;
	private boolean withActions = true;

	/**
	 * 
	 */
	public DataTable() {

		table = SIGHComponentFactory.getDataTable();
		table.setStyle(getCurrentStyle());
		table.setVar(DATA_TABLE_VARIABLE);
	}

	private String getCurrentStyle() {

		String cStyle = DEFAULT_STYLE.replaceFirst("HEIGHT", heigh + "");
		cStyle = DEFAULT_STYLE.replaceFirst("WIDTH", width + "");
		return cStyle;
	}

	/**
	 * @return columns
	 */
	public List<Column> getColumns() {

		return columns;
	}

	/**
	 * @param columns
	 *            columns para setear
	 */
	public void setColumns(final List<Column> columns) {

		idAdded = false;
		this.columns = columns;
	}

	/**
	 * Asigna la tabla actual a una lista de clases CSS
	 * 
	 * @param clases
	 *            List of CSS style class(es) to be applied when this element is
	 *            rendered. This value must be passed through as the "class"
	 *            attribute on generated markup.
	 */
	public void setStyleClass(final String ... clases) {

		table.setStyleClass(StringUtils.join(" ", clases));
	}

	/**
	 * Asigna un conjunto de clases CSS a la tabla, por fila, esto significa que
	 * la primera fila tendrá la primera clase pasada como parámetro, la
	 * siguiente fila tendrá la siguiente clase. <br>
	 * </br>Notar que este proceso es cíclico, en otras palabras, si se pasan
	 * dos clases, la tercera fila tendrá el primer estilo y la cuarta el
	 * segundo (hasta N).
	 * 
	 * @param clases
	 *            Assigns one or more CSS class names to the rows of the table.
	 *            If the CSS class names are comma-separated, each class will be
	 *            assigned to a particular row in the order they follow in the
	 *            attribute. If you have less class names than rows, the class
	 *            will be applied to every n-fold row where n is the order in
	 *            which the class is listed in the attribute. If there are more
	 *            class names than rows, the overflow ones are ignored.
	 */
	public void setRowClass(final String ... clases) {

		table.setRowClasses(StringUtils.join(" ", clases));
	}
}
