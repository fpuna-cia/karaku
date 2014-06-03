/*
 * @ReportHelper.java 1.0 28/03/2014 Sistema Integral de Gestion Hospitalaria
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
import py.una.pol.karaku.reports.SIGHReportBlock;

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

	/**
	 * Configura el dataSource para cada bloque del reporte.
	 * 
	 * @param blocks
	 *            Bloques que se desean añadir.
	 * @param params
	 *            Parámetros del reporte.
	 * @return Parámetros configurados con cada uno de los dataSources.
	 */
	private Map<String, Object> setDataSources(List<SIGHReportBlock> blocks,
			Map<String, Object> params) {

		int id = 0;
		for (SIGHReportBlock block : blocks) {
			block.setNameDataSource("block_" + id);
			params.put(block.getNameDataSource(), block.getDataSource());
			id++;
		}
		return params;
	}

}
