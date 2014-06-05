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
package py.una.pol.karaku.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.event.AjaxBehaviorEvent;
import org.richfaces.component.UIExtendedDataTable;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.dynamic.forms.MultiplePickerField.ItemKeyProvider;

/**
 * 
 * @author Osmar Vianconi
 * @since 1.0
 * @version 1.0 24/07/2013
 * 
 */
public abstract class KarakuCheckListController<T, K extends Serializable>
		extends KarakuAdvancedController<T, K> implements
		IKarakuCheckListController<T, K> {

	private ItemKeyProvider<T> itemKeyProvider;
	private boolean allButtonVisible;
	private Map<T, Boolean> selectedMap;
	private Collection<T> elementsList;

	@Override
	public abstract IKarakuBaseLogic<T, K> getBaseLogic();

	@Override
	public Object getItemKeyProvider(T item) {

		if (itemKeyProvider == null) {
			return item.hashCode();
		}
		return itemKeyProvider.getKeyValue(item);
	}

	@Override
	public void setItemKeyProvider(ItemKeyProvider<T> itemKeyProvider) {

		this.itemKeyProvider = itemKeyProvider;
	}

	/**
	 * @return selectedMap
	 */
	@Override
	public Map<T, Boolean> getSelectedMap() {

		if (selectedMap == null) {
			selectedMap = new HashMap<T, Boolean>();
		}
		return selectedMap;
	}

	@Override
	public void setSelectedMap(Map<T, Boolean> selectedMap) {

		this.selectedMap = selectedMap;
	}

	@Override
	public boolean isSelectAllButtonVisible() {

		return allButtonVisible;
	}

	@Override
	public void setSelectAllButtonVisible(boolean buttonDisabled) {

		this.allButtonVisible = buttonDisabled;
	}

	/**
	 * @param selected
	 *            selected para setear
	 */
	@Override
	public void setSelected(Collection<T> selected) {

		for (T t : selected) {
			getSelectedMap().put(t, true);
		}
	}

	/**
	 * @return selected
	 */
	@Override
	public Collection<T> getSelected() {

		Collection<T> elemnts = new ArrayList<T>(getSelectedMap().size());
		for (Entry<T, Boolean> entry : getSelectedMap().entrySet()) {
			if (entry.getValue()) {
				elemnts.add(entry.getKey());
			}
		}
		setElementsList(elemnts);
		return elemnts;
	}

	@Override
	public void onCheckboxHeaderClicked(final AjaxBehaviorEvent event) {

		HtmlSelectBooleanCheckbox checkBox = (HtmlSelectBooleanCheckbox) event
				.getSource();
		UIExtendedDataTable dataTable;
		UIComponent test = checkBox;
		while (test != null && !(test instanceof UIExtendedDataTable)) {
			test = test.getParent();
		}
		dataTable = (UIExtendedDataTable) test;
		Boolean selected = (Boolean) checkBox.getValue();
		// XXX por que va a realizar otra llamada a la base de datos
		if (dataTable != null) {
			@SuppressWarnings("unchecked")
			List<T> items = (List<T>) dataTable.getValue();

			for (T item : items) {
				getSelectedMap().put(item, selected);
			}
		}
	}

	@Override
	public void setChecked(T object) {

		getSelectedMap().put(object, true);
		getSelected().add(object);
	}

	@Override
	public String getDefaultPermission() {

		return "SIGH";
	}

	public Collection<T> getElementsList() {

		return elementsList;
	}

	public void setElementsList(Collection<T> elementsList) {

		this.elementsList = elementsList;
	}

	public void reset() {

		elementsList.clear();
		getSelected().clear();
		selectedMap.clear();
	}

}
