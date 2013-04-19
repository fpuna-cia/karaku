/*
 * @MainInstanceHelperData.java 1.0 Feb 14, 2013
 * 
 * Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import py.una.med.base.dao.annotations.MainInstance;

/**
 * Clase auxiliar que manipula los field que tienen la anotación
 * {@link MainInstance}.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 14, 2013
 * 
 */
public class MainInstanceFieldHelper {

	public static Map<Class<?>, List<Field>> fields;

	private static <T> List<Field> generateAndCreateFields(final Class<T> clazz) {

		List<Field> aRet = new ArrayList<Field>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			MainInstance principal = f.getAnnotation(MainInstance.class);
			if (principal != null) {
				aRet.add(f);
			}
		}
		MainInstanceFieldHelper.fields.put(clazz, aRet);
		return aRet;
	}

	public static <T> List<Field> getMainInstanceFields(final Class<T> clazz) {

		if (fields == null) {
			fields = new HashMap<Class<?>, List<Field>>(1);
		}
		List<Field> aRet = fields.get(clazz);
		if (aRet != null) {
			return aRet;
		}
		return generateAndCreateFields(clazz);
	}
}
