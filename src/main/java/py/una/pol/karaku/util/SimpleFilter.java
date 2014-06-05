/*
 * 
 */
package py.una.pol.karaku.util;

import javax.validation.constraints.NotNull;

/**
 * Clase que representa a un filtro simple con valor y opciones
 * 
 * @author Arturo Volpe
 * @since 2.0
 * @version 1.0
 */
public class SimpleFilter {

	/**
	 * Interfaz que se utiliza para capturar los eventos de cambios que sufre la
	 * búsqueda simple.
	 * 
	 * @author Nathalia Ochoa
	 * @since 1.0
	 * @version 1.0 Feb 21, 2014
	 * 
	 */
	public interface ChangeListenerSimpleFilter {

		/**
		 * Método invocado cada vez que se presiona el botón buscar de la
		 * búsqueda simple.
		 * 
		 * @param thizz
		 *            simple filter
		 * @param value
		 * @param option
		 */
		void onChange(SimpleFilter thizz, String value, String option);
	}

	private String option;

	@NotNull
	private String value;

	private ChangeListenerSimpleFilter changeListener;

	public String getOption() {

		return option;
	}

	public void setOption(String option) {

		this.option = option;
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {

		this.value = value;
	}

	/**
	 * @param changeListener
	 *            changeListener para setear
	 */
	public void setChangeListener(ChangeListenerSimpleFilter changeListener) {

		this.changeListener = changeListener;
	}

	public void changeValueListener() {

		if (changeListener == null) {
			return;
		}
		changeListener.onChange(this, value, option);
	}

	/**
	 * Limpia los filtros
	 */
	public void clear() {

		value = null;
		option = null;
		changeValueListener();

	}

}
