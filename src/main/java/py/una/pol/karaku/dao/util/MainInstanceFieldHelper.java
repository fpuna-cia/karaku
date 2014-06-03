/*
 * @MainInstanceHelperData.java 1.0 Feb 14, 2013
 *
 * Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dao.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import py.una.pol.karaku.dao.annotations.MainInstance;

/**
 * Clase auxiliar que manipula los field que tienen la anotación
 * {@link MainInstance}.
 *
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 14, 2013
 *
 */
public final class MainInstanceFieldHelper {

	private MainInstanceFieldHelper() {

		// No-op
	}

	private static Map<Class<?>, List<Field>> fields;

	private static synchronized <T> List<Field> generateAndCreateFields(
			final Class<T> clazz) {

		if (fields.containsKey(clazz)) {
			return fields.get(clazz);
		}
		List<Field> aRet = new ArrayList<Field>();
		Field[] fieldz = clazz.getDeclaredFields();
		for (Field f : fieldz) {
			MainInstance principal = f.getAnnotation(MainInstance.class);
			if (principal != null) {
				aRet.add(f);
			}
		}
		MainInstanceFieldHelper.fields.put(clazz, aRet);
		return aRet;
	}

	/**
	 * Lista de atributos con la anotación {@link MainInstance}.
	 *
	 * <p>
	 * Busca recursivamente entre los atributos de una clase, se cachean los
	 * resultados para reducir costos de reflección.
	 * </p>
	 *
	 * @param clazz
	 *            clase a inspeccionar.
	 * @return {@link List} de atributos, nunca <code>null</code>
	 */
	public static <T> List<Field> getMainInstanceFields(final Class<T> clazz) {

		init();
		if (fields.containsKey(clazz)) {
			return fields.get(clazz);
		}
		return generateAndCreateFields(clazz);
	}

	private static synchronized void init() {

		if (fields == null) {
			fields = new HashMap<Class<?>, List<Field>>(1);
		}
	}
}
