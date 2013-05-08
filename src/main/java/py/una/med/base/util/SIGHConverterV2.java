package py.una.med.base.util;

import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SIGHConverterV2 implements Converter {

	private Logger log = LoggerFactory.getLogger(SIGHConverterV2.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		return SelectHelper.findValueByStringConversion(context, component,
				value, this);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {

		if (context == null) {
			throw new IllegalArgumentException("context");
		}
		if (component == null) {
			throw new IllegalArgumentException("component");
		}
		if (object == null) {
			return "";
		}

		try {
			return getIdValue(object).toString();
		} catch (Exception e) {
			log.error("Error al obtener el Id", e);
		}
		System.out.println("HOLA 2");
		return null;
	}

	private Object getIdValue(Object obj) {

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
			log.error("Error al obtener el Id", ex);
			return null;
		}
	};
}
