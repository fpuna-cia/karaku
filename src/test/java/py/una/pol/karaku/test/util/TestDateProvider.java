/*
 * @TestDateProvider.java 1.0 Nov 14, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.util;

import java.util.Calendar;
import java.util.Date;
import py.una.pol.karaku.util.DateProvider;

/**
 * Componente que provee fechas con enfoque de Test.
 *
 * Es decir, el tiempo no avanza, se debe avanzar manualmente con los métodos
 * {@link #forward(int)} y {@link #backward(int)}.
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 14, 2013
 *
 */
public class TestDateProvider extends DateProvider {

	Calendar now = Calendar.getInstance();

	@Override
	public Date getNow() {

		return now.getTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Calendar getNowCalendar() {

		return (Calendar) now.clone();
	}

	/**
	 * Adelanta el tiempo en una cantidad definida.
	 *
	 * @param minutes
	 *            cantidad de minutos para avanzar.
	 */
	public void forward(int minutes) {

		now.add(Calendar.MINUTE, minutes);
	}

	/**
	 * Retrocede en el tiempo en una cantidad definida.
	 *
	 * @param minutes
	 *            cantidad de minutos para retroceder.
	 */
	public void backward(int minutes) {

		now.add(Calendar.MINUTE, -minutes);
	}
}
