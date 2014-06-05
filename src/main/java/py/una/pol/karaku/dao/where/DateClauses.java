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
package py.una.pol.karaku.dao.where;

import static py.una.pol.karaku.util.Checker.notNull;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.util.FormatProvider;

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

	/**
	 * Construye la cláusula SQL menor que.
	 * 
	 * @param path
	 *            Ubicación del atributo.
	 * @param date
	 *            Fecha a comparar.
	 * @return
	 */
	public Clause lt(String path, Date date) {

		return this.doLt(path, date);
	}

	/**
	 * Construye la cláusula SQL menor o igual que.
	 * 
	 * @param path
	 *            Ubicación del atributo.
	 * @param date
	 *            Fecha a comparar.
	 * @return
	 */
	public Clause le(String path, Date date) {

		return this.doLe(path, date);
	}

	/**
	 * Construye la cláusula SQL mayor que.
	 * 
	 * @param path
	 *            Ubicación del atributo.
	 * @param date
	 *            Fecha a comparar.
	 * @return
	 */
	public Clause gt(String path, Date date) {

		return this.doGt(path, date);
	}

	/**
	 * Construye la cláusula SQL mayor o igual que.
	 * 
	 * @param path
	 *            Ubicación del atributo.
	 * @param date
	 *            Fecha a comparar.
	 * @return
	 */
	public Clause ge(String path, Date date) {

		return this.doGe(path, date);
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
	public Clause betweenTime(@Nonnull String path, @Nonnull String dateOne,
			@Nonnull String dateTwo, boolean inclusive) {

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
	public Clause betweenTime(@Nonnull String path, @Nonnull Date dateOne,
			@Nonnull Date dateTwo, boolean inclusive) {

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
	public Clause betweenDateTime(@Nonnull String path,
			@Nonnull String dateOne, @Nonnull String dateTwo, boolean inclusive) {

		Date one = notNull(this.getAsDate(dateOne));
		Date two = notNull(this.getAsDate(dateTwo));

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
	public Clause betweenDateTime(@Nonnull String path, @Nonnull Date one,
			@Nonnull Date two, boolean inclusive) {

		return this.doBetweenDateTime(path, one, two, inclusive);
	}

	private Clause doBetween(@Nonnull String path, @Nonnull Date one,
			@Nonnull Date two, boolean inclusive) {

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

	private Clause doBetweenTime(@Nonnull final String path,
			@Nonnull final Date one, @Nonnull final Date two, boolean inclusive) {

		Date first = this.getOnlyHourAndMinutes(one);
		Date last = this.getOnlyHourAndMinutes(two);

		if (!inclusive) {
			first = this.nextInstant(first);
			last = this.previousInstant(last);
		}
		return Clauses.between(path, first, last);
	}

	private Clause doBetweenDateTime(@Nonnull final String path,
			@Nonnull final Date one, @Nonnull final Date two, boolean inclusive) {

		Date first = this.truncateSecondsAndMinutes(one);
		Date last = this.truncateSecondsAndMinutes(two);

		if (!inclusive) {
			first = this.nextInstant(first);
			last = this.previousInstant(last);
		}
		return Clauses.between(path, first, last);
	}

	private Clause doLt(@Nonnull String path, @Nonnull Date date) {

		Date end = this.setTimeToEnd(date);
		end = this.nextInstant(end);
		return Clauses.lt(path, end);
	}

	private Clause doLe(@Nonnull String path, @Nonnull Date date) {

		Date begin = this.setTimeToBegin(date);
		return Clauses.le(path, begin);
	}

	private Clause doGt(@Nonnull String path, @Nonnull Date date) {

		Date end = setTimeToEnd(date);
		end = nextInstant(date);

		return Clauses.gt(path, end);
	}

	private Clause doGe(@Nonnull String path, @Nonnull Date date) {

		Date begin = this.setTimeToBegin(date);
		return Clauses.ge(path, begin);
	}

	@Nonnull
	private Date truncateSecondsAndMinutes(Date date) {

		Calendar c = this.getCalendar(date);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return notNull(c.getTime());
	}

	/**
	 * Retorna un {@link Date} que solo contiene la hora y los minutos,
	 * eliminado todos los demas.
	 * 
	 * @param date
	 * @return
	 */
	@Nonnull
	private Date getOnlyHourAndMinutes(Date date) {

		return notNull(this.copy(this.getCalendar(date), this.getEpoch(),
				Calendar.HOUR_OF_DAY, Calendar.MINUTE).getTime());
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

	@Nonnull
	private Date nextInstant(@Nonnull Date date) {

		return this.addMinute(date, 1);
	}

	@Nonnull
	private Date previousInstant(@Nonnull Date date) {

		return this.addMinute(date, -1);
	}

	@Nonnull
	private Date addMinute(@Nonnull Date date, int minutes) {

		date.setTime(date.getTime() + (MILISECOND_SECOND * minutes));
		return date;
	}

	/**
	 * Hace que una fecha tenga el ultimo minuto posible 23:59
	 * 
	 * @param d
	 * @return
	 */
	@Nonnull
	private Date setTimeToEnd(Date d) {

		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MINUTE, LAST_MINUTE);
		c.set(Calendar.HOUR_OF_DAY, LAST_HOUR);
		return notNull(c.getTime());
	}

	/**
	 * Hace que una fecha tenga el primer minuto posible 0:00
	 * 
	 * @param d
	 * @return
	 */
	@Nonnull
	private Date setTimeToBegin(Date d) {

		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		return notNull(c.getTime());
	}

	@Nonnull
	private Calendar getEpoch() {

		Calendar epoch = Calendar.getInstance();
		epoch.setTimeInMillis(EPOCH_MILISECONDS);
		return epoch;
	}

	@Nonnull
	private Calendar copy(@Nonnull Calendar from, @Nonnull Calendar to,
			int ... fields) {

		for (int i : fields) {
			to.set(i, from.get(i));
		}
		return to;
	}

	@Nonnull
	private Calendar getCalendar(Date date) {

		Calendar first = Calendar.getInstance();
		first.setTime(date);
		return first;
	}
}
