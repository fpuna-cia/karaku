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
package py.una.pol.karaku.dynamic.data;

import java.util.List;

/**
 * Clase que define una fuente de datos paginada.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Jun 24, 2013
 * 
 */
public interface PagingDataSource {

	/**
	 * Retorna la cantidad máxima de registros.
	 * 
	 * @return cantidad de registros.
	 */
	Long getTotalCount();

	/**
	 * Retorna la página actual
	 * 
	 * @return número de página que se visualiza en el momento.
	 */
	Long getCurrentPage();

	/**
	 * Retorna la cantidad de registros por página.
	 * 
	 * @return cantidad de registros por página (número de filas).
	 */
	Long getRegistersPerPage();

	/**
	 * Retorna una lista de elementos para mostrar, <b>debe</b> traer solo
	 * aquellos que serán mostrados en el momento, es decir los que pertenecen a
	 * la página retornada por {@link #getCurrentPage()}.
	 * 
	 * @return Lista de objetos o lista vacía si no hay nada que mostrar.
	 */
	List<?> getItems();

	/**
	 * Este método es llamado cada vez que ocurre un evento que altera el estado
	 * de los datos. Por ejemplo, se cambio el filtro, se cambio la fuente de
	 * datos.
	 */
	void refresh();

}
