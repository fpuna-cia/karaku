/*
 * @MultiplePickerField.java 1.0 Jun 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.List;
import javax.faces.event.ValueChangeListener;

/**
 * 
 * @author Jorge Ramírez
 * @since 1.0
 * @version 1.0 Jun 25, 2013
 * 
 */
public class MultiplePickerField<T> extends ListSelectField<T> {

	public interface ItemKeyProvider<T> {

		Object getKeyValue(T item);
	}

	public interface PickerValidator<T> {

		boolean validate(Field source, List<T> values);
	}

	private String popupID;
	private boolean buttonDisabled;
	private ItemKeyProvider<T> itemKeyProvider;
	private PickerValidator<T> pickerValidator;

	public MultiplePickerField() {

		super();
		popupID = getId() + "popup";
		setDataTableID(popupID + "datatable");
	}

	@Override
	public String getType() {

		return "py.una.med.base.dynamic.forms.MultiplePickerField";
	}

	public String getPopUpClientID() {

		return findComponent(getPopupID()).getClientId();
	}

	@Override
	public boolean disable() {

		setButtonDisabled(true);
		return true;
	}

	@Override
	public boolean enable() {

		setButtonDisabled(false);
		return true;
	}

	public String getPopupID() {

		return popupID;
	}

	public void setPopupID(String id) {

		popupID = id;
	}

	public boolean isButtonDisabled() {

		return buttonDisabled;
	}

	public void setButtonDisabled(boolean buttonDisabled) {

		this.buttonDisabled = buttonDisabled;
	}

	/**
	 * Se encarga de llamar al metodo {@link ValueChangeListener#onChange }
	 * cuando existe un cambio en el valor asociado al {@link PickerField}
	 **/
	@Override
	public void changeValueListener() {

		super.changeValueListener();
		performValidation();
	}

	public void performValidation() {

		if (pickerValidator == null) {
			return;
		}
		pickerValidator.validate(this, getValues());
	}

	public void setPickerValidator(PickerValidator<T> validator) {

		if (validator == null) {
			throw new IllegalArgumentException("validator no puede ser null");
		}
		pickerValidator = validator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getItemKey(Object item) {

		if (getItemKeyProvider() == null) {
			throw new IllegalArgumentException(
					"itemKeyProvider no puede ser null");
		}
		return itemKeyProvider.getKeyValue((T) item);
	}

	public ItemKeyProvider<T> getItemKeyProvider() {

		return itemKeyProvider;
	}

	public void setItemKeyProvider(ItemKeyProvider<T> itemKeyProvider) {

		this.itemKeyProvider = itemKeyProvider;
	}

}
