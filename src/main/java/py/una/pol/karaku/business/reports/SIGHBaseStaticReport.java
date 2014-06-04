/**
 * @SIGHBaseReportStatic 1.0 21/03/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.business.reports;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.ExportReport;

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
	public abstract <T> List<?> getList(IKarakuBaseLogic<T, ?> logic,
			Where<T> where);

	@Override
	public <T> void generateReport(String fileReport,
			Map<String, Object> params, String type,
			IKarakuBaseLogic<T, ?> logic, Where<T> where) throws ReportException {

		JRDataSource dataSource = new JRBeanCollectionDataSource(getList(logic,
				where));
		exportReport.exportReportStatic(fileReport, dataSource, params, type);
	}
}
