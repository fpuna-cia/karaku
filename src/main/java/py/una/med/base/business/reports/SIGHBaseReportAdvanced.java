/**
 * @SIGHBaseReportAvanced 1.1 05/03/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.reports.Column;
import py.una.med.base.reports.ExportReport;
import py.una.med.base.util.ListHelper;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;

/**
 * Clase que implementa las funcionalidades basicas necesarias para generar
 * reportes complejos
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 12/03/2013
 * 
 */
public abstract class SIGHBaseReportAdvanced<T> implements
		ISIGHBaseReportAdvanced<T> {

	@Override
	public abstract ISIGHBaseLogic<T, ?> getBaseLogic();

	@Override
	public DRDataSource getStructDataSource() {

		DRDataSource dataSource = new DRDataSource(
				ListHelper.asArray(getColumnsDataSource()));
		return dataSource;
	}

	@Override
	public DRDataSource getDataSource(HashMap<String, Object> listFilters,
			List<String> listOrder) {

		DRDataSource dataSource = getStructDataSource();

		List<?> aRet = getList(listFilters, listOrder);

		for (Object o : aRet) {
			dataSource.add((Object[]) o);
		}
		return dataSource;
	}

	@Override
	public LinkedList<String> getColumnsDataSource() {

		LinkedList<String> template = new LinkedList<String>();
		for (Column column : getColumnsReport()) {
			template.add(column.getField());
		}
		return template;
	}

	@Override
	public abstract List<?> getList(HashMap<String, Object> listFilters,
			List<String> listOrder);

	@Override
	public void generateReport(Map<String, Object> params, String type,
			HashMap<String, Object> listFilters, List<String> listOrder) {

		try {
			ExportReport.exportAvancedReport(
					builReport(params, listFilters, listOrder),
					getDataSource(listFilters, listOrder), params, type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public abstract DynamicReport builReport(Map<String, Object> params,
			HashMap<String, Object> listFilters, List<String> listOrder)
			throws ColumnBuilderException, ClassNotFoundException;
}
