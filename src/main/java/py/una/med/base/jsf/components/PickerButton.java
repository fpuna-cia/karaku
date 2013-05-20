/*
 * @PickerButton.java 1.0 Feb 25, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.jsf.components;

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

}
