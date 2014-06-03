/*
 * @RadioButtonField.java 1.0 Jun 03, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

/**
 * 
 * @author Diego Acuña
 * @since 1.0
 * @version 1.0 Jun 03, 2013
 * 
 */
public class RadioButtonField<T> extends ComboBoxField<T> {

	public static final String TYPE = "py.una.med.base.dynamic.forms.RadioButtonField";

	public RadioButtonField() {

		super(false);
		getBind().setConverter(null);
		
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
