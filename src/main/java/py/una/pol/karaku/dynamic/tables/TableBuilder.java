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

import org.richfaces.model.SelectionMode;
import py.una.pol.karaku.dynamic.data.AbstractPagingDataSource;

public final class TableBuilder {

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
