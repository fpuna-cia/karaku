/*
 * @KarakuConverter.java 1.0 Jun 6, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import py.una.med.base.domain.BaseEntity;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 6, 2013
 * 
 */
@Component
public class KarakuConverter implements Converter {

	private final Logger log = LoggerFactory.getLogger(KarakuConverter.class);

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

		if (object instanceof BaseEntity) {
			BaseEntity entity = (BaseEntity) object;
			if (entity.getId() == null) {
				return "";
			}
			return entity.getId().toString();
		}
		try {
			return getIdValue(object).toString();
		} catch (Exception e) {
			log.error("Error al obtener el Id", e);
		}
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
	}
}
