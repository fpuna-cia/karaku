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
package py.una.pol.karaku.test.util.transaction;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define un conjunto de secuencias que deben ser creadas antes de la clase de
 * test.
 *
 *
 * <p>
 * Las secuencias son generadas antes, y se eliminan despuÃ©s de cada clase, se
 * garantiza que sean ejecutadas antes de los scripts definidos con
 * {@link SQLFiles}
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 1, 2013
 *
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sequences {

	/**
	 * Nombre de las secuencias que deben ser ejecutadas antes de cada test
	 * class.
	 *
	 *
	 * @return lista posiblemente nula o vacÃ­a de cadenas que representan los
	 *         nombres de las secuencias.
	 */
	String[] value();
}
