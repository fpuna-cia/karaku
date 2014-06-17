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
import java.lang.reflect.Modifier;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.pol.karaku.model.DisplayName;

public final class EntitySerializer {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EntitySerializer.class);

	private EntitySerializer() {

		// No-op
	}

	public static String serialize(Object object) {

		Class<?> clazz = object.getClass();
		StringBuilder sb = new StringBuilder();
		try {
			for (Field field : clazz.getDeclaredFields()) {
				if (!check(field)) {
					continue;
				}
				construct(sb, field, object);
			}
		} catch (Exception e) {
			LOGGER.warn("Can't serialize: ", e);
			return null;
		}
		return sb.toString();
	}

	private static StringBuilder construct(StringBuilder sb, Field f, Object o)
			throws IllegalAccessException {

		f.setAccessible(true);
		Object value = f.get(o);

		if (!StringUtils.isValid(value)) {
			return sb;
		}

		String key = f.getName();
		DisplayName displayName = f.getAnnotation(DisplayName.class);
		if (displayName != null) {

			key = I18nHelper.getName(displayName);
		}

		return Serializer.contruct(sb, key, value.toString());

	}

	private static boolean check(Field f) {

		if (f.isAnnotationPresent(Id.class)) {
			return false;
		}
		if (f.isAnnotationPresent(OneToMany.class)) {
			return false;
		}
		if (f.isAnnotationPresent(ManyToMany.class)) {
			return false;
		}
		if (f.isSynthetic()) {
			return false;
		}
		return !isStatic(f);
	}

	private static boolean isStatic(Field field) {

		return Modifier.isStatic(field.getModifiers());
	}
}
