/*
 * @BaseDAO.java 1.0 23/10/2021
 */
package py.una.med.base.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import org.hibernate.NonUniqueResultException;
import py.una.med.base.dao.annotations.CaseSensitive;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.util.EntityExample;

/**
 * Interfaz de los metidos proveídos por todos los daos, provee búsqueda por
 * criterios básicos, creación, modificación y eliminación de entidades, y
 * metadatos de la tabla a la que accede
 *
 * @author Arturo Volpe
 * @version 1.0, 23/10/2012
 * @since 1.0
 *
 * @param <T>
 *            Entidad que sera accedida a través del DAO
 * @param <ID>
 *            Clase de la Clave Primaria de la entidad
 *
 */
public interface BaseDAO<T, ID extends Serializable> {

	/**
	 * Retorna la entidad con un ID dado, retorna <code>null</code> si la
	 * entidad no existe
	 *
	 * @param id
	 *            Clave primario de la entidad
	 * @return Entidad con el id dado, o <code>null</code> en caso de no
	 *         encontrar
	 */
	public T getById(ID id);

	/**
	 * Retorna <b>una</b> entidad dado una entidad ejemplo
	 *
	 * @param example
	 *            Entidad que sera utilizada como ejemplo
	 * @return Entidad con los mismos atributos que el ejemplo,
	 *         <code>null</code> si no encuentra ninguna coincidencia
	 * @throws NonUniqueResultException
	 *             si para la consulta se encuentran varias entidades que
	 *             cumplen con el ejemplo, se lanza la excepción
	 * @see #getByExample(EntityExample)
	 */
	public T getByExample(T example) throws NonUniqueResultException;

	/**
	 * Retorna <b>una</b> entidad dado una entidad ejemplo
	 *
	 * @param example
	 *            Entidad que será utilizada como ejemplo
	 * @return Entidad con los mismos atributos que el ejemplo,
	 *         <code>null</code> si no encuentra ninguna coincidencia
	 * @throws NonUniqueResultException
	 *             si para la consulta se encuentran varias entidades que
	 *             cumplen con el ejemplo, se lanza la excepción
	 * @see EntityExample
	 *
	 */
	public T getByExample(EntityExample<T> example)
			throws NonUniqueResultException;

	/**
	 * Retorna una lista de entidades.
	 *
	 * @param params
	 *            Parámetros de la busqeuda, vease {@link ISearchParam}
	 * @return lista de entidades, null si no encuentra ninguna entidad
	 */
	public List<T> getAll(ISearchParam params);

	/**
	 * Retorna una lista de entidades que cumplen con un ejemplo dado
	 *
	 * @param example
	 *            Entidad utilizada como ejemplo
	 * @param params
	 *            Parametros de la busqueda, vease {@link ISearchParam}
	 * @return lista de todas las entidades que cumplen con la entidad pasada de
	 *         ejemplo
	 */
	public List<T> getAllByExample(T example, ISearchParam params);

	/**
	 * Retorna una lista de entidades que cumplen con un ejemplo dado
	 *
	 * @param example
	 *            Entidad utilizada como ejemplo
	 * @param params
	 *            Parametros de la busqueda, vease {@link ISearchParam}
	 * @return lista de entidades que cumplen con el ejemplo
	 */
	public List<T> getAllByExample(EntityExample<T> example, ISearchParam params);

	/**
	 * Crea o modifica una entidad dada.
	 *
	 * @param entity
	 *            Entidad a ser creada o modificada
	 * @return Entidad creada o modificada
	 */
	public T update(T entity);

	/**
	 * Crea una entidad, y le asigna un ID en caso de que el id sea
	 * autogenerado.
	 *
	 * <p>
	 * Se debe garantizar que el {@link Id} de la entidad retornada sea igual a
	 * la de la entidad pasada, pero <bold>no se debe, ni puede</bold>
	 * garantizar que los demás atributos sean iguales.
	 * </p>
	 * <p>
	 * Restricciones del negocio, como la utilización de la anotación
	 * {@link CaseSensitive} si se ven reflejadas en la entidad pasada.
	 * </p>
	 *
	 * <p>
	 * En general cualquier lógica aplicada por el {@link BaseDAO} debe ser
	 * reflejada en ambos lados, pero aquellos cambios que se realizan al
	 * insertar datos (como triggers) no <bold>no pueden, ni deben</bold> ser
	 * persistidos en ambas referencias.
	 * </p>
	 *
	 * @param entity
	 *            Entidad todavia no persistida a ser persistida.
	 * @return Entidad ya persistida con el ID generado
	 */
	public T add(T entity);

	/**
	 * Elimina una entidad de la fuente de informacion
	 *
	 *
	 * @param entity
	 *            Entidad a ser eliminada
	 */
	public void remove(T entity);

	/**
	 * Elimina una entidad de la fuente de información
	 *
	 * @param id
	 *            ID de la Entidad a ser eliminada
	 */
	public void remove(ID id);

	/**
	 * Retorna la clase de la entidad que es accedida por este DAO
	 *
	 * @return Clase de la entidad T
	 */
	public Class<T> getClassOfT();

	/**
	 * Retorna el nombre de la tabla que es reflejada por al entidad
	 *
	 * @return Cadena que representa el nombre de la tabla en la base de datos
	 */
	public String getTableName();

	/**
	 * Retorna la cantidad de registros que retornaría la consultada dada, omite
	 * los valores de orden, cantidad de resultados y limite de resultados
	 *
	 * @return cantidad total de registros en la base de datos
	 */
	public Long getCount();

	/**
	 * Retorna la cantidad de registros que retornarla la consultada dada, omite
	 * los valores de orden, cantidad de resultados y limite de resultados,
	 * Basándose en un ejemplo de entidad
	 *
	 * @param example
	 *            entidad ejemplo
	 * @return total de registros que cumplen con ese ejemplo
	 */
	public Long getCountByExample(EntityExample<T> example);

	/**
	 * Retorna los registros que cumplan con las restricciones del where
	 *
	 * @param where
	 *            criterios de búsqueda
	 * @param params
	 *            Parámetros de filtro de resultados
	 *
	 * @return lista de entidades
	 */
	public List<T> getAll(Where<T> where, ISearchParam params);

	/**
	 * Retorna la cantidad de registros que retornaría la consulta dada con el
	 * where pasado como parámetro
	 *
	 * @param where
	 *            Que filtra los resultados
	 * @return cantidad de registros
	 */
	public Long getCount(Where<T> where);

	public EntityManager getEntityManager();
}
