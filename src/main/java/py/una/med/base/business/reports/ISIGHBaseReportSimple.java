/**
 * @ISIGHBaseReportSimple 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.List;
import java.util.Map;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.exception.ReportException;
import py.una.med.base.reports.Column;

/**
 * Interface que define el servicio para reportes simples utilizados en los
 * ABMs.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 2.0 19/02/2013
 */
public interface ISIGHBaseReportSimple {

	/**
	 * Obtiene la lista de elementos aplicando los fitros ingresados, si no se
	 * ingresa filtro alguno retorna toda la lista de registros almacenados.
	 * 
	 * @param logic
	 *            Logic parametrico que nos proporciona la funcionalidad de
	 *            consultar a la base de datos.
	 * @param where
	 *            Representa los filtros ingresados por el usuario.
	 * @return Lista filtrada
	 */
	<T> List<?> getList(ISIGHBaseLogic<T, ?> logic, Where<T> where);

	/**
	 * Metodo que genera el datasource necesario y el reporte correspondiente.
	 * 
	 * @param params
	 *            Parametros generales y especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @param columns
	 *            Columnas que deben ser visualizadas en el reporte
	 * @param logic
	 *            Logic parametrico que nos proporciona la funcionalidad de
	 *            consultar a la base de datos.
	 * @param where
	 *            Representa los filtros introducidos
	 * @param clazz
	 *            Clase de la entidad sobre la cual se realizara el reporte.
	 * @throws ReportException
	 */
	<T> void generateReport(Map<String, Object> params, String type,
			List<Column> columns, ISIGHBaseLogic<T, ?> logic, Where<T> where,
			Class<T> clazz) throws ReportException;
}
