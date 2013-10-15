/*
 * @DateClauses.java 1.0 Sep 20, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.util.FormatProvider;

/**
 * Factoría de {@link Clauses} para manipulación de fechas.
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 20, 2013
 *
 */
@Component
public final class DateClauses {

	/**
	 *
	 */
	private static final int MILISECOND_SECOND = 1000;

	private static final int LAST_MINUTE = 59;

	private static final int LAST_HOUR = 23;

	/**
	 * Tomando como zona horaria la paraguaya, GMT-4, son 4 horas de diferencia.
	 */
	private static final long EPOCH_MILISECONDS = 14400000;

	@Autowired
	private FormatProvider fp;

	/**
	 * Comparación de rangos de fechas.
	 *
	 * <p>
	 * Dadas dos fechas en cualquier formato (
	 * {@link FormatProvider#DATE_FORMAT} o
	 * {@link FormatProvider#DATE_SHORT_FORMAT}), retorna una cláusula que
	 * realiza la comparación de fechas.
	 *
	 * </p>
	 *
	 * @param path
	 *            ubicación del atributo
	 * @param dateOne
	 *            fecha de inicio
	 * @param dateTwo
	 *            fecha de fin
	 * @param inclusive
	 *            si el rango es inclusivo o exclusivo.
	 * @return {@link Clause} de comparación.
	 */
	public Clause between(String path, String dateOne, String dateTwo,
			boolean inclusive) {

		Date one = this.getAsDate(dateOne);
		Date two = this.getAsDate(dateTwo);

		return this.doBetween(path, one, two, inclusive);
	}

	/***
	 * Compara dos fechas (sin tener en cuenta horas y minutos),
	 *
	 * @param path
	 *            atributo para comparar
	 * @param dateOne
	 *            desde
	 * @param dateTwo
	 *            hasta
	 * @param inclusive
	 *            si se debe incluir la primera y la última fecha.
	 * @return {@link Clause} del tipo {@link And} que contiene las dos
	 *         comparaciones necesarias.
	 */
	public Clause between(String path, Date dateOne, Date dateTwo,
			boolean inclusive) {

		return this.doBetween(path, dateOne, dateTwo, inclusive);
	}

	/**
	 * Dada dos cadenas con el formato {@link FormatProvider#TIME_FORMAT},
	 * retorna todos los registros que se encuentren en el medio de ambos.
	 * <p>
	 * Notar que esto omite por completo la
	 *
	 * @param path
	 *            ubicación del atributo
	 * @param dateOne
	 *            fecha de inicio
	 * @param dateTwo
	 *            fecha de fin
	 * @param inclusive
	 *            si el rango es inclusivo o exclusivo.
	 * @return {@link Clause} de comparación.
	 */
	public Clause betweenTime(String path, String dateOne, String dateTwo,
			boolean inclusive) {

		Date one = this.getAsDate(dateOne);
		Date two = this.getAsDate(dateTwo);

		return this.doBetweenTime(path, one, two, inclusive);
	}

	/***
	 * Compara dos fechas (sin tener en cuenta días, meses y horas),
	 *
	 * @param path
	 *            atributo para comparar
	 * @param dateOne
	 *            desde
	 * @param dateTwo
	 *            hasta
	 * @param inclusive
	 *            si se debe incluir la primera y la última fecha.
	 * @return {@link Clause} del tipo {@link And} que contiene las dos
	 *         comparaciones necesarias.
	 */
	public Clause betweenTime(String path, Date dateOne, Date dateTwo,
			boolean inclusive) {

		return this.doBetweenTime(path, dateOne, dateTwo, inclusive);
	}

	/**
	 * Comparación de fechas con horas y minutos.
	 * <p>
	 * Dadas dos fechas en cualquier formato (
	 * {@link FormatProvider#DATETIME_FORMAT} o
	 * {@link FormatProvider#DATETIME_SHORT_FORMAT}), retorna una cláusula que
	 * realiza la comparación de fechas.
	 * </p>
	 *
	 * @param path
	 *            ubicación del atributo
	 * @param dateOne
	 *            fecha de inicio
	 * @param dateTwo
	 *            fecha de fin
	 * @param inclusive
	 *            si el rango es inclusivo o exclusivo.
	 * @return {@link Clause} de comparación.
	 */
	public Clause betweenDateTime(String path, String dateOne, String dateTwo,
			boolean inclusive) {

		Date one = this.getAsDate(dateOne);
		Date two = this.getAsDate(dateTwo);

		return this.doBetweenDateTime(path, one, two, inclusive);
	}

	/**
	 * Comparación de fechas con horas y minutos.
	 * <p>
	 * Dadas dos fechas en cualquier formato (
	 * {@link FormatProvider#DATETIME_FORMAT} o
	 * {@link FormatProvider#DATETIME_SHORT_FORMAT}), retorna una cláusula que
	 * realiza la comparación de fechas.
	 * </p>
	 *
	 * @param path
	 *            ubicación del atributo
	 * @param one
	 *            fecha de inicio
	 * @param two
	 *            fecha de fin
	 * @param inclusive
	 *            si el rango es inclusivo o exclusivo.
	 * @return {@link Clause} de comparación.
	 */
	public Clause betweenDateTime(String path, Date one, Date two,
			boolean inclusive) {

		return this.doBetweenDateTime(path, one, two, inclusive);
	}

	private Clause doBetween(String path, Date one, Date two, boolean inclusive) {

		Date from;
		Date to;
		if (inclusive) {
			from = this.setTimeToBegin(one);
			to = this.setTimeToEnd(two);
		} else {
			from = this.setTimeToEnd(one);
			to = this.setTimeToBegin(two);
			from = this.nextInstant(from);
			to = this.previousInstant(to);
		}
		return Clauses.between(path, from, to);
	}

	private Clause doBetweenTime(final String path, final Date one,
			final Date two, boolean inclusive) {

		Date first = getOnlyHourAndMinutes(one);
		Date last = getOnlyHourAndMinutes(two);

		if (!inclusive) {
			first = nextInstant(first);
			last = previousInstant(last);
		}
		return Clauses.between(path, first, last);
	}

	private Clause doBetweenDateTime(final String path, final Date one,
			final Date two, boolean inclusive) {

		Date first = truncateSecondsAndMinutes(one);
		Date last = truncateSecondsAndMinutes(two);

		if (!inclusive) {
			first = nextInstant(first);
			last = previousInstant(last);
		}
		return Clauses.between(path, first, last);
	}

	private Date truncateSecondsAndMinutes(Date date) {

		Calendar c = getCalendar(date);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * Retorna un {@link Date} que solo contiene la hora y los minutos,
	 * eliminado todos los demas.
	 *
	 * @param date
	 * @return
	 */
	private Date getOnlyHourAndMinutes(Date date) {

		return copy(getCalendar(date), getEpoch(), Calendar.HOUR_OF_DAY,
				Calendar.MINUTE).getTime();
	}

	private Date getAsDate(final String date) {

		try {
			if (this.fp.isDate(date)) {
				return this.fp.parseDate(date);
			} else {
				return this.fp.parseShortDate(date);
			}
		} catch (ParseException e) {
			throw new KarakuRuntimeException("Can't parse: " + date, e);
		}
	}

	private Date nextInstant(Date date) {

		return addMinute(date, 1);
	}

	private Date previousInstant(Date date) {

		return addMinute(date, -1);
	}

	private Date addMinute(Date date, int minutes) {

		date.setTime(date.getTime() + (MILISECOND_SECOND * minutes));
		return date;
	}

	/**
	 * Hace que una fecha tenga el ultimo minuto posible 23:59
	 *
	 * @param d
	 * @return
	 */
	private Date setTimeToEnd(Date d) {

		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MINUTE, LAST_MINUTE);
		c.set(Calendar.HOUR_OF_DAY, LAST_HOUR);
		return c.getTime();
	}

	/**
	 * Hace que una fecha tenga el primer minuto posible 0:00
	 *
	 * @param d
	 * @return
	 */
	private Date setTimeToBegin(Date d) {

		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		return c.getTime();
	}

	private Calendar getEpoch() {

		Calendar epoch = Calendar.getInstance();
		epoch.setTimeInMillis(EPOCH_MILISECONDS);
		// epoch.set(Calendar.YEAR, 1970);
		// epoch.set(Calendar.DAY_OF_YEAR, 1);
		return epoch;
	}

	private Calendar copy(Calendar from, Calendar to, int ... fields) {

		for (int i : fields) {
			to.set(i, from.get(i));
		}
		return to;
	}

	private Calendar getCalendar(Date date) {

		Calendar first = Calendar.getInstance();
		first.setTime(date);
		return first;
	}
}
