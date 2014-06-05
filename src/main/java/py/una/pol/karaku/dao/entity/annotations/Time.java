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
package py.una.pol.karaku.dao.entity.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define el formato de una fecha.
 *
 * <p>
 * Esta anotación sirve para que el DAO automáticamente elimine los atributos de
 * un {@link java.util.Date} que no se desean persistir.
 * <p>
 *
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FieldAnnotation
public @interface Time {

	/**
	 * Define el tipo de este atributo, por defecto es {@link Type#DATE}.
	 *
	 * @return {@link Type} del atributo.
	 */
	Type type() default Type.DATE;

	/**
	 * Tipos de fechas soportados.
	 *
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Oct 7, 2013
	 *
	 */
	static enum Type {
		/**
		 * Fecha sin horas ni minutos.
		 * <p>
		 * {@link py.una.pol.karaku.util.FormatProvider#DATE_FORMAT} es el formato
		 * por defecto de este tipo de atributos
		 * </p>
		 */
		DATE,
		/**
		 * Horas y minutos.
		 * <p>
		 * Se eliminan el dia, el mes, el año, segundos y milisegundos
		 * </p>
		 * <p>
		 * {@link py.una.pol.karaku.util.FormatProvider#TIME_FORMAT} es el formato
		 * por defecto de este tipo de atributos
		 * </p>
		 */
		TIME,
		/**
		 * Fechas con horas y minutos.
		 * <p>
		 * Se eliminan los segundos y milisegundos
		 * </p>
		 * <p>
		 * {@link py.una.pol.karaku.util.FormatProvider#DATETIME_FORMAT} es el
		 * formato por defecto de este tipo de atributos
		 * </p>
		 */
		DATETIME
	}
}
