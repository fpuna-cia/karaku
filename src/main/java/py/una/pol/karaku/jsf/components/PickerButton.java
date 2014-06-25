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
import org.richfaces.component.UIExtendedDataTable;
import py.una.pol.karaku.dynamic.forms.PickerField;
import py.una.pol.karaku.jsf.utils.JSFUtils;
import py.una.pol.karaku.util.ListHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 25, 2013
 * 
 */
@FacesComponent(value = "pickerButton")
public class PickerButton extends UINamingContainer {

	public Object getSelectedItem() {

		UIExtendedDataTable dt = (UIExtendedDataTable) JSFUtils.find(
				getPicker().getDataTableID(), this);
		Collection<?> col = dt.getSelection();

		if (!ListHelper.hasElements(col)) {
			return null;

		}
		Object selected = col.iterator().next();

		dt.setRowKey(selected);
		return dt.getRowData();
	}

	public Object get(String key) {

		return getStateHelper().get(key);
	}

	public void put(String key, Object object) {

		getStateHelper().put(key, object);
	}

	public void aceptarClicked() {

		getPicker().changeValueListener(getSelectedItem());
	}

	private PickerField<?> getPicker() {

		return (PickerField<?>) getAttributes().get("pickerField");
	}

}
