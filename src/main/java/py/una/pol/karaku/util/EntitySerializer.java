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

import java.lang.annotation.Annotation;
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

	public static <T> String serialize(T object) {

		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) object.getClass();
		StringBuilder sb = new StringBuilder();
		try {
			for (Field field : clazz.getDeclaredFields()) {
				if (!checkAnnotations(field)) {
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
		String key = f.getName();
		Object value = f.get(o);

		DisplayName displayName = f.getAnnotation(DisplayName.class);
		if (displayName != null) {
			key = I18nHelper.getName(displayName);
		}

		if (!"serialVersionUID".equals(key) && (value != null)
				&& !"".equals(value) && !isPublicStaticFinal(f)) {
			return Serializer.contruct(sb, key, value.toString());
		}
		return sb;

	}

	private static boolean checkAnnotations(Field f) {

		if (checkAnnotation(f, Id.class)) {
			return false;
		}
		if (checkAnnotation(f, OneToMany.class)) {
			return false;
		}
		if (checkAnnotation(f, ManyToMany.class)) {
			return false;
		}
		return true;
	}

	private static boolean checkAnnotation(Field f,
			Class<? extends Annotation> clazz) {

		if (f.getAnnotation(clazz) != null) {
			return true;
		}
		return false;
	}

	public static boolean isPublicStaticFinal(Field field) {

		int modifiers = field.getModifiers();
		return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier
				.isFinal(modifiers));
	}
}
