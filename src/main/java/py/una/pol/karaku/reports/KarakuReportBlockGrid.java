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
package py.una.pol.karaku.reports;

import java.util.ArrayList;
import java.util.List;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.util.ListHelper;

public final class KarakuReportBlockGrid extends KarakuReportBlock {

	private List<Column> columns;

	public KarakuReportBlockGrid(String title, String nameDataSource,
			List<Column> columns, List<?> listElement) {

		super(title, nameDataSource);
		setDataSource(buildDataSource(columns, listElement));

	}

	public JRDataSource buildDataSource(List<Column> columns,
			List<?> listElement) {

		this.columns = columns;

		if (listElement.isEmpty()) {
			return new DRDataSource();
		}

		Object first = listElement.get(0);

		if (first instanceof BaseEntity) {
			return new JRBeanCollectionDataSource(listElement);

		}

		List<String> columnsDataSource = new ArrayList<String>();
		for (Column column : columns) {
			columnsDataSource.add(column.getField());
		}

		DRDataSource ds = new DRDataSource(
				ListHelper.asArray(columnsDataSource));
		for (Object o : listElement) {
			ds.add((Object[]) o);
		}

		return ds;
	}

	public List<Column> getColumns() {

		return columns;
	}

	public void setColumns(List<Column> columns) {

		this.columns = columns;
	}
}
