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
package py.una.pol.karaku.dao.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Representa un atributo dado del filtro, por ejemplo el filter
 * 
 * <pre>
 * {@code Select p from
 * Pais p where p.nombre :nombre}
 * </pre>
 * 
 * Se setea como {@link FilterAttribute#name()} 'nombre' y en el
 * {@link FilterAttribute#path()} la expresión del lenguaje natural que
 * represente el valor a insertar en el parámetro
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterAttribute {

	/**
	 * Nombre del atributo del filter
	 * 
	 * @see Filter
	 */
	String name();

	/**
	 * Path en Expression Language que será evaluada, el resultado de esta
	 * expresión será insertado como parámetro, notar que para pasar valores de
	 * un parámetro se utiliza la nomenclatura {0} para el primer parámetro.
	 * 
	 * @return
	 */
	String path();
}
