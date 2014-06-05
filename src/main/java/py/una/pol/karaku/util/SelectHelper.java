/*
 * 
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
