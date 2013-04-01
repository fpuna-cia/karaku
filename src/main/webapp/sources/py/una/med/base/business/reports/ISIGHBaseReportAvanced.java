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
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;

/**
 * Iterface que define el servicio para los reportes complejos.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 15/03/2013
 */
public interface ISIGHBaseReportAvanced<T> {

	/**
	 * Metodo que forma un datasource en base a la estructura definida en
	 * StructDataSource y obtiene los datos que seran visualizados en el
	 * reporte.
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
	 * Metodo que define la estructura del dataSource. Dicha estructura debe
	 * coincidir en orden con el resultado del metodo getList().
	 * 
	 * @return
	 */
	public LinkedList<String> getStructDataSource();

	/**
	 * Metodo que realiza la consulta a la base de datos. Dicha consulta es
	 * especifica de cada reporte.
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
	 * @param listFilters
	 *            Filtros ingresados para realizar la consulta
	 * @param listOrder
	 *            Lista de ordenes en el cual se desea visualizar el reporte
	 */
	public void generateReport(Map<String, Object> params, String type,
			HashMap<String, Object> listFilters, List<String> listOrder);

	/**
	 * Define el reporte complejo especifico para cada caso. Prestar suma
	 * atencion al momento de utilizar el datasource, ya que el mismo se
	 * encuentra disponible en el parametro "datas" dentro del reporte.
	 * 
	 * @return
	 * @throws ColumnBuilderException
	 * @throws ClassNotFoundException
	 */
	public DynamicReport builReport() throws ColumnBuilderException,
			ClassNotFoundException;
}
