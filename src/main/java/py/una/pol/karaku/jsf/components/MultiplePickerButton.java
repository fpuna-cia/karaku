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
package py.una.pol.karaku.jsf.components;

import java.util.Iterator;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import py.una.pol.karaku.dynamic.forms.ListSelectField;
import py.una.pol.karaku.dynamic.forms.ListSelectField.ValuesChangeListener;

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
