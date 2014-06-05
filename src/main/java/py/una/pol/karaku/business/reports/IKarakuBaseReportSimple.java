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
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.Column;

/**
 * Interface que define el servicio para reportes simples utilizados en los
 * ABMs.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 2.0 19/02/2013
 */
public interface IKarakuBaseReportSimple {

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
	<T> List<?> getList(IKarakuBaseLogic<T, ?> logic, Where<T> where);

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
			List<Column> columns, IKarakuBaseLogic<T, ?> logic, Where<T> where,
			Class<T> clazz) throws ReportException;
}
