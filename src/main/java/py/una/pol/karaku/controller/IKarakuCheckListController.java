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
import java.util.Collection;
import java.util.Map;
import javax.faces.event.AjaxBehaviorEvent;
import py.una.pol.karaku.dynamic.forms.MultiplePickerField.ItemKeyProvider;
import py.una.pol.karaku.util.KarakuConverter;

/**
 * 
 * @author Osmar Vianconi
 * @since 1.0
 * @version 1.0 24/07/2013
 * 
 */
public interface IKarakuCheckListController<T, K extends Serializable> extends
		IKarakuAdvancedController<T, K> {

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

	KarakuConverter getConverter();
}
