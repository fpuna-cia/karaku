package py.una.med.base.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.med.base.model.DisplayName;

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

		if (!key.equals("serialVersionUID") && (value != null)
				&& !value.equals("")) {
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
}
