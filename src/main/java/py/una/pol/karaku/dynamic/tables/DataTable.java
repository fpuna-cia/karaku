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

import java.util.ArrayList;
import java.util.List;
import org.richfaces.component.UIExtendedDataTable;
import org.richfaces.model.SelectionMode;
import py.una.pol.karaku.dynamic.data.AbstractPagingDataSource;
import py.una.pol.karaku.dynamic.forms.Field;
import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;
import py.una.pol.karaku.util.StringUtils;

/**
 * Clase que representa una tabla, se asume durante la construcción de este
 * elemento, que la variable para referenciar al bind es 'table' <br>
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
public class DataTable extends Field {

	/**
	 * 
	 */
	public static final String DATA_TABLE_VARIABLE = "item";

	private List<AbstractColumn> columns;
	private final UIExtendedDataTable table;

	private AbstractPagingDataSource dataSource;

	/**
	 * 
	 */
	public DataTable() {

		table = KarakuComponentFactory.getDataTable();
		table.setVar(DATA_TABLE_VARIABLE);
	}

	/**
	 * @return columns
	 */
	public List<AbstractColumn> getColumns() {

		if (columns == null) {
			columns = new ArrayList<AbstractColumn>();
		}
		return columns;
	}

	/**
	 * @param columns
	 *            columns para setear
	 */
	public void setColumns(final List<AbstractColumn> columns) {

		this.columns = columns;
	}

	public void addColumn(AbstractColumn ... columns) {

		for (AbstractColumn column : columns) {
			getColumns().add(column);
		}
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

	private boolean builded = false;

	public void build() {

		if (builded) {
			return;
		}
		builded = true;
		for (AbstractColumn column : getColumns()) {
			column.build();
			table.getChildren().add(column.getBind());
		}
		getDataSource().refresh();
		table.setValue(getDataSource().getItems());
	}

	public UIExtendedDataTable getBind() {

		return table;
	}

	@Override
	public boolean disable() {

		return false;
	}

	@Override
	public boolean enable() {

		return false;
	}

	public SelectionMode getSelectionMode() {

		return table.getSelectionMode();
	}

	public void setSelectionMode(SelectionMode selectionMode) {

		table.setSelectionMode(selectionMode);
	}

	public void setPagingDataSource(AbstractPagingDataSource ds) {

		dataSource = ds;
	}

	public AbstractPagingDataSource getDataSource() {

		return dataSource;
	}

}
