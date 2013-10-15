/*
 * @MultiplePickerButton.java 1.0 Jun 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import org.richfaces.component.UIExtendedDataTable;
import py.una.med.base.dynamic.forms.MultiplePickerField;

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

	/**
	 *
	 */
	private static final String UNCHECKED = "unchecked";
	/**
	 *
	 */
	private static final String RAWTYPES = "rawtypes";
	private static final String GET_ITEM_KEY_METHOD = "getItemKey";
	private static final String CHECKED_ITEMS = "checkedItems";
	private static final String SELECTED_ITEMS_TEMP = "selectedItemsTemp";
	private static final String SELECTED_ITEMS = "selectedItems";
	private static final String SELECT_ALL_CHECKED = "selectAllChecked";
	private static final String SET_VALUES_METHOD = "setValues";

	private UIExtendedDataTable dataTable;
	private Map<Object, Boolean> checkedItems;
	private List<Object> selectedItemsTemp;
	private List<Object> selectedItems;

	@SuppressWarnings(RAWTYPES)
	private MultiplePickerField pickerField;

	public MultiplePickerButton() {

		super();
		setCheckedItems(new HashMap<Object, Boolean>());
		setSelectedItemsTemp(new ArrayList<Object>());
		setSelectedItems(new ArrayList<Object>());
		setSelectAllChecked(false);
	}

	@SuppressWarnings(UNCHECKED)
	public String init() {

		@SuppressWarnings(RAWTYPES)
		MultiplePickerField mpb = (MultiplePickerField) getAttributes().get(
				"pickerField");
		pickerField = mpb;

		List<?> values = mpb.getValues();
		setSelectedItems((List<Object>) values);
		if ((values == null) || (values.size() == 0)) {
			return "";
		}
		List<Object> newSelected = new ArrayList<Object>(values.size());
		checkedItems = getCheckedItems();

		for (int i = 0; i < values.size(); i++) {
			newSelected.add(values.get(i));
			checkedItems.put(mpb.getItemKey(values.get(i)), true);
			selectedItemsTemp.add(values.get(i));

		}

		setCheckedItems(checkedItems);
		updateCheckboxHeader(mpb.getListHelper().getEntities());
		return "";
	}

	public Object get(String key) {

		return getStateHelper().get(key);
	}

	public void put(String key, Object object) {

		getStateHelper().put(key, object);
	}

	/**
	 *
	 **/
	public void preRenderDataTable(ComponentSystemEvent event) {

	}

	/**
	 * Handler llamado cuando se hace click en el checkbox ubicado en el header,
	 * ocasionando que todos los elementos se seleccionen o ninguno se
	 * seleccione.
	 **/
	@SuppressWarnings(UNCHECKED)
	public void onCheckboxHeaderClicked(final AjaxBehaviorEvent event) {

		setSelectAllChecked(!isSelectAllChecked());

		checkedItems = getCheckedItems();
		if (checkedItems == null) {
			checkedItems = new HashMap<Object, Boolean>();
		}
		List<Object> items = (List<Object>) dataTable.getValue();

		for (Object item : items) {
			Object key = getItemKey(item);
			checkedItems.put(key, isSelectAllChecked());
			updateSelectedItems(item, checkedItems.get(key));
		}
		if (!isSelectAllChecked()) {
			setSelectedItemsTemp(new ArrayList<Object>());
		} else {
			setSelectedItemsTemp(items);
			@SuppressWarnings(RAWTYPES)
			MultiplePickerField mpb = (MultiplePickerField) getAttributes()
					.get("pickerField");
			pickerField = mpb;
			mpb.setValues(items);
		}

		setCheckedItems(checkedItems);
	}

	/**
	 *
	 **/
	@SuppressWarnings(UNCHECKED)
	public void onItemCheckboxClicked(final AjaxBehaviorEvent event) {

		if (getCheckedItems() != null) {
			checkedItems = getCheckedItems();
		}
		Object selected = dataTable.getRowData();
		Object key = getItemKey(selected);

		Boolean checked = checkedItems.get(key);
		checkedItems.put(key, checked == null ? true : !checked);

		updateSelectedItems(selected, checkedItems.get(key));
		setCheckedItems(checkedItems);
		updateCheckboxHeader((List<Object>) dataTable.getValue());
	}

	public void updateCheckboxHeader(List<Object> allValues) {

		for (Object item : allValues) {
			Object key = getItemKey(item);
			if ((checkedItems.get(key) == null) || !getCheckedItems().get(key)) {
				setSelectAllChecked(false);
				return;
			}
		}
		setSelectAllChecked(true);
	}

	@SuppressWarnings(UNCHECKED)
	private Object getItemKey(Object item) {

		if (pickerField != null) {
			return pickerField.getItemKey(item);
		} else {
			FacesContext ctx = FacesContext.getCurrentInstance();
			ELContext elCtx = ctx.getELContext();
			MethodExpression me = (MethodExpression) getAttributes().get(
					GET_ITEM_KEY_METHOD);
			Object[] params = new Object[1];
			params[0] = item;
			return me.invoke(elCtx, params);
		}
	}

	/**
	 * Actualiza la lista de elementos seleccionados
	 **/
	public void updateSelectedItems(Object item, boolean add) {

		selectedItemsTemp = getSelectedItemsTemp();
		if (selectedItemsTemp == null) {
			selectedItemsTemp = new ArrayList<Object>();
		}
		if (add) {
			selectedItemsTemp.add(item);
		} else {
			selectedItemsTemp.remove(item);
		}

		setSelectedItemsTemp(selectedItemsTemp);
		updatePickerValues(selectedItemsTemp);

	}

	/**
	 * Representa a la accion cancelar, cancela todos los elementos
	 * seleccionados y en el caso de que ya se encuentren elementos previamente
	 * seleccionados los vuelve a setear para que la accion de seleccion
	 * cancelada no tenga efecto .
	 **/
	@SuppressWarnings(UNCHECKED)
	public String clear() {

		@SuppressWarnings(RAWTYPES)
		MultiplePickerField mpb = (MultiplePickerField) getAttributes().get(
				"pickerField");
		pickerField = mpb;

		checkedItems.clear();
		selectedItemsTemp.clear();

		if (selectedItems != null) {
			for (int i = 0; i < selectedItems.size(); i++) {
				checkedItems.put(mpb.getItemKey(selectedItems.get(i)), true);
				selectedItemsTemp.add(selectedItems.get(i));
			}
		}

		setCheckedItems(checkedItems);
		updateCheckboxHeader(mpb.getListHelper().getEntities());
		mpb.setValues(selectedItems);
		setSelectedItemsTemp(selectedItems);
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

		return (Map<Object, Boolean>) get(CHECKED_ITEMS);
	}

	@SuppressWarnings(UNCHECKED)
	public List<Object> getSelectedItemsTemp() {

		return (List<Object>) get(SELECTED_ITEMS_TEMP);
	}

	public boolean isSelectAllChecked() {

		return (Boolean) get(SELECT_ALL_CHECKED);
	}

	public void setSelectAllChecked(boolean selectAllChecked) {

		put(SELECT_ALL_CHECKED, selectAllChecked);
	}

	public void setCheckedItems(Map<Object, Boolean> checkedItems) {

		this.checkedItems = checkedItems;
		put(CHECKED_ITEMS, checkedItems);
	}

	public void setSelectedItemsTemp(List<Object> selectedItemsTemp) {

		this.selectedItemsTemp = selectedItemsTemp;
		put(SELECTED_ITEMS_TEMP, selectedItemsTemp);
	}

	@SuppressWarnings(UNCHECKED)
	public List<Object> getSelectedItems() {

		return (List<Object>) get(SELECTED_ITEMS);
	}

	public void setSelectedItems(List<Object> selectedItems) {

		this.selectedItems = selectedItems;
		put(SELECTED_ITEMS, selectedItems);
	}

	private void updatePickerValues(List<Object> items) {

		FacesContext ctx = FacesContext.getCurrentInstance();
		ELContext elCtx = ctx.getELContext();
		MethodExpression me = (MethodExpression) getAttributes().get(
				SET_VALUES_METHOD);
		Object[] params = new Object[1];
		params[0] = items;
		me.invoke(elCtx, params);
	}

}
