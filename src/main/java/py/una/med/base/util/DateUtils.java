/*
 * @DateUtils.java 1.0 Nov 14, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.util.Calendar;
import java.util.Date;

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
}
