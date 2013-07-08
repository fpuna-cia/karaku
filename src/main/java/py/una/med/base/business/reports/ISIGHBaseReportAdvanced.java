/**
 * @ISIGHBaseReportAvanced 1.1 05/03/13. Sistema Integral de Gestion
 *                         Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.exception.ReportException;
import py.una.med.base.reports.Column;
import ar.com.fdvs.dj.domain.DynamicReport;

/**
 * Iterface que define el servicio para los reportes complejos.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 15/03/2013
 */
public interface ISIGHBaseReportAdvanced<T> {

	/**
	 * Nos retorna el logic para poder acceder al dao correspondiente de manera
	 * a tener acceso a la base de datos
	 * 
	 * @return
	 */
	ISIGHBaseLogic<T, ?> getBaseLogic();

	/**
	 * Metodo que forma un datasource en base a la estructura definida en
	 * StructDataSource y obtiene los datos que seran visualizados en el
	 * reporte. Cabe destacar que los datos que seran visualizados pueden ser
	 * campos calculados, o concatenados, y si la consulta no obtiene
	 * directamente dichos campos, es aqui donde se debe definir tal
	 * caracteristica sobreescribiendo este metodo. Por defecto se asume que la
	 * consulta retorna todos los atributos necesarios ya calculados.
	 * 
	 * @param listFilters
	 *            Filtros ingresados para realizar la consulta
	 * @param listOrder
	 *            Lista de ordenes en el cual se desea visualizar el reporte
	 * @return
	 */
	DRDataSource getDataSource(Map<String, Object> listFilters,
			List<String> listOrder);

	/**
	 * Metodo que obtiene un datasource vacio, con la estructura de columnas
	 * proporcionadas por getColumnsDataSource. Dicha estructura debe coincidir
	 * en orden con el resultado del metodo getList().
	 * 
	 * @return Esqueleto del datasource a ser utilizado
	 */
	DRDataSource getStructDataSource();

	/**
	 * Metodo que define las columnas que tendra el dataSource.
	 * 
	 * @return Columnas que definen la estructura del DataSource.
	 */
	List<String> getColumnsDataSource();

	/**
	 * Define las columnas que contendra el reporte, las mismas son generadas de
	 * forma dinamica. Dicha estructura debe coincidir en orden con el resultado
	 * del metodo getList()
	 * 
	 * @return Columnas que deben ser generadas de forma dinamica.
	 */
	List<Column> getColumnsReport();

	/**
	 * Metodo que realiza la consulta a la base de datos. Dicha consulta es
	 * especifica de cada reporte.
	 * 
	 * 
	 * @param listFilters
	 *            Filtros ingresados para realizar la consulta.
	 * @param listOrder
	 *            Lista de ordenes en el cual se desea visualizar el reporte.
	 * @return Lista de elementos que cumplen con las condiciones de filtro
	 *         ingresadas o seleccionadas.
	 */
	List<?> getList(Map<String, Object> listFilters, List<String> listOrder);

	/**
	 * Metodo que genera el reporte fisicamente.
	 * 
	 * @param params
	 *            Parametros generales y especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * 
	 * @param listFilters
	 *            Filtros ingresados para realizar la consulta
	 * @param listOrder
	 *            Lista de ordenes en el cual se desea visualizar el reporte
	 * @throws ReportException
	 */
	void generateReport(Map<String, Object> params, String type,
			Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException;

	/**
	 * Define el reporte complejo especifico para cada caso.
	 * 
	 * @param params
	 *            Parametros generales y especificos del reporte
	 * 
	 * @param listFilters
	 *            Filtros ingresados para realizar la consulta
	 * @param listOrder
	 *            Lista de ordenes en el cual se desea visualizar el reporte
	 * @return
	 * @throws ReportException
	 */
	DynamicReport builReport(Map<String, Object> params,
			Map<String, Object> listFilters, List<String> listOrder)
			throws ReportException;

}
