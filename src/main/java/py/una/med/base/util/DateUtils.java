/*
 * @DateUtils.java 1.0 Nov 14, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

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
	 * Determina si una fecha ocurrio antes que otra.
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
	 * Determina si una fecha ocurrio antes que otra.
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
}
