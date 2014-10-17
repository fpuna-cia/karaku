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
package py.una.pol.karaku.util;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.reports.ExportReport;
import py.una.pol.karaku.reports.KarakuReportBlock;

/**
 * Clase utilizada para generar reportes dentro de la aplicación.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 28/03/2014
 * 
 */
@Service
public class ReportHelper {

	@Autowired
	private ExportReport exportReport;

	@Log
	private transient Logger log;

	/**
	 * Genera un reporte Maestro-Detalle.
	 * 
	 * <p>
	 * Recibe bloques de reportes y realiza las configuraciones necesarias para
	 * generar un reporte con cada uno de ellos utilizando el template base sin
	 * la sección de criterios.
	 * 
	 * 
	 * @param master
	 *            Bloque referente a la cabecera.
	 * @param details
	 *            Bloques del reporte
	 * @param type
	 *            Tipo del reporte.
	 */
	public void generateMasterDetail(ReportBuilder reportBuilder) {

		try {
			// Configuramos el dataSource de cada bloque en los parámetros del
			// reporte.
			Map<String, Object> params = setDataSources(
					reportBuilder.getBlocksMasterDetail(),
					reportBuilder.getParams());

			exportReport.exportReportBlock(reportBuilder.getAlign(),
					reportBuilder.isSectionCriteria(),
					reportBuilder.getBlocksMasterDetail(), params,
					reportBuilder.getType());
		} catch (ReportException e) {
			log.warn("Can not create report master detail", e);
		}

	}

	public void generateReportBlocks(ReportBuilder reportBuilder) {

		try {
			if (reportBuilder.getBlocks() == null) {
				exportReport.blank(reportBuilder.getAlign(),
						reportBuilder.isSectionCriteria(),
						reportBuilder.getParams(), reportBuilder.getType());
			} else {
				// Configuramos el dataSource de cada bloque en los parámetros
				// del
				// reporte.
				Map<String, Object> params = setDataSources(
						reportBuilder.getBlocks(), reportBuilder.getParams());

				exportReport.exportReportBlock(reportBuilder.getAlign(),
						reportBuilder.isSectionCriteria(),
						reportBuilder.getBlocks(), params,
						reportBuilder.getType());
			}
		} catch (ReportException e) {
			log.warn("Can not create report master detail", e);
		}

	}

	public void generateReportBlocks(ReportBuilder reportBuilder,
			String templateFilePath) {

		try {
			if (reportBuilder.getBlocks() == null) {
				exportReport.blank(reportBuilder.isSectionCriteria(),
						reportBuilder.getParams(), reportBuilder.getType(),
						templateFilePath);
			} else {
				// Configuramos el dataSource de cada bloque en los parámetros
				// del
				// reporte.
				Map<String, Object> params = setDataSources(
						reportBuilder.getBlocks(), reportBuilder.getParams());

				exportReport.exportReportBlock(reportBuilder.getAlign(),
						reportBuilder.isSectionCriteria(),
						reportBuilder.getBlocks(), params,
						reportBuilder.getType(), templateFilePath);
			}
		} catch (ReportException e) {
			log.warn("Can not create report master detail", e);
		}

	}

	/**
	 * Configura el dataSource para cada bloque del reporte.
	 * 
	 * @param blocks
	 *            Bloques que se desean añadir.
	 * @param params
	 *            Parámetros del reporte.
	 * @return Parámetros configurados con cada uno de los dataSources.
	 */
	private Map<String, Object> setDataSources(List<KarakuReportBlock> blocks,
			Map<String, Object> params) {

		int id = 0;
		for (KarakuReportBlock block : blocks) {
			block.setNameDataSource("block_" + id);
			params.put(block.getNameDataSource(), block.getDataSource());
			id++;
		}
		return params;
	}

}
