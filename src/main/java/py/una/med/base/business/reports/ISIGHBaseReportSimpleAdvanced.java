/**
 * @ISIGHBaseReportSimpleAdvanced 1.0 25/03/13. Sistema Integral de Gestion
 *                                Hospitalaria
 */
package py.una.med.base.business.reports;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.reports.Column;

/**
 * Interfaz que define el servicio para los reportes simples cuyas columnas de
 * la grilla poseen atributos calculados.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 25/03/2013
 * 
 */
public interface ISIGHBaseReportSimpleAdvanced<T> {

	/**
	 * Metodo que obtiene un datasource vacio, con la estructura de columnas
	 * proporcionadas por getColumnsDataSource.
	 * 
	 * @return Esqueleto del datasource a ser utilizado
	 */
	public DRDataSource getStructDataSource();

	/**
	 * Metodo que obtiene el datasource correctamente estructurado, cargado con
	 * la lista de elementos filtrados.
	 * 
	 * @param logic
	 *            Logic parametrico que nos proporciona la funcionalidad de
	 *            consultar a la base de datos.
	 * @param where
	 *            Representa los filtros ingresados por el usuario.
	 * @return Datasource que sera utilizado para generar el reporte.
	 */
	public DRDataSource getDataSource(ISIGHBaseLogic<T, ?> logic, Where<T> where);

	/**
	 * Metodo que define la estructura del dataSource. Dicha estructura debe
	 * coincidir en orden con el resultado del metodo getList().
	 * 
	 * @return Columnas que definen la estructura del DataSource.
	 */
	public LinkedList<String> getColumnsDataSource();

	/**
	 * Define las columnas que contendra el reporte, las mismas son generadas de
	 * forma dinamica.
	 * 
	 * @return Columnas que deben ser generadas de forma dinamica.
	 */
	public LinkedList<Column> getColumnsReport();

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

	public List<?> getList(ISIGHBaseLogic<T, ?> logic, Where<T> where);

	/**
	 * Metodo que genera el reporte fisicamente.s
	 * 
	 * @param params
	 *            Parametros generales y especificos del reporte
	 * @param type
	 *            Tipo de exportacion, puede ser XLS o PDF
	 * @param logic
	 *            Logic parametrico que nos proporciona la funcionalidad de
	 *            consultar a la base de datos.
	 * @param where
	 *            Representa los filtros introducidos
	 */
	public void generateReport(Map<String, Object> params, String type,
			ISIGHBaseLogic<T, ?> logic, Where<T> where);

}
