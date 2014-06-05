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
import net.sf.dynamicreports.report.datasource.DRDataSource;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.Column;

/**
 * Interfaz que define el servicio para los reportes simples cuyas columnas de
 * la grilla poseen atributos calculados.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 25/03/2013
 * 
 */
public interface IKarakuBaseReportSimpleAdvanced<T> {

	/**
	 * Metodo que obtiene un datasource vacio, con la estructura de columnas
	 * proporcionadas por getColumnsDataSource.
	 * 
	 * @return Esqueleto del datasource a ser utilizado
	 */
	DRDataSource getStructDataSource();

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
	DRDataSource getDataSource(IKarakuBaseLogic<T, ?> logic, Where<T> where);

	/**
	 * Metodo que define la estructura del dataSource. Dicha estructura debe
	 * coincidir en orden con el resultado del metodo getList().
	 * 
	 * @return Columnas que definen la estructura del DataSource.
	 */
	List<String> getColumnsDataSource();

	/**
	 * Define las columnas que contendra el reporte, las mismas son generadas de
	 * forma dinamica.
	 * 
	 * @return Columnas que deben ser generadas de forma dinamica.
	 */
	List<Column> getColumnsReport();

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

	List<?> getList(IKarakuBaseLogic<T, ?> logic, Where<T> where);

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
	 * @throws ReportException
	 */
	void generateReport(Map<String, Object> params, String type,
			IKarakuBaseLogic<T, ?> logic, Where<T> where) throws ReportException;

}
