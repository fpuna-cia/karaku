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
package py.una.pol.karaku.dao.entity.interceptors;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import py.una.pol.karaku.dao.entity.Operation;

/**
 * Interfaz que define un EntityInterceptor
 * 
 * <p>
 * Las implementaciones de esta interfaz realizan acciones sobre la entidad
 * antes de ser persistida.
 * </p>
 * <p>
 * Las clases que la implementan deben tener la anotación {@literal @}
 * {@link org.springframework.stereotype.Component}
 * </p>
 * 
 * @see AbstractInterceptor
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 7, 2013
 * 
 */
public interface Interceptor {

	/**
	 * Lista de tipos observados.
	 * 
	 * <p>
	 * Si el vector que retorna, retorna {@link void}.class, entonces escuchara
	 * a cualquier tipo.
	 * 
	 * </p>
	 * 
	 * @return vector de clases que definen los tipos observados.
	 */
	Class<?>[] getObservedTypes();

	/**
	 * Lista de anotaciones escuchadas
	 * 
	 * <p>
	 * Si el vector que retorna, retorna {@link void}.class, entonces escuchara
	 * a cualquier anotación.
	 * </p>
	 * 
	 * @return vector de clases que definen las anotaciones observados.
	 */
	Class<?>[] getObservedAnnotations();

	/**
	 * Intercepta un atributo para aplicarle la lógica deseada.
	 * 
	 * <p>
	 * En este punto, el {@link Field} es accesible y se pueden realizar todas
	 * las tareas necesarias, ademas se asegura que:
	 * <ol>
	 * <li>El Field es accesible</li>
	 * <li>El Field es del tipo de alguna de las clases retornadas por
	 * {@link #getObservedTypes()}</li>
	 * <li>El Field tiene alguna de las anotaciones retornadas en
	 * {@link #getObservedAnnotations()}</li>
	 * <li>Cualquier cambio que se realize será validado para asegurar su
	 * cumplimiento con las anotaciones de la entidad</li>
	 * <li>Una excepción no controlada lanzada en este punto interrumpirá la
	 * creación/actualización del bean</li>
	 * <li>El Field no es estático, final, transient (es decir es accesible)</li>
	 * <li>El Field no tiene la anotación {@link javax.persistence.Transient},
	 * final, transient (es decir es accesible)</li>
	 * </ol>
	 * </p>
	 * 
	 * @param field
	 *            campo actualmente procesado
	 * @param bean
	 *            bean a guardar
	 * @param operation
	 *            tipo de operación.
	 * @see Operation
	 */
	void intercept(@Nonnull Operation operation, @Nonnull Field field,
			@Nonnull Object bean);

	/**
	 * Define si un atributo debe ser interceptado.
	 * 
	 * <p>
	 * Sirve como un paso previo a {@link #intercept(Field, Object)}, y define
	 * sin un atributo debe ser o no interceptado, por ejemplo se puede
	 * controlar que tenga otra anotación, o el nombre del mismo.
	 * </p>
	 * <p>
	 * No se debe verificar que:
	 * <ol>
	 * <li>Sea final</li>
	 * <li>Sea transient</li>
	 * <li>Sea estático</li>
	 * <li>Tenga la anotación Transient</li>
	 * <li>Que sea <code>null</code></li>
	 * </ol>
	 * Esto es por que estas características son necesarias para que un
	 * {@link Field} llege a este punto.
	 * </p>
	 * 
	 * @param field
	 *            atributo a interceptar
	 * @param bean
	 *            bean actual del objeto
	 * @param operation
	 *            tipo de operación.
	 * @return <code>true</code> si se desea interceptar, <code>false</code> si
	 *         no cumple las precondiciones de la lógica.
	 * @see Operation
	 */
	boolean interceptable(@Nonnull Operation op, @Nonnull Field field,
			@Nonnull Object bean);

}
