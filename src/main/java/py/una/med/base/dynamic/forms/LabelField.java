/*
 * @LabelField.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import py.una.med.base.util.I18nHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public abstract class LabelField extends Field {

	private String label;

	/**
	 * @return label
	 */
	public String getLabel() {

		return label;
	}

	/**
	 * Busca en el archivo de internacionalización y luego asigna el valor a la
	 * etiqueta de este {@link Field}
	 * 
	 * @param label
	 *            key del archivo de internacionalización.
	 */
	public void setLabel(String label) {

		this.label = (I18nHelper.getMessage(label));
	}

	/**
	 * A diferencia de {@link #setLabel(String)} no busca en el archivo de
	 * bundles, útil para cuando se buscan cadenas personalizadas.
	 * 
	 * @param label
	 */
	public void setLabelEscaped(String label) {

		this.label = label;
	}
}
