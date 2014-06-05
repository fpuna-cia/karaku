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

import java.util.Collection;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.event.AjaxBehaviorEvent;
import org.richfaces.component.UIExtendedDataTable;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 25, 2013
 * 
 */
@FacesComponent(value = "pickerButton")
public class PickerButton extends UINamingContainer {

	private static final String SELECTION_KEY = "selection";
	private static final String SELECTED_KEY = "selected";

	public Object getSelectedItem() {

		return get(SELECTED_KEY);
	}

	public void setSelectedItem(Object selected) {

		put(SELECTED_KEY, selected);
	}

	/**
	 * @return currentSelection
	 */
	@SuppressWarnings("unchecked")
	public Collection<Object> getCurrentSelection() {

		return (Collection<Object>) get(SELECTION_KEY);
	}

	public Object get(String key) {

		return getStateHelper().get(key);
	}

	public void put(String key, Object object) {

		getStateHelper().put(key, object);
	}

	/**
	 * @param currentSelection
	 *            currentSelection para setear
	 */
	public void setCurrentSelection(Collection<Object> currentSelection) {

		put(SELECTION_KEY, currentSelection);
	}

	public boolean getSelectDisable() {

		return getCurrentSelection() == null;
	}

	public void selectionListener(AjaxBehaviorEvent event) {

		UIExtendedDataTable dataTable = (UIExtendedDataTable) event
				.getComponent();

		for (Object selectionKey : getCurrentSelection()) {
			dataTable.setRowKey(selectionKey);
			if (dataTable.isRowAvailable()) {
				setSelectedItem(dataTable.getRowData());

			}
		}
	}

	public void clear() {

		setCurrentSelection(null);
		setSelectedItem(null);
	}
}
