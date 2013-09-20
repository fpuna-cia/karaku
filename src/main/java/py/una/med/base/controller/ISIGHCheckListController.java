/*
 * @ISIGHCheckList.java 1.0 24/07/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import py.una.med.base.dynamic.forms.MultiplePickerField.ItemKeyProvider;
import py.una.med.base.util.SIGHConverterV2;

/**
 * 
 * @author Osmar Vianconi
 * @since 1.0
 * @version 1.0 24/07/2013
 * 
 */
public interface ISIGHCheckListController<T, ID extends Serializable> extends
		ISIGHAdvancedController<T, ID> {

	Collection<T> getSelected();

	Object getItemKeyProvider(T item);

	void setItemKeyProvider(ItemKeyProvider<T> itemKeyProvider);

	Map<T, Boolean> getSelectedMap();

	void setSelectedMap(Map<T, Boolean> selectedMap);

	boolean isSelectAllButtonVisible();

	void setSelectAllButtonVisible(boolean buttonDisabled);

	void setSelected(Collection<T> selected);

	void onCheckboxHeaderClicked(final AjaxBehaviorEvent event);

	void setChecked(T object);

	@Override
	String getDefaultPermission();

	SIGHConverterV2 getConverter();
}
