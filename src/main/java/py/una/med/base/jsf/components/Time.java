package py.una.med.base.jsf.components;

import java.util.Date;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

/**
 * FacesComponent que funciona de back end bean para el tag &lt time &gt
 *
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 *
 */
@FacesComponent(value = "customTimeBean")
@SuppressWarnings("deprecation")
public class Time extends UINamingContainer {

	private Date date;

	private Date getDate() {

		if (date == null) {

			Date d = (Date) getAttributes().get("date");
			if (d == null) {
				d = new Date();
			}
			date = d;
		}
		return date;
	}

	/**
	 * Cambia la cantidad de minutos del bean
	 *
	 * @param value
	 */

	public void setMinutes(final int value) {

		getDate().setMinutes(value);
	}

	/**
	 * cambia la cantidad de horas del bean
	 *
	 * @param value
	 */
	public void setHours(final int value) {

		getDate().setHours(value);
	}

	/**
	 * Retorna la cantidad de minutos del componente
	 *
	 * @return minutos actuales del componente
	 */
	public int getMinutes() {

		return getDate().getMinutes();
	}

	/**
	 * Retorna la cantidad de horas del componente
	 *
	 * @return horas actuales del componente
	 */
	public int getHours() {

		return getDate().getHours();
	}

	/**
	 * Configura la cantidad de segundos manejados por el componente
	 *
	 * @param value
	 */
	public void setSeconds(final int value) {

		getDate().setSeconds(value);
	}

	/**
	 * Retorna la cantidad de segundos
	 *
	 * @return seconds
	 */
	public int getSeconds() {

		return getDate().getSeconds();
	}

}
