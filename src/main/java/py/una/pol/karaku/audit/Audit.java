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
package py.una.pol.karaku.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación que permite marcar un método para ser auditado automáticamente
 * mediante AOP.
 * 
 * @author Arturo Volpe
 * @author Romina Fernandez
 * @since 1.0
 * @version 1.1
 * @see Auditor encargado de interceptar el método
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

	/**
	 * Representa todo aquello que formando parte del bean actual, será
	 * auditado. Como son expresiones de EL, no pueden ser métodos ni atributos
	 * privados.
	 * <p>
	 * 
	 * </p>
	 * 
	 * @return Vector de valores de la clase que serán auditadas, ejemplo {
	 *         "filterOption", "bean.pais" }
	 */
	String[] toAudit() default {};

	/**
	 * Representa todo aquello que forma parte de un parámetro y desea ser
	 * auditado, los parámetros se enumeran de 1 a N.
	 * 
	 * 
	 * @return Vector de valores que pertenecen al parámetro, ejemplo {
	 *         "{1}.id", "{2}" }
	 */
	String[] paramsToAudit() default {};

}
