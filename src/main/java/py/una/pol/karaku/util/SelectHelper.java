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
package py.una.pol.karaku.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

public final class SelectHelper {

	private SelectHelper() {

		// No-op
	}

	public static <T> List<SelectItem> getSelectItems(Collection<T> items,
			LabelProvider<T> labelProvider) {

		List<SelectItem> aRet = new ArrayList<SelectItem>();
		for (T valor : items) {
			aRet.add(new SelectItem(valor, labelProvider.getAsString(valor)));
		}
		return aRet;
	}

	public static <T> List<SelectItem> getSelectItems(T[] items,
			LabelProvider<T> labelProvider) {

		List<SelectItem> aRet = new ArrayList<SelectItem>();
		for (T valor : items) {
			aRet.add(new SelectItem(valor, labelProvider.getAsString(valor)));
		}
		return aRet;
	}

	public static <T> List<SelectItem> getSelectItems(Collection<T> items) {

		return getSelectItems(items, new LabelProvider<T>() {

			@Override
			public String getAsString(T object) {

				return object.toString();
			}
		});
	}

	public static boolean isEmpty(Object[] array) {

		return (array == null) || (array.length == 0);
	}

	public static Object findValueByStringConversion(FacesContext context,
			UIComponent component, String value, Converter converter) {

		return findValueByStringConversion(context, component,
				new SelectItemsIterator(context, component), value, converter);
	}

	private static Object findValueByStringConversion(FacesContext context,
			UIComponent component, Iterator<SelectItem> items, String value,
			Converter converter) {

		while (items.hasNext()) {
			SelectItem item = items.next();
			if (item instanceof SelectItemGroup) {
				SelectItem[] subitems = ((SelectItemGroup) item)
						.getSelectItems();
				if (!isEmpty(subitems)) {
					Iterator<SelectItem> iSubItems = Arrays.asList(subitems)
							.iterator();
					Object object = findValueByStringConversion(context,
							component, iSubItems, value, converter);
					if (object != null) {
						return object;
					}
				}
			} else if (!item.isNoSelectionOption()
					&& value.equals(converter.getAsString(context, component,
							item.getValue()))) {
				return item.getValue();
			}
		}
		return null;
	}
}
