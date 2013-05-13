/**
 * @SIGHBaseReportStatic 1.0 21/03/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.exception.ReportException;
import py.una.med.base.reports.ExportReport;

/**
 * Clase que proporciona las funcionalidades basicas para generar reportes
 * estaticos, utilizando archivos con extension jrxml.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 21/03/2013
 * 
 */
public abstract class SIGHBaseStaticReport implements ISIGHBaseStaticReport {

	@Autowired
	private ExportReport exportReport;

	@Override
	public abstract <T> List<?> getList(ISIGHBaseLogic<T, ?> logic,
			Where<T> where);

	@Override
	public <T> void generateReport(String fileReport,
			Map<String, Object> params, String type,
			ISIGHBaseLogic<T, ?> logic, Where<T> where) throws ReportException {

		JRDataSource dataSource = new JRBeanCollectionDataSource(getList(logic,
				where));
		exportReport.exportReportStatic(fileReport, dataSource, params, type);
	}
}
