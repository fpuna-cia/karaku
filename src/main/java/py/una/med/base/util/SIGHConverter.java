package py.una.med.base.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.Id;

@Deprecated
public class SIGHConverter<T, ID extends Serializable> implements Converter {

	@Override
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String value) {

		return SelectHelper.findValueByStringConversion(context, component,
				value, this);
	}

	@Override
	public String getAsString(final FacesContext context,
			final UIComponent component, final Object object) {

		if (context == null) {
			throw new NullPointerException("context");
		}
		if (component == null) {
			throw new NullPointerException("component");
		}
		if (object == null) {
			return "";
		}

		try {
			return getIdValue(object).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object getIdValue(final Object obj) {

		try {
			for (Field field : obj.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Id.class)) {
					field.setAccessible(true);
					Object data = field.get(obj);
					field.setAccessible(false);
					return data;
				}
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	};
}
