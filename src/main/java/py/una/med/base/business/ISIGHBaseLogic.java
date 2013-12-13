/*
 * @ISIGHBaseLogic 1.4 18/02/2013 Sistema integral de Gestion Hospitalaria
 */
package py.una.med.base.business;

import java.io.Serializable;
import java.util.List;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.repo.ISIGHBaseDao;

/**
 * Intefaz que define las actividades basicas de la logica de negocio para todos
 * los casos de uso, funciona como un wrapper de {@link ISIGHBaseDao} y agrega
 * funcionales, que escapan alcance de estos. <br>
 * Deberia ser utilizado como la base para todos los servicios que son proveídos
 * por el sistema
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.4 18/02/2013
 * 
 * @param <T>
 *            Clase de la entidad
 * @param <K>
 *            Clase del id de la entidad
 */
public interface ISIGHBaseLogic<T, K extends Serializable> {

	/**
	 * Retorna el DAO que se utiliza para realizar operaciones baáicas.
	 * 
	 * @return Dao Base
	 */
	ISIGHBaseDao<T, K> getDao();

	/**
	 * Crea una consulta con los atributos del parámetro example para buscar UN
	 * resultado que lo corresponda en la base de datos.
	 * 
	 * @param example
	 *            Entidad ejemplo
	 * @return null si no hay coincidencias o Entidad de la base de datos que
	 *         tiene los mismos atributos que el ejemplo, y los demás los carga
	 *         de la base de datos
	 * @throws org.hibernate.NonUniqueResultException
	 *             Cuando se encuentra mas de un resultado
	 */
	T getByExample(T example);

	/**
	 * Trae todos los registros segun el parametro
	 * 
	 * @param params
	 *            define el orden, limite y cantidad de registros
	 * @return lista limitada y ordenada por los parametros
	 */
	List<T> getAll(ISearchParam params);

	/**
	 * Crea una consulta con los atributos seteados del parametro example para
	 * buscar UN resultado que lo correspodna en la base de datos
	 * 
	 * @param example
	 *            Entidad ejemplo
	 * @param params
	 *            define el orden, limite y cantidad de registros
	 * @return null si no hay coincidencias o lista entidades de la base de
	 *         datos que tiene los mismos atributos que el ejemplo.
	 */
	List<T> getAllByExample(EntityExample<T> example, ISearchParam params);

	/**
	 * Crea una consulta con los atributos seteados del parametro example para
	 * buscar UN resultado que lo correspodna en la base de datos
	 * 
	 * @param where
	 *            Criterios de busqueda
	 * @param params
	 *            define el orden, limite y cantidad de registros
	 * @return null si no hay coincidencias o lista entidades de la base de
	 *         datos que cumplen con el where pasado.
	 * @see Where
	 */
	List<T> getAll(Where<T> where, ISearchParam params);

	/**
	 * Persiste las modificaciones realizadas a una entidad <br>
	 * <i>Notese que si se activa algun trigger o contador en la base de datos,
	 * que provoca una modificacion del registro, la entidad retornada tendra
	 * los cambios</i>
	 * 
	 * @param entity
	 *            a ser persistida
	 * @return otra instancia de la entidad con los atributos actualizados
	 */
	T update(T entity);

	/**
	 * Persiste una entidad en la base de datos
	 * 
	 * @param entity
	 *            a ser persistida
	 * @return entidad persistida
	 */
	T add(T entity);

	/**
	 * Elimina una entidad de la base de datos
	 * 
	 * @param entity
	 *            a ser eliminada
	 */
	void remove(T entity);

	/**
	 * Dado una entidad retorna su ID
	 * 
	 * @param entity
	 *            entidad de la cual se desea su ID
	 * @return ID de la entidad entity
	 */
	K getIdValue(T entity);

	/**
	 * Retorna una nueva instancia de T
	 * 
	 * @return Entidad recien iniciailzada
	 * 
	 */
	T getNewInstance();

	/**
	 * 
	 * Retorna una lista de entidades, usese
	 * {@link #getAll(Where, ISearchParam)}
	 * 
	 * @deprecated utilizar
	 *             {@link #getAllByExample(EntityExample, ISearchParam)}
	 * @param ejemplo
	 *            entidad ejemplo
	 * @param sp
	 *            criterios de filtros
	 * @return Lista de entidades
	 */
	@Deprecated
	List<T> getAllByExample(T ejemplo, ISearchParam sp);

	/**
	 * 
	 * @param example
	 * @return
	 */
	Long getCountByExample(EntityExample<T> example);

	Long getCount(Where<T> where);

	Long getCount();

	T getById(K id);

}
