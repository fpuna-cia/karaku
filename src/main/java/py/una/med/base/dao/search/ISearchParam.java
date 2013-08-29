package py.una.med.base.dao.search;

import java.util.List;
import py.una.med.base.dao.BaseDAO;

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
	List<OrderParam> getOrders();

	/**
	 * Cambia los ordenes actuales, por la lista que es pasada como parametros
	 *
	 * @param orders
	 *            nuevo ordenamiento de resultados
	 */
	void setOrders(List<OrderParam> orders);

	/**
	 * Retorna el numero de fila del primer resultado
	 *
	 * @return Numero de fila del primer resultado
	 */
	Integer getOffset();

	/**
	 * Define el numero de fila del primer resultado que sera retornado
	 *
	 * @param offset
	 *            numero de fila del primer resultado
	 */
	ISearchParam setOffset(Integer offset);

	/**
	 * Retorna la cantidad de filas a ser retornadas
	 *
	 * @return Numero que representa la cantidad de filas
	 */
	Integer getLimit();

	/**
	 * Define la cantidad de filas a ser retornadas
	 *
	 * @param limit
	 *            numero que representa el limite de filas a ser retornadas,
	 *            null si no hay limite
	 */
	ISearchParam setLimit(Integer limit);

	/**
	 * Agrega un {@link OrderParam} a la consulta actual.
	 * <p>
	 * Si el nombre definido en {@link OrderParam#getColumnName()} no existe en
	 * la tabla, no se lanzara ninguna excepción, pero cuando se utilize con el
	 * {@link BaseDAO} se lanzará una excepción de columna no encontrada.
	 * </p>
	 *
	 * @return this
	 */
	ISearchParam addOrder(OrderParam orderParam);

	/**
	 * Agrega un {@link OrderParam} a la consulta actual, con el orden definido
	 * en <code>asc</code> para la columna <code>columnName</code>.
	 * <p>
	 * Si la columna <code>columnName</code> definido en no existe en la tabla,
	 * no se lanzara ninguna excepción, pero cuando se utilize con el
	 * {@link BaseDAO} se lanzará una excepción de columna no encontrada.
	 *
	 * @return this
	 */
	ISearchParam addOrder(String columnName, boolean asc);
}
