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
package py.una.pol.karaku.business;

import java.io.Serializable;
import java.util.List;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.select.Select;
import py.una.pol.karaku.dao.util.EntityExample;
import py.una.pol.karaku.repo.IKarakuBaseDao;

/**
 * Intefaz que define las actividades basicas de la logica de negocio para todos
 * los casos de uso, funciona como un wrapper de {@link IKarakuBaseDao} y agrega
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
public interface IKarakuBaseLogic<T, K extends Serializable> {

	/**
	 * Retorna el DAO que se utiliza para realizar operaciones baáicas.
	 * 
	 * @return Dao Base
	 */
	IKarakuBaseDao<T, K> getDao();

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

	/**
	 * Lista de entidades proyectadas de acuerdo a un {@link Select}
	 * condicionadas por un {@link Where} y limitadas por un
	 * {@link ISearchParam}.
	 * 
	 * <p>
	 * La diferencia principal con {@link #getAll(Where, ISearchParam)} es que
	 * no retorna todas las columnas, si bien retorna una entidad, la misma
	 * tiene -en las columnas no traídas- el valor <code>null</code>.
	 * </p>
	 * <p>
	 * Por ejemplo, para hacer la consulta HQL:
	 * 
	 * <pre>
	 * select c.id, c.descripcion
	 * 	from Ciudad c join c.pais
	 * 	where p.descripcion = 'Paraguay'
	 * 	limit 10
	 * 	order by c.id
	 * 
	 * </pre>
	 * 
	 * Se puede utilizar el código:
	 * 
	 * <pre>
	 * dao.get(
	 * 	Select.columns(&quot;id&quot;, &quot;descripcion&quot;),
	 * 	new Where().addClause(Clauses.eq("pais.descripcion", 'Paraguay'),
	 * 	new SearchParam().addOrder("id").setLimit(10);
	 * 
	 * );
	 * </pre>
	 * 
	 * Y pasará el siguiente test:
	 * 
	 * <pre>
	 * {@literal @}Test
	 * public void test() {
	 * 	list = dao.getIdAndDescription();
	 * 	for (Entidad e : list) {
	 * 		assertNotNull(e.getId());
	 * 		assertNotNull(e.getDescripcion());
	 * 		assertNull(e.<b>getPais</b>());
	 * 		assertNull(e.get<b>CualquierOtroCampo()</b>));
	 * 	}
	 * }
	 * </pre>
	 * 
	 * Todos los atributos son opcionales )
	 * 
	 * @param select
	 *            select para limitar la cantidad de columnas, o
	 *            <code>null</code> si se desean todas.
	 * @param where
	 *            condicionador de la consulta o <code>null</code> si no se
	 *            aplican restricciones.
	 * @param params
	 *            parámetros de orden, offset y limites o <code>null</code> si
	 *            se desean todas.
	 * @return Lista de entidades, nunca <code>null</code>, si no hay entidades,
	 *         retorna una lista vacía.
	 */
	List<T> get(Select select, Where<T> where, ISearchParam params);

}
