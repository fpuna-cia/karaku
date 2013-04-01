/**
 * @SIGHBaseReportDetail 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import py.una.med.base.reports.ExportReport;
import py.una.med.base.reports.SIGHReportDetails;

/**
 * Clase que implementa el servicio para reportes del tipo cabecera-detalle. Se
 * refiere al reporte especifico de un registro de la grilla.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 07/03/2013
 * 
 */
public abstract class SIGHBaseReportDetail<T> implements
		ISIGHBaseReportDetail<T> {

	@Override
	public abstract List<?> getDetails(T bean);

	@Override
	public void generateReport(SIGHReportDetails report,
			Map<String, Object> params, String type, T bean, Class<T> clazz) {

		JRDataSource datasource = new JRBeanCollectionDataSource(
				getDetails(bean));
		ExportReport
				.exportDetailReport(report, clazz, datasource, params, type);
	}

	@Override
	public void generateReport(String path, SIGHReportDetails report,
			Map<String, Object> params, String type, T bean, Class<T> clazz) {

		JRDataSource datasource = new JRBeanCollectionDataSource(
				getDetails(bean));
		ExportReport.exportDetailReport(path, report, clazz, datasource,
				params, type);
	}

}
