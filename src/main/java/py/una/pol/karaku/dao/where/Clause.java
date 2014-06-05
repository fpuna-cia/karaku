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
package py.una.pol.karaku.dao.where;

import org.hibernate.criterion.Criterion;

/**
 * Interfaz base para todas las cláusulas soportadas por Karaku, una cláusula
 * puede ser vista como una condición SQL.
 * <p>
 * Si bien, la mayoría de las {@link Clause} se pueden instanciar normalmente,
 * existe una factoría para las mismas que facilita el trabajo, la misma es
 * {@link Clauses}
 * </p>
 * <p>
 * Esta interfaz es una copia de {@link Criterion} de hibernate, a fin de
 * mantener la independencia de la base de datos.
 * </p>
 * <h3>
 * Proceso de desarrollo de nuevas {@link Clause}</h3>
 * <ol>
 * <li>Crear una clase que implemente {@link Clause}, el método
 * {@link #getCriterion()} puede retornar <code>null</code>.</li>
 * <li>Crear un {@link BaseClauseHelper} con la anotación {@link Component},
 * para que sea automáticamente detectado. En la clase se debe implementar el
 * método abstracto
 * {@link BaseClauseHelper#getCriterion(org.hibernate.Criteria, Clause, java.util.Map)}
 * . Los métodos auxiliares
 * {@link BaseClauseHelper#configureAlias(org.hibernate.Criteria, String, java.util.Map)}
 * ayudan a generar automáticamente una cadena de alias y joins para búsquedas
 * anidadas. Generalmente una vez construido el Alias se utiliza la factoría
 * {@link Restrictions} para obtener una cláusula para la sesión.</li>
 * </ol>
 * 
 * 
 * <h3>Funcionamiento</h3>
 * <p>
 * Existe un componente denominado {@link RestrictionHelper} que se encarga de
 * buscar todos los {@link Component} que hereden de {@link BaseClauseHelper}, y
 * los indexa. Luego cada vez que es necesario procesar una {@link Clause} busca
 * su correspondiente helper y invoca al método
 * {@link BaseClauseHelper#getCriterion(org.hibernate.Criteria, Clause, java.util.Map)}
 * , y agrega al {@link Criterion}, se puede notar que la clase
 * {@link RestrictionHelper} puede ser inyectada dentro de cada
 * {@link BaseClauseHelper} para realizar conversiones complejas o recursivas,
 * como es el caso de la clausula {@link And} y {@link Or}.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 23, 2013
 * 
 */
public interface Clause {

	/**
	 * Opción por defecto en caso de no encontrarse un helper.
	 * 
	 * @return criterion para agregar a la consulta
	 * @deprecated No es necesario implementar esto
	 */
	@Deprecated
	Criterion getCriterion();

}
