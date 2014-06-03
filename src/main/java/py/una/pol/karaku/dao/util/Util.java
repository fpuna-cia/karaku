package py.una.med.base.dao.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javax.persistence.Column;
import javax.persistence.Id;

public final class Util {

	private Util() {

		// No-op
	}

	public static boolean isUsable(Field f) {

		if (Modifier.isStatic(f.getModifiers())) {
			return false;
		}
		if (f.getAnnotation(Id.class) != null) {
			return false;
		}
		return true;
	}

	public static String getColumnName(Field f) {

		Column column = f.getAnnotation(Column.class);
		if (column == null) {
			return f.getName();
		} else {
			return column.name();
		}
	}
}
