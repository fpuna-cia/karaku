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

package py.una.pol.karaku.business.reports;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.Column;
import py.una.pol.karaku.reports.ExportReport;
import py.una.pol.karaku.reports.KarakuReportBlockGrid;
import py.una.pol.karaku.util.ListHelper;
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
@Service
@Transactional
public abstract class KarakuBaseReportAdvanced<T> implements
		IKarakuBaseReportAdvanced<T> {

	@Autowired
	private ExportReport exportReport;

	@Override
	public IKarakuBaseLogic<T, ?> getBaseLogic() {

		throw new NotImplementedException();
	}

	@Override
	public DRDataSource getStructDataSource() {

		DRDataSource dataSource = new DRDataSource(ListHelper.asArray(this
				.getColumnsDataSource()));
		return dataSource;
	}

	@Override
	public DRDataSource getDataSourceCustom(Map<String, Object> listFilters,
			List<String> listOrder) {

		DRDataSource dataSource = this.getStructDataSource();

		List<?> aRet = this.getList(listFilters, listOrder);

		for (Object o : aRet) {
			dataSource.add((Object[]) o);
		}
		return dataSource;
	}

	public JRDataSource getDataSource(Map<String, Object> listFilters,
			List<String> listOrder) {

		return new JRBeanCollectionDataSource(this.getList(listFilters,
				listOrder));
	}

	@Override
	public List<String> getColumnsDataSource() {

		LinkedList<String> template = new LinkedList<String>();
		for (Column column : this.getColumnsReport()) {
			template.add(column.getField());
		}
		return template;
	}

	@Override
	public List<Column> getColumnsReport() {

		return Collections.emptyList();
	}

	@Override
	public List<?> getList(Map<String, Object> listFilters,
			List<String> listOrder) {

		return Collections.emptyList();
	}

	/**
	 * @deprecated user
	 *             {@link #generateReport(boolean, boolean, Map, String, Map, List)}
	 */
	@Deprecated
	@Override
	public void generateReport(boolean dataSource, Map<String, Object> params,
			String type, Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException {

		if (!dataSource) {
			this.exportReport.exportAvancedReport(
					this.builReport(params, listFilters, listOrder),
					new JREmptyDataSource(), params, type);
		} else {
			this.exportReport.exportAvancedReport(
					this.builReport(params, listFilters, listOrder),
					this.getDataSourceCustom(listFilters, listOrder), params,
					type);
		}

	}

	@Override
	public void generateReport(boolean dataSource, boolean isClass,
			Map<String, Object> params, String type,
			Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException {

		// Si posee más de un dataSource
		if (!dataSource) {
			this.exportReport.exportAvancedReport(
					this.builReport(params, listFilters, listOrder),
					new JREmptyDataSource(), params, type);
		} else {
			// Si la lista a ser visualizada en el reporte esta representada por
			// una clase
			if (isClass) {
				this.exportReport.exportAvancedReport(
						this.builReport(params, listFilters, listOrder),
						this.getDataSource(listFilters, listOrder), params,
						type);
			} else {
				this.exportReport.exportAvancedReport(
						this.builReport(params, listFilters, listOrder),
						this.getDataSourceCustom(listFilters, listOrder),
						params, type);
			}

		}

	}

	@Override
	public void generateReport(String path, Map<String, Object> params,
			String type, Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException {

		JRDataSource dataSource = new JRBeanCollectionDataSource(this.getList(
				listFilters, listOrder));
		this.exportReport.exportReportStatic(path, dataSource, params, type);

	}

	/**
	 * Se utiliza para REPORTES ESTÁTICOS
	 * 
	 * @param blocks
	 * @param params
	 * @return
	 */

	@Override
	public void generateReportStatic(String fileReport,
			Map<String, Object> params, String type,
			Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException {

		JRDataSource datasource = new JRBeanCollectionDataSource(this.getList(
				listFilters, listOrder));
		this.exportReport.exportReportStatic(fileReport, datasource, params,
				type);
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
	public Map<String, Object> setDataSources(List<KarakuReportBlockGrid> blocks,
			Map<String, Object> params) {

		for (KarakuReportBlockGrid block : blocks) {
			params.put(block.getNameDataSource(), block.getDataSource());
		}
		return params;
	}

	@Override
	public DynamicReport builReport(Map<String, Object> params,
			Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException {

		return new DynamicReport();
	}
}
