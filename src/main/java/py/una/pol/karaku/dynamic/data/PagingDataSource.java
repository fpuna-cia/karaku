/*
 * @DataSource.java 1.0 Jun 24, 2013 Sistema Integral de Gestion Hospitalaria
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
