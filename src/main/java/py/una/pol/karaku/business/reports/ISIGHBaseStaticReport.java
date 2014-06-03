/**
 * @ISIGHBaseStaticReport 1.0 21/03/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.business.reports;

import java.util.List;
import java.util.Map;
import py.una.pol.karaku.business.ISIGHBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.exception.ReportException;

/**
 * Interface que define el servicio para generar reportes completamente
 * estaticos, utilizando archivos con extension jrxml.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 21/03/2013
 * 
 */
public interface ISIGHBaseStaticReport {

	/**
	 * Obtiene la lista de elementos aplicando los filtros ingresados, si no se
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
	 * 
	 * @param fileReport
	 *            Ubicacion del archivo que define la estructura del reporte.
	 *            Debe ser el nombre de la dependencia, luego del directorio,
	 *            seguido del nombre del jrxml. <br>
	 *            <b>Por ejemplo</b><br>
	 *            <ol>
	 *            <li><b>identificacion/persona/report.jrxml</b>
	 *            </ol>
	 * @param params
	 *            Parametros generales y especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @param logic
	 *            Logic parametrico que nos proporciona la funcionalidad de
	 *            consultar a la base de datos.
	 * @param where
	 *            Representa los filtros introducidos
	 * @throws ReportException
	 */
	<T> void generateReport(String fileReport, Map<String, Object> params,
			String type, ISIGHBaseLogic<T, ?> logic, Where<T> where)
			throws ReportException;
}
