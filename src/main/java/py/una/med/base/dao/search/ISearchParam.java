package py.una.med.base.dao.search;

import java.util.List;

/**
 * Interfaz que define parámetros de búsqueda que afecten las clausulas ORDER
 * BY, LIMIT, etc. No interviene en el SELECT, WHERE ni el JOIN.
 * 
 * @see SearchParam
 * @author Arturo Volpe
 * @version 1.0, 23/10/2012
 * @since 1.0
 */
public interface ISearchParam {

	/**
	 * Retorna una lista en orden, de los ordenes por los cuales se ordenara la
	 * consulta
	 * 
	 * @return lista de {@link OrderParam} ordenados
	 */
	public List<OrderParam> getOrders();

	/**
	 * Cambia los ordenes actuales, por la lista que es pasada como parametros
	 * 
	 * @param orders
	 *            nuevo ordenamiento de resultados
	 */
	public void setOrders(List<OrderParam> orders);

	/**
	 * Retorna el numero de fila del primer resultado
	 * 
	 * @return Numero de fila del primer resultado
	 */
	public Integer getOffset();

	/**
	 * Define el numero de fila del primer resultado que sera retornado
	 * 
	 * @param offset
	 *            numero de fila del primer resultado
	 */
	public void setOffset(Integer offset);

	/**
	 * Retorna la cantidad de filas a ser retornadas
	 * 
	 * @return Numero que representa la cantidad de filas
	 */
	public Integer getLimit();

	/**
	 * Define la cantidad de filas a ser retornadas
	 * 
	 * @param limit
	 *            numero que representa el limite de filas a ser retornadas,
	 *            null si no hay limite
	 */
	public void setLimit(Integer limit);

}
