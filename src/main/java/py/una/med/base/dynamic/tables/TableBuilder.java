package py.una.med.base.dynamic.tables;

import org.richfaces.model.SelectionMode;
import py.una.med.base.dynamic.data.AbstractPagingDataSource;

public class TableBuilder {

	private final DataTable table;

	private TableBuilder(DataTable currentTable) {

		this.table = currentTable;
	}

	public static TableBuilder createNewTableBuilder() {

		return new TableBuilder(new DataTable());
	}

	public TableBuilder addColumn(String headerText, String property) {

		SimpleColumn column = new SimpleColumn();
		column.setHeaderText(headerText);
		column.bindAttribute(property, Object.class);
		table.addColumn(column);
		return this;
	}

	public DataTable build() {

		table.build();
		return table;
	}

	public TableBuilder setDataSource(AbstractPagingDataSource helper) {

		table.setPagingDataSource(helper);
		return this;
	}

	/**
	 * Actualmente solo se soporta {@link SelectionMode#single} y
	 * {@link SelectionMode#none}
	 * 
	 * @param mode
	 * @return this
	 */
	public TableBuilder addSelectionSupport(SelectionMode mode) {

		table.getBind().setSelectionMode(mode);
		return this;
	}
}
