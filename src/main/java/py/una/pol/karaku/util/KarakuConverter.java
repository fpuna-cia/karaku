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

import java.lang.reflect.Field;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.domain.BaseEntity;

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

	private static KarakuConverter INSTANCE = new KarakuConverter();

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

	/**
	 * @return iNSTANCE
	 */
	public static KarakuConverter getInstance() {

		return INSTANCE;
	}

}
