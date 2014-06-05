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
package py.una.pol.karaku.util;

import java.util.Calendar;
import java.util.Date;
import javax.annotation.Nullable;

/**
 * Provee funcionalidades básicas para manipulación de {@link Date} y
 * {@link Calendar}.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 14, 2013
 * 
 */
public final class DateUtils {

	private DateUtils() {

	}

	/**
	 * Retorna una copia del objeto.
	 * 
	 * @param date
	 *            fecha a copiar
	 * @return <code>null</code> si la fecha es nula, una copia de la misma en
	 *         caso contrario.
	 */
	public static Date cloneDate(Date date) {

		if (date == null) {
			return null;
		}

		return (Date) date.clone();
	}

	/**
	 * Retorna una copia del objeto.
	 * 
	 * @param calendar
	 *            fecha a copiar
	 * @return <code>null</code> si la fecha es nula, una copia de la misma en
	 *         caso contrario.
	 */
	public static Calendar cloneCalendar(Calendar calendar) {

		if (calendar == null) {
			return null;
		}

		return (Calendar) calendar.clone();
	}

	/**
	 * Determina si una fecha ocurrio antes que otra o si ambas son iguales.
	 * 
	 * <ol>
	 * <li>Si ambas fechas son <code>null</code> o iguales, entonces retorna
	 * <code>true</code></li>
	 * <li>Si la fecha <code>before</code> es <code>null</code>, retorna
	 * <code>false</code></li>
	 * <li>Si la fecha <code>after</code> es <code>null</code> retorna
	 * <code>true</code></li>
	 * <li>Si <code>before</code> ocurrió antes que <code>after</code> retorna
	 * <code>true</code></li>
	 * </ol>
	 * 
	 * 
	 * @param before
	 *            primera fecha, nulable.
	 * @param after
	 *            fecha después, nulable
	 * @return condiciones especificadas arriba.
	 */
	public static boolean isBeforeOrEqual(@Nullable Date before,
			@Nullable Date after) {

		if ((before == null) && (after == null)) {
			return true;
		}
		if (before == null) {
			return false;
		}

		if (after == null) {
			return true;
		}
		return before.before(after) || before.equals(after);
	}

	/**
	 * Determina si una fecha es anterior a otra.
	 * 
	 * <ol>
	 * <li>Si ambas fechas son <code>null</code> o iguales entonces retorna
	 * <code>false</code></li>
	 * <li>Si la fecha <code>before</code> es <code>null</code>, retorna
	 * <code>false</code></li>
	 * <li>Si la fecha <code>after</code> es <code>null</code> retorna
	 * <code>true</code></li>
	 * <li>Si <code>before</code> ocurrió antes que <code>after</code> retorna
	 * <code>true</code></li>
	 * 
	 * 
	 * @param before
	 *            primera fecha, nulable.
	 * @param after
	 *            fecha después, nulable
	 * @return condiciones especificadas arriba.
	 */
	public static boolean isBefore(@Nullable Date before, @Nullable Date after) {

		if ((before == null) && (after == null)) {
			return false;
		}
		if (before == null) {
			return false;
		}

		if (after == null) {
			return true;
		}
		return before.before(after);
	}

	/**
	 * Determina si una fecha ocurrio despúes que otra o si ambas son iguales.
	 * 
	 * <ol>
	 * <li>Si ambas fechas son <code>null</code> o iguales, entonces retorna
	 * <code>true</code></li>
	 * <li>Si la fecha <code>before</code> es <code>null</code>, retorna
	 * <code>true</code></li>
	 * <li>Si la fecha <code>after</code> es <code>null</code> retorna
	 * <code>false</code></li>
	 * <li>Si <code>after</code> ocurrió después que <code>before</code> retorna
	 * <code>true</code></li>
	 * </ol>
	 * 
	 * 
	 * @param before
	 *            primera fecha, nulable.
	 * @param after
	 *            fecha después, nulable
	 * @return condiciones especificadas arriba.
	 */
	public static boolean isAfterOrEqual(@Nullable Date before,
			@Nullable Date after) {

		if ((before == null) && (after == null)) {
			return true;
		}
		if (before == null) {
			return true;
		}

		if (after == null) {
			return false;
		}
		return before.before(after) || before.equals(after);
	}

	/**
	 * Limpia la fecha recibida como parámetro, esto es sin horas, minutos,
	 * segundos ni milisegundos.
	 * <ol>
	 * <li>Si la fecha es<code>null</code>, entonces retorna <code>null</code></li>
	 * <li>Si la fecha no es <code>null</code>, retorna
	 * <code>la fecha sin horas, minutos, segundos ni milisegundos.</code></li>
	 * 
	 * </ol>
	 * 
	 * @param date
	 * @return condiciones especificadas arriba.
	 */
	public static Date clearDate(Date date) {

		if (date == null) {
			return null;
		}
		Calendar nuevo = Calendar.getInstance();
		nuevo.setTime(date);
		nuevo.set(Calendar.MILLISECOND, 0);
		nuevo.set(Calendar.SECOND, 0);
		nuevo.set(Calendar.MINUTE, 0);
		nuevo.set(Calendar.HOUR_OF_DAY, 0);
		return nuevo.getTime();
	}

}
