/*
 * @LabelField.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

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
	 * @param label
	 *            label para setear
	 */
	public void setLabel(String label) {

		this.label = label;
	}

}
