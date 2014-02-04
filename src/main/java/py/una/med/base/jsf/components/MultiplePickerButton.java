/*
 * @MultiplePickerButton.java 1.0 Jun 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.components;

import java.util.Iterator;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;

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

	public String clear() {

		DataTableCheckBox nested = (DataTableCheckBox) find("pepitobailador",
				this);
		nested.clear();
		return "";
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
