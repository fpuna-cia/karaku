/*
 * @SIGHBaseReportAvanced.java 1.1 05/03/13. Sistema Integral de Gestion
 * Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.exception.ReportException;
import py.una.med.base.reports.Column;
import py.una.med.base.reports.ExportReport;
import py.una.med.base.reports.SIGHReportBlockGrid;
import py.una.med.base.util.ListHelper;
import ar.com.fdvs.dj.domain.DynamicReport;

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

	@Autowired
	private ExportReport exportReport;

	@Override
	public abstract ISIGHBaseLogic<T, ?> getBaseLogic();

	@Override
	public DRDataSource getStructDataSource() {

		DRDataSource dataSource = new DRDataSource(
				ListHelper.asArray(getColumnsDataSource()));
		return dataSource;
	}

	@Override
	public DRDataSource getDataSource(Map<String, Object> listFilters,
			List<String> listOrder) {

		DRDataSource dataSource = getStructDataSource();

		List<?> aRet = getList(listFilters, listOrder);

		for (Object o : aRet) {
			dataSource.add((Object[]) o);
		}
		return dataSource;
	}

	@Override
	public List<String> getColumnsDataSource() {

		LinkedList<String> template = new LinkedList<String>();
		for (Column column : getColumnsReport()) {
			template.add(column.getField());
		}
		return template;
	}

	@Override
	public abstract List<?> getList(Map<String, Object> listFilters,
			List<String> listOrder);

	@Override
	public void generateReport(boolean dataSource, Map<String, Object> params,
			String type, Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException {

		if (!dataSource) {
			exportReport.exportAvancedReport(
					builReport(params, listFilters, listOrder),
					new JREmptyDataSource(), params, type);
		} else {
			exportReport.exportAvancedReport(
					builReport(params, listFilters, listOrder),
					getDataSource(listFilters, listOrder), params, type);
		}

	}

	/**
	 * Se utiliza para setear como parametro una lista de datasources, esto se
	 * aplica para subreportes concatenados
	 * 
	 * @param blocks
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> setDataSources(List<SIGHReportBlockGrid> blocks,
			Map<String, Object> params) {

		for (SIGHReportBlockGrid block : blocks) {
			params.put(block.getNameDataSource(), block.getDataSource());
		}
		return params;
	}

	@Override
	public abstract DynamicReport builReport(Map<String, Object> params,
			Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException;
}
