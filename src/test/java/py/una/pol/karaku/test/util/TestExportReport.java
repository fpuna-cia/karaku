/*
 * @ExportReportTest.java 1.0 06/03/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.util;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.ExportReport;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 06/03/2014
 * 
 */
public class TestExportReport extends ExportReport {

	@Override
	protected String getUserName() {

		return "ROOT";
	}

	@Override
	protected String getNameSystem() {

		return "SIGH";
	}

	@Override
	public void generate(HttpServletResponse httpServletResponse,
			JasperPrint jasperPrint, Map<String, Object> params, String type)
			throws ReportException {

		return;
	}

	@Override
	public void generate(HttpServletResponse httpServletResponse,
			JasperPrint jasperPrint, String name, String type)
			throws ReportException {

		return;
	}

	@Override
	public <T> JasperPrint generateReport(String fileReport,
			JRDataSource dataSource, Map<String, Object> params, String type)
			throws ReportException {

		return null;
	}

	@Override
	protected HttpServletResponse getServletResponse() {

		return null;
	}

}
