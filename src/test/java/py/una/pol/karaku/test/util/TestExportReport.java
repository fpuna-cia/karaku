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
