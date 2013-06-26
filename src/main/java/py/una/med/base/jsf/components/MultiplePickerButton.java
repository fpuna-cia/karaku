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

/**
 * 
 * @author Jorge Ramírez
 * @since 1.0
 * @version 1.0 Jun 25, 2013
 * 
 */
@FacesComponent(value = "multiplePickerButton")
public class MultiplePickerButton extends UINamingContainer {

	private static final String GET_ITEM_KEY_METHOD = "getItemKey";
	private static final String CHECKED_ITEMS = "checkedItems";
	private static final String SELECTED_ITEMS = "selectedItems";
	private static final String SELECT_ALL_CHECKED = "selectAllChecked";
	private static final String SET_VALUES_METHOD = "setValues";

	private UIExtendedDataTable dataTable;
	private Map<Object, Boolean> checkedItems;
	private List<Object> selectedItems;
	@SuppressWarnings("unused")
	private boolean selectAllChecked;

	public MultiplePickerButton() {

		super();
		setCheckedItems(new HashMap<Object, Boolean>());
		setSelectedItems(new ArrayList<Object>());
		setSelectAllChecked(false);
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
	 * ocasionando que todos los elementos se seleccionen.
	 **/
	@SuppressWarnings("unchecked")
	public void onCheckboxHeaderClicked(final AjaxBehaviorEvent event) {

		setSelectAllChecked(!isSelectAllChecked());

		checkedItems = getCheckedItems();

		List<Object> items = (List<Object>) dataTable.getValue();

		for (Object item : items) {
			Object key = getItemKey(item);
			checkedItems.put(key, isSelectAllChecked());
			updateSelectedItems(item, checkedItems.get(key));
		}

		setCheckedItems(checkedItems);
	}

	/**
	 * 
	 **/
	public void onItemCheckboxClicked(final AjaxBehaviorEvent event) {

		checkedItems = getCheckedItems();
		Object selected = dataTable.getRowData();
		Object key = getItemKey(selected);
		Boolean checked = checkedItems.get(key);
		checkedItems.put(key, checked == null ? true : !checked);
		updateSelectedItems(selected, checkedItems.get(key));
		setCheckedItems(checkedItems);
		updateCheckboxHeader();
	}

	@SuppressWarnings("unchecked")
	public void updateCheckboxHeader() {

		for (Object item : (List<Object>) dataTable.getValue()) {
			Object key = getItemKey(item);
			if (checkedItems.get(key) == null || !getCheckedItems().get(key)) {
				setSelectAllChecked(false);
				return;
			}
		}
		setSelectAllChecked(true);
	}

	private Object getItemKey(Object item) {

		FacesContext ctx = FacesContext.getCurrentInstance();
		ELContext elCtx = ctx.getELContext();
		MethodExpression me = (MethodExpression) getAttributes().get(
				GET_ITEM_KEY_METHOD);
		Object[] params = new Object[1];
		params[0] = item;
		return me.invoke(elCtx, params);
	}

	/**
	 * Actualiza la lista de elementos seleccionados
	 **/
	public void updateSelectedItems(Object item, boolean add) {

		selectedItems = getSelectedItems();
		if (add) {
			selectedItems.add(item);
		} else {
			selectedItems.remove(item);
		}

		setSelectedItems(selectedItems);
		updatePickerValues(selectedItems);

	}

	/**
	 * Reinicializa la lista de elementos seleccionados.
	 **/
	public void clearSelectedItems() {

		checkedItems = getCheckedItems();
		checkedItems.clear();
		setCheckedItems(checkedItems);
		selectedItems = getSelectedItems();
		selectedItems.clear();
		setSelectedItems(selectedItems);
		setSelectAllChecked(false);
	}

	public UIExtendedDataTable getDataTable() {

		return dataTable;
	}

	public void setDataTable(UIExtendedDataTable dataTable) {

		this.dataTable = dataTable;
	}

	@SuppressWarnings("unchecked")
	public Map<Object, Boolean> getCheckedItems() {

		return (Map<Object, Boolean>) get(CHECKED_ITEMS);
	}

	@SuppressWarnings("unchecked")
	public List<Object> getSelectedItems() {

		return (List<Object>) get(SELECTED_ITEMS);
	}

	public boolean isSelectAllChecked() {

		return (Boolean) get(SELECT_ALL_CHECKED);
	}

	public void setSelectAllChecked(boolean selectAllChecked) {

		this.selectAllChecked = selectAllChecked;
		put(SELECT_ALL_CHECKED, selectAllChecked);
	}

	public void setCheckedItems(Map<Object, Boolean> checkedItems) {

		this.checkedItems = checkedItems;
		put(CHECKED_ITEMS, checkedItems);
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
