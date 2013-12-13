/**
 * @SIGHBaseReportSimple 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.exception.ReportException;
import py.una.med.base.reports.Column;
import py.una.med.base.reports.ExportReport;

/**
 * Servicio que implementa el servicio para reportes simples utilizados en los
 * ABMs.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 19/02/2013
 * 
 */
@Service
public class SIGHBaseReportSimple implements ISIGHBaseReportSimple {

	@Autowired
	private ExportReport exportReport;

	@Override
	public <T> List<?> getList(ISIGHBaseLogic<T, ?> logic, Where<T> where) {

		if (where != null) {
			return logic.getAll(where, null);
		}
		return logic.getAll(null);
	}

	@Override
	public <T> void generateReport(Map<String, Object> params, String type,
			List<Column> columns, ISIGHBaseLogic<T, ?> logic, Where<T> where,
			Class<T> clazz) throws ReportException {

		JRDataSource datasource = new JRBeanCollectionDataSource(getList(logic,
				where));
		exportReport.exportSimpleReport(columns, clazz, datasource, params,
				type);
	}

}
