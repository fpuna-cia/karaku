/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Polit閏nica, Universidad Nacional de Asunci髇.
 * 		2012-2014, Facultad de Ciencias M閐icas, Universidad Nacional de Asunci髇.
 * 		2012-2013, Centro Nacional de Computaci髇, Universidad Nacional de Asunci髇.
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import py.una.pol.karaku.exception.KarakuRuntimeException;

/**
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 11, 2013
 * @see DatabasePopulatorExecutionListener
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SQLFiles {

	public static final String DEFAULT = "default";

	/**
	 * En el caso de que no se quiere ejecutar ning煤n archivo, establecer esta
	 * cadena, como valor del atributo {@link #value()}.
	 */
	public static final String NONE = "none";

	/**
	 * Ubicaci贸n de los archivos sql para cargar.
	 * <p>
	 * Si el archivo no puede ser cargardo se busca en la ubicaci贸n del test. <br />
	 * La extensi贸n <code>.sql</code> puede ser omitida. <br />
	 * Los valores inv谩lidos (y repetidos) lanzaran una excepci贸n del tipo
	 * {@link KarakuRuntimeException}
	 *
	 * </p>
	 *
	 * <p>
	 * <b>En caso de que esta anotaci贸n est茅 en el scope de una clase</b>, una
	 * cadena con que sea igual a {@link #DEFAULT} o a <code>""</code>, ser谩
	 * interpretada como la ubicaci贸n por defecto. La ubicaci贸n por defecto es
	 * la misma donde se encuentra el test, con un archivo con el mismo nombre
	 * que el test, cambiando <code>java</code> por <code>sql</code>. <br>
	 * <b>Por ejemplo</b>
	 *
	 * <pre>
	 * 	py.una.med.sistema.test.casos.Test350.java
	 * Tendr谩 por archivo por defecto
	 * 	py/una/med/sistema/test/casos/Test350.sql
	 * </pre>
	 *
	 * </p>
	 *
	 * @return {@link String[]} con la ubicaci贸n de los archivos
	 */
	String[] value() default { DEFAULT };
}
