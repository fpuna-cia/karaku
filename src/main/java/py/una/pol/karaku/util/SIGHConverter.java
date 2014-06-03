package py.una.med.base.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @deprecated Utiilzar {@link SIGHConverterV2}
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 12, 2013
 * 
 */
@Deprecated
public class SIGHConverter<T, K extends Serializable> implements Converter {

	private Logger log = LoggerFactory.getLogger(SIGHConverter.class);

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
			throw new IllegalArgumentException("context");
		}
		if (component == null) {
			throw new IllegalArgumentException("component");
		}
		if (object == null) {
			return "";
		}

		return getIdValue(object).toString();
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
			log.error("Error al obtener el Id", ex);
			return null;
		}
	}
}