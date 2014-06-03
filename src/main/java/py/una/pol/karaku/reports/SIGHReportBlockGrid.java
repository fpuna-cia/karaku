package py.una.med.base.reports;

import java.util.ArrayList;
import java.util.List;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import py.una.med.base.domain.BaseEntity;
import py.una.med.base.util.ListHelper;

public final class SIGHReportBlockGrid extends SIGHReportBlock {

	private List<Column> columns;

	public SIGHReportBlockGrid(String title, String nameDataSource,
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