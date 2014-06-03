/*
 * @DateProvider.java 1.0 Nov 14, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.util;

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * Componente que sirve para proveer fechas de Karaku.
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 14, 2013
 *
 */
@Component
public class DateProvider {

	/**
	 * Retorna la fecha actual.
	 *
	 * @return fecha actual
	 */
	public Date getNow() {

		return getNowCalendar().getTime();
	}

	/**
	 * Retorna la fecha actual en forma de un {@link Calendar}.
	 *
	 * @return fecha actual.
	 */
	public Calendar getNowCalendar() {

		return Calendar.getInstance();
	}
}
