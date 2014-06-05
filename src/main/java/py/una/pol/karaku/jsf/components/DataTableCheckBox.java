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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UISelectBoolean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.richfaces.component.UIExtendedDataTable;
import py.una.pol.karaku.dynamic.forms.ListSelectField;
import py.una.pol.karaku.util.Checker;
import py.una.pol.karaku.util.ListHelper;

/**
 * 
 * @author Jorge Ramírez
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 Jun 25, 2013
 * 
 */
@FacesComponent(value = "dataTableCheckBox")
public class DataTableCheckBox extends UINamingContainer {

	private static final int ROWS_PER_PAGE = 5;
	private static final String UNCHECKED = "unchecked";
	private static final String RAWTYPES = "rawtypes";
	private static final String CHECKED_ITEMS = "checkedItems";
	private static final String SELECTED_ITEMS_TEMP = "selectedItemsTemp";
	private static final String SELECTED_ITEMS = "selectedItems";
	private static final String SET_VALUES_METHOD = "setValues";

	private UIExtendedDataTable dataTable;

	public String getValueAttribute() {

		return "listSelectField";
	}

	@SuppressWarnings(UNCHECKED)
	public String init() {

		@SuppressWarnings(RAWTYPES)
		ListSelectField mpb = getSelectField();

		List<?> values = mpb.getValues();
		setSelectedItems((List<Object>) values);
		if (ListHelper.hasElements(values)) {
			return "";
		}
		List<Object> newSelected = new ArrayList<Object>(values.size());

		for (int i = 0; i < values.size(); i++) {
			newSelected.add(values.get(i));
			getCheckedItems().put(mpb.getItemKey(values.get(i)), true);
			getSelectedItemsTemp().add(values.get(i));

		}

		return "";
	}

	public Object get(String key) {

		return getStateHelper().get(key);
	}

	public void put(String key, Object object) {

		getStateHelper().put(key, object);
	}

	/**
	 * Handler llamado cuando se hace click en el checkbox ubicado en el header,
	 * ocasionando que todos los elementos se seleccionen o ninguno se
	 * seleccione.
	 **/
	public void onCheckboxHeaderClicked(final AjaxBehaviorEvent event) {

		boolean isCheckHeader = (Boolean) ((UISelectBoolean) event
				.getComponent()).getSubmittedValue();

		for (Object item : getSelectField().getListHelper().getEntities()) {
			Object key = getItemKey(item);
			getCheckedItems().put(key, isCheckHeader);
			updateSelectedItems(item, isCheckHeader);
		}

		flush();

	}

	@SuppressWarnings("rawtypes")
	public ListSelectField getSelectField() {

		return (ListSelectField) getAttributes().get(getValueAttribute());
	}

	/**
	 *
	 **/
	public void onItemCheckboxClicked(final AjaxBehaviorEvent event) {

		Object selected = dataTable.getRowData();
		Object key = getItemKey(selected);

		Boolean checked = getCheckedItems().get(key);
		getCheckedItems().put(key, checked == null ? true : !checked);

		updateSelectedItems(selected, getCheckedItems().get(key));

		flush();
	}

	public void setValidCheckboxHeader(boolean bol) {

		/**
		 * Este seter es requerido para representar la selección del check de la
		 * cabecera de la lista de elementos.
		 */
	}

	public boolean isValidCheckboxHeader() {

		for (Object item : getSelectField().getListHelper().getEntities()) {
			Object key = getItemKey(item);
			if ((getCheckedItems().get(key) == null)
					|| !getCheckedItems().get(key)) {
				return false;
			}
		}
		return true;
	}

	private Object getItemKey(Object item) {

		Checker.notNull(item, "Please, set a 'listSelectField' to the picker");
		return getSelectField().getItemKey(item);
	}

	/**
	 * Actualiza la lista de elementos seleccionados
	 **/
	private void updateSelectedItems(Object item, boolean add) {

		if (!add) {
			getSelectedItemsTemp().remove(item);
		} else {
			if (!getSelectedItemsTemp().contains(item)) {
				getSelectedItemsTemp().add(item);
			}
		}
	}

	/**
	 * Representa a la accion cancelar, cancela todos los elementos
	 * seleccionados y en el caso de que ya se encuentren elementos previamente
	 * seleccionados los vuelve a setear para que la accion de seleccion
	 * cancelada no tenga efecto .
	 **/
	public String clear() {

		getCheckedItems().clear();
		getSelectedItemsTemp().clear();

		for (Object o : getSelectedItems()) {
			getCheckedItems().put(getSelectField().getItemKey(o), true);
			updateSelectedItems(o, true);
		}
		flush();
		return "";

	}

	public UIExtendedDataTable getDataTable() {

		return dataTable;
	}

	public void setDataTable(UIExtendedDataTable dataTable) {

		this.dataTable = dataTable;
	}

	@SuppressWarnings(UNCHECKED)
	public Map<Object, Boolean> getCheckedItems() {

		Map<Object, Boolean> toRet = (Map<Object, Boolean>) get(CHECKED_ITEMS);

		if (toRet == null) {
			toRet = new HashMap<Object, Boolean>(ROWS_PER_PAGE);
			setCheckedItems(toRet);
		}

		return toRet;
	}

	@SuppressWarnings(UNCHECKED)
	public List<Object> getSelectedItemsTemp() {

		List<Object> itemsTemp = (List<Object>) get(SELECTED_ITEMS_TEMP);
		if (itemsTemp == null) {
			itemsTemp = new ArrayList<Object>(ROWS_PER_PAGE);
			setSelectedItemsTemp(itemsTemp);
		}

		return itemsTemp;
	}

	public void setCheckedItems(Map<Object, Boolean> checkedItems) {

		put(CHECKED_ITEMS, checkedItems);

	}

	public void setSelectedItemsTemp(List<Object> selectedItemsTemp) {

		put(SELECTED_ITEMS_TEMP, selectedItemsTemp);
	}

	@SuppressWarnings(UNCHECKED)
	public List<Object> getSelectedItems() {

		List<Object> items = (List<Object>) get(SELECTED_ITEMS);
		if (items == null) {
			items = new ArrayList<Object>(ROWS_PER_PAGE);
			// cambiar por el total de elementos.
			setSelectedItems(items);
		}

		return items;
	}

	public void setSelectedItems(List<Object> selectedItems) {

		put(SELECTED_ITEMS, selectedItems);
	}

	/**
	 * Setea los valores seleccionados en el pickerField
	 */
	public void updatePickerValues() {

		List<Object> items = getSelectedItemsTemp();
		setSelectedItems(items);
		FacesContext ctx = FacesContext.getCurrentInstance();
		ELContext elCtx = ctx.getELContext();
		MethodExpression me = (MethodExpression) getAttributes().get(
				SET_VALUES_METHOD);
		Object[] params = new Object[1];
		params[0] = items;
		me.invoke(elCtx, params);
	}

	@Override
	public void processUpdates(FacesContext context) {

		updatePickerValues();
		super.processUpdates(context);
	}
	private void flush() {

		setCheckedItems(getCheckedItems());
		setSelectedItemsTemp(getSelectedItemsTemp());
		setSelectedItems(getSelectedItems());
	}

}
