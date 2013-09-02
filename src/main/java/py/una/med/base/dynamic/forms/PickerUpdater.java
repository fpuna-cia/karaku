package py.una.med.base.dynamic.forms;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

@FacesComponent(value = "pickerUpdater")
@SuppressWarnings("rawtypes")
public class PickerUpdater extends UINamingContainer {

	private PickerField getPicker() {
		return (PickerField) getAttributes().get("pickerField");
	}

	private Boolean isOptional() {
		return (Boolean) getAttributes().get("optional");
	}

	@Override
	public void processUpdates(FacesContext context) {
		if (isOptional() == null || isOptional()) {
			return;
		}
		getPicker().postUpdate();
	}

	@Override
	public void processValidators(FacesContext context) {
		if (getPicker() == null) {
			return;
		}
		if (!getPicker().isNullable() && getPicker().getValue() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					getPicker().getMessage("COMPONENT_PICKER_NOT_SELECTED"),
					getPicker().getMessage("COMPONENT_PICKER_NOT_SELECTED"));
			throw new ValidatorException(msg);
		}
	}

}
