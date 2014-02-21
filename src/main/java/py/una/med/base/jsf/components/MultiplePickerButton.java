/*
 * @MultiplePickerButton.java 1.0 Jun 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.components;

import java.util.Iterator;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import py.una.med.base.dynamic.forms.ListSelectField;
import py.una.med.base.dynamic.forms.ListSelectField.ValuesChangeListener;

/**
 * 
 * @author Jorge Ramírez
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 Jun 25, 2013
 * 
 */
@FacesComponent(value = "multiplePickerButton")
public final class MultiplePickerButton extends UINamingContainer {

	public String cancel() {

		getDataTableCheckBox().clear();
		return "";

	}

	/**
	 * Updatea los valores seleccionados en el {@link ListSelectField} e invoca
	 * al {@link ValuesChangeListener} del mismo.
	 */
	public void updateValues() {

		DataTableCheckBox nested = getDataTableCheckBox();
		nested.updatePickerValues();
		nested.getSelectField().changeValueListener();

	}

	private DataTableCheckBox getDataTableCheckBox() {

		DataTableCheckBox nested = (DataTableCheckBox) find(
				"dataTableCheckBox", this);
		return nested;
	}

	/**
	 * Método necesario, pues {@link #findComponent(String)} no recupera
	 * correctamente los elementos anidados en un cc.
	 * 
	 * @param id
	 * @param uiComponent
	 * @return
	 */
	private UIComponent find(String id, UIComponent uiComponent) {

		Iterator<UIComponent> it = uiComponent.getFacetsAndChildren();
		while (it.hasNext()) {
			UIComponent ui = it.next();
			if (ui.getId().equals(id)) {
				return ui;
			}
			UIComponent finded = find(id, ui);
			if (finded != null) {
				return finded;
			}
		}
		return null;
	}

}
