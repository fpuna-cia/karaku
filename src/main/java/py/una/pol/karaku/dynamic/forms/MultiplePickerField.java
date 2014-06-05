/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.dynamic.forms;

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

		return "py.una.pol.karaku.dynamic.forms.MultiplePickerField";
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
