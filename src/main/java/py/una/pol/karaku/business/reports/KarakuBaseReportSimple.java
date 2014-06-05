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
import org.springframework.stereotype.Service;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.Column;
import py.una.pol.karaku.reports.ExportReport;

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
public class KarakuBaseReportSimple implements IKarakuBaseReportSimple {

	@Autowired
	private ExportReport exportReport;

	@Override
	public <T> List<?> getList(IKarakuBaseLogic<T, ?> logic, Where<T> where) {

		if (where != null) {
			return logic.getAll(where, null);
		}
		return logic.getAll(null);
	}

	@Override
	public <T> void generateReport(Map<String, Object> params, String type,
			List<Column> columns, IKarakuBaseLogic<T, ?> logic, Where<T> where,
			Class<T> clazz) throws ReportException {

		JRDataSource datasource = new JRBeanCollectionDataSource(getList(logic,
				where));
		exportReport.exportSimpleReport(columns, clazz, datasource, params,
				type);
	}

}
