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
package py.una.pol.karaku.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Anotación que sirve para indicar que se debe inyectar un
 * {@link org.slf4j.Logger}.
 * <p>
 * La utilización debe ser como sigue:
 *
 * <pre>
 * {@literal @}{@link Log}
 * Logger log;
 * </pre>
 *
 * De esta forma, el {@link LogPostProcessor} se encargará automáticamente de
 * inyectar el {@link org.slf4j.Logger} (a través del
 * {@link org.slf4j.LoggerFactory}) pertinente al atributo.
 * </p>
 * <p>
 * Esto produce el mismo resultado que:
 *
 * <pre>
 * Logger log = LoggerFactory.getLogger("py.una.clase")
 * </pre>
 *
 * </p>
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * @see LogPostProcessor
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface Log {

	/**
	 * Nombre del log, si es <code>null</code>, entonces se utilizará un
	 * {@link org.slf4j.Logger} con el nombre del bean.
	 *
	 * @return cadena que representa el nombre del {@link org.slf4j.Logger},
	 *         <code>""</code> es interpretado como <code>null</code>.
	 */
	String name() default "";
}
