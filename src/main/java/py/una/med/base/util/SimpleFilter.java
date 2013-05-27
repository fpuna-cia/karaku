package py.una.med.base.util;

import javax.validation.constraints.NotNull;

/**
 * Clase que representa a un filtro simple con valor y opciones
 * 
 * @author Arturo Volpe
 * @since 2.0
 * @version 1.0
 */
public class SimpleFilter {

	private String option;

	@NotNull
	private String value;

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
	 * Limpia los filtros
	 */
	public void clear() {

		value = null;
		option = null;

	}
}
