/**
 * @ISIGHBaseReportAvanced 1.1 05/03/13. Sistema Integral de Gestion
 *                         Hospitalaria
 */

package py.una.med.base.business.reports;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.reports.Column;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;

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
	public ISIGHBaseLogic<T, ?> getBaseLogic();

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
	public DRDataSource getDataSource(HashMap<String, Object> listFilters,
			List<String> listOrder);

	/**
	 * Metodo que obtiene un datasource vacio, con la estructura de columnas
	 * proporcionadas por getColumnsDataSource. Dicha estructura debe coincidir
	 * en orden con el resultado del metodo getList().
	 * 
	 * @return Esqueleto del datasource a ser utilizado
	 */
	public DRDataSource getStructDataSource();

	/**
	 * Metodo que define las columnas que tendra el dataSource.
	 * 
	 * @return Columnas que definen la estructura del DataSource.
	 */
	public LinkedList<String> getColumnsDataSource();

	/**
	 * Define las columnas que contendra el reporte, las mismas son generadas de
	 * forma dinamica. Dicha estructura debe coincidir en orden con el resultado
	 * del metodo getList()
	 * 
	 * @return Columnas que deben ser generadas de forma dinamica.
	 */
	public LinkedList<Column> getColumnsReport();

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
	public List<?> getList(HashMap<String, Object> listFilters,
			List<String> listOrder);

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
	 */
	public void generateReport(Map<String, Object> params, String type,
			HashMap<String, Object> listFilters, List<String> listOrder);

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
	 * @throws ColumnBuilderException
	 * @throws ClassNotFoundException
	 */
	public DynamicReport builReport(Map<String, Object> params,
			HashMap<String, Object> listFilters, List<String> listOrder)
			throws ColumnBuilderException, ClassNotFoundException;
}
