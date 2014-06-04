/**
 * @ISIGHBaseReportDetail 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.business.reports;

import java.util.List;
import java.util.Map;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.reports.Align;
import py.una.pol.karaku.reports.SIGHReportBlock;
import py.una.pol.karaku.reports.SIGHReportBlockSign;
import py.una.pol.karaku.reports.SIGHReportDetails;

/**
 * Interface que define el servicio para reportes del tipo cabecera-detalle. Se
 * refiere al reporte especifico de un registro de la grilla.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 07/03/2013
 */
public interface ISIGHBaseReportDetail<T> {

	/**
	 * Nos retorna el logic para poder acceder al dao correspondiente de manera
	 * a tener acceso a la base de datos
	 * 
	 * @return
	 */
	IKarakuBaseLogic<T, ?> getBaseLogic();

	/**
	 * Metodo que realiza la consulta HQL especifica, la cual obtiene los
	 * elementos necesarios para generar el reporte.
	 * 
	 * 
	 * @param bean
	 *            Registro cuyos detalles deben ser visualizados.
	 * 
	 * @param bean
	 * @return Detalles
	 */
	List<?> getDetails(T bean);

	/**
	 * Metodo que genera un reporte utilizando el template base, donde solo se
	 * definen los parametros generales, tanto los atributos de la cabecera como
	 * los detalles son generados de forma dinamica.
	 * 
	 * @param report
	 *            Estructura que contiene la definicion estructural del reporte
	 *            y datos necesarios para generar el mismo.
	 * @param align
	 *            Alineacion con la cual se desea visualizar el reporte
	 * @param params
	 *            Parametros del reporte
	 * @param type
	 *            Tipo de exportacion puede ser PDF o XLS.
	 * 
	 * @param bean
	 *            Registro cuyos detalles deben ser visualizados.
	 */
	void generateReport(SIGHReportDetails report, Align align,
			Map<String, Object> params, String type, T bean, Class<?> clazz);

	/**
	 * Metodo que genera un reporte utilizando un archivo especifico, donde se
	 * definen los atributos de la cabecera, los detalles son generados de forma
	 * dinamica.
	 * 
	 * @param path
	 *            Ubicacion del template que se desea adicionar al reporte. Debe
	 *            ser el nombre de la dependencia, luego del directorio, seguido
	 *            del nombre del jrxml. <br>
	 *            <b>Por ejemplo</b><br>
	 *            <ol>
	 *            <li><b>identificacion/persona/detallesPersona.jrxml</b>
	 *            </ol>
	 * @param report
	 *            Estructura que contiene la definicion estructural del reporte
	 *            y datos necesarios para generar el mismo.
	 * @param params
	 *            Parametros del reporte
	 * @param type
	 *            Tipo de exportacion puede ser PDF o XLS.
	 * 
	 * @param bean
	 *            Registro cuyos detalles deben ser visualizados.
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 */
	void generateReport(String path, SIGHReportDetails report,
			Map<String, Object> params, String type, T bean, Class<?> clazz);

	/**
	 * Metodo que genera un reporte estatico de un registro especifico de la
	 * grilla.
	 * 
	 * @param path
	 *            Ubicacion del template que se desea adicionar al reporte. Debe
	 *            ser el nombre de la dependencia, luego del directorio, seguido
	 *            del nombre del jrxml. <br>
	 *            <b>Por ejemplo</b><br>
	 *            <ol>
	 *            <li><b>identificacion/persona/detallesPersona.jrxml</b>
	 *            </ol>
	 * @param params
	 *            Parametros del reporte
	 * @param type
	 *            Tipo de exportacion puede ser PDF o XLS.
	 * 
	 * @param bean
	 *            Registro cuyos detalles deben ser visualizados.
	 * @param clazz
	 *            Clase de la entidad sobre la cual se desea realizar el reporte
	 */
	void generateReport(String path, Map<String, Object> params, String type,
			T bean, Class<?> clazz);

	/**
	 * @param fields
	 * @param params
	 * @param type
	 */
	void generateReport(List<SIGHReportBlock> blocks,
			Map<String, Object> params, String type);

	/**
	 * @param blocks
	 * @param signs
	 * @param params
	 * @param type
	 */
	void generateReport(List<SIGHReportBlock> blocks,
			List<SIGHReportBlockSign> signs, Map<String, Object> params,
			String type);

	/**
	 * Define si la sección de parámetros debe ser visible o no.
	 * 
	 * 
	 * @return <b><code>true</code></b> Por defecto dicha sección debe
	 *         visualizarse
	 */
	boolean withCriteriaVisible();

}
