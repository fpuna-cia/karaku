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
public abstract class KarakuBaseStaticReport implements IKarakuBaseStaticReport {

	@Autowired
	private ExportReport exportReport;

	@Override
	public abstract <T> List<?> getList(IKarakuBaseLogic<T, ?> logic,
			Where<T> where);

	@Override
	public <T> void generateReport(String fileReport,
			Map<String, Object> params, String type,
			IKarakuBaseLogic<T, ?> logic, Where<T> where)
			throws ReportException {

		JRDataSource dataSource = new JRBeanCollectionDataSource(getList(logic,
				where));
		exportReport.exportReportStatic(fileReport, dataSource, params, type);
	}
}
