/*
 * @DateClauses.java 1.0 Sep 20, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.log.Log;
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

	@Autowired
	private FormatProvider fp;

	@Log
	private Logger logger;

	/**
	 * Dadas dos fechas en cualquier formato (
	 * {@link FormatProvider#DATE_FORMAT} o
	 * {@link FormatProvider#DATE_SHORT_FORMAT}), retorna una cláusula que
	 * realiza la comparación de fechas.
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

		Date one = this._asDate(dateOne);
		Date two = this._asDate(dateTwo);

		return this._between(path, one, two, inclusive);
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

		return this._between(path, dateOne, dateTwo, inclusive);
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

		Date one = this._asDate(dateOne);
		Date two = this._asDate(dateTwo);

		return this._between(path, one, two, inclusive);
	}

	/**
	 * Dadas dos fechas en cualquier formato (
	 * {@link FormatProvider#DATETIME_FORMAT} o
	 * {@link FormatProvider#DATETIME_SHORT_FORMAT}), retorna una cláusula que
	 * realiza la comparación de fechas.
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

		Date one = this._asDate(dateOne);
		Date two = this._asDate(dateTwo);

		return this._between(path, one, two, inclusive);
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

		return this._between(path, dateOne, dateTwo, inclusive);
	}

	private Clause _between(String path, Date one, Date two, boolean inclusive) {

		if (inclusive) {
			one = this.setTimeToBegin(one);
			two = this.setTimeToEnd(two);
		} else {
			one = this.setTimeToEnd(one);
			two = this.setTimeToBegin(two);
			one = this.addMinute(one, 1);
			two = this.addMinute(two, -1);
		}
		return Clauses.between(path, one, two);
	}

	private Date _asDate(final String date) {

		try {
			if (this.fp.isDate(date)) {
				return this.fp.parseDate(date);
			} else {
				return this.fp.parseShortDate(date);
			}
		} catch (ParseException e) {
			this.logger.info("Can't parse: {}", date);
			throw new KarakuRuntimeException("Can't parse: " + date);
		}
	}

	private Date addMinute(Date date, int minutes) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);
		return c.getTime();
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
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.HOUR_OF_DAY, 23);
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
}
