/*
 * 
 */
package py.una.pol.karaku.dynamic.forms;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * {@link FacesComponent} que se encarga de realizar los controles necesarios
 * para el {@link PickerField}. Mediante los métodos
 * {@link #processUpdates(FacesContext)} y
 * {@link #processValidators(FacesContext)}
 * <p>
 * Utiliza un componente auxiliar para realizar la verificación de que si es o
 * no requerido. Este componente tiene como id <code>required</code> y si esta
 * presente, entonces el picker es necesario.
 * </p>
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 4, 2013
 * 
 */
@FacesComponent(value = "pickerUpdater")
@SuppressWarnings("rawtypes")
public class PickerUpdater extends UINamingContainer {

	private PickerField getPicker() {

		return (PickerField) getAttributes().get("pickerField");
	}

	/**
	 * Determina si el picker al que corresponde es o no requerido.
	 * 
	 * @return <code>true</code> si no admite valores nulos, <code>false</code>
	 *         si los permite.
	 */
	public Boolean isRequired() {

		UIComponent ui = findComponent("required");
		return ui != null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Relega el trabajo de actualizar a {@link PickerField#postUpdate()}.
	 * </p>
	 */
	@Override
	public void processUpdates(FacesContext context) {

		pushComponentToEL(context, this.getParent());
		getPicker().postUpdate();
		popComponentFromEL(context);
	}

	@Override
	public void processValidators(FacesContext context) {

		if (getPicker() == null) {
			return;
		}
		if (!getPicker().isNullable() && getPicker().getValue() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"", getPicker().getMessage("COMPONENT_PICKER_NOT_SELECTED"));
			String clientId = getPicker().findComponent(getPicker().getId())
					.getClientId(context);
			context.addMessage(clientId, msg);
			context.validationFailed();
			context.renderResponse();
		}
	}

}
