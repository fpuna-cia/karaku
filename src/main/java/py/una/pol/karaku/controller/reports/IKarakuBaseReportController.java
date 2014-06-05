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
package py.una.pol.karaku.controller.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import py.una.pol.karaku.util.KarakuConverter;

/**
 * Interface del controlador utilizado para manejar los reportes complejos.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 13/05/2013
 * 
 */
public interface IKarakuBaseReportController<T, K extends Serializable> {

	Map<String, Object> getFilterOptions();

	void setFilterOptions(Map<String, Object> filterOptions);

	List<String> getBaseOrderOptions();

	List<String> getOrderSelected();

	void setOrderSelected(List<String> orderSelected);

	String getTypeExport();

	void setTypeExport(String typeExport);

	Map<String, Object> getFilterReport(Map<String, Object> paramsReport);

	void generateReport();

	String getDefaultPermission();

	/**
	 * @return
	 */
	KarakuConverter getConverter();

}
