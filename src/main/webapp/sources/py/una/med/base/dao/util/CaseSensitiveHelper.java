package py.una.med.base.dao.util;

import java.lang.reflect.Field;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.controller.LanguageBean;
import py.una.med.base.dao.annotations.CaseSensitive;

/**
 * Clase que sirve para analizar entidades y cambiar automáticamente todas las
 * cadena a mayúscula según el locale definido en el bean de idioma.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1 07/02/2013
 * @see CaseSensitive
 * 
 */
@Component
public class CaseSensitiveHelper {

	@Autowired
	LanguageBean languageBean;

	/**
	 * Analiza una entidad pasada como parámetro y convierte todas las cadenas a
	 * Mayúsculas, excepto aquellas que tienen la anotación CaseSensitive
	 * 
	 * @param entity
	 *            a ser convertida
	 * @return la misma entidad, con las cadenas en mayúsculas, notese que es la
	 *         misma entidad traída como parámetro
	 */
	public <T> T analize(final T entity) {

		if (entity == null)
			return entity;
		Class<?> clzz = entity.getClass();

		do {
			for (Field f : entity.getClass().getDeclaredFields()) {
				CaseSensitive caseSensitive = f
						.getAnnotation(CaseSensitive.class);
				if (handleAnnotation(entity, f, caseSensitive)) {
					continue;
				}
			}
			clzz = clzz.getSuperclass();
		} while (!clzz.equals(Object.class));

		return entity;
	}

	/**
	 * Analiza una lista de entidades y convierte todas las cadenas a Mayúsculas
	 * 
	 * @param entities
	 *            de entidades
	 * @return Lista de Entidades con cadenas capitalizadas
	 */
	public <T> List<T> analize(final List<T> entities) {

		for (T entity : entities) {
			analize(entity);
		}
		return entities;
	}

	/**
	 * Analiza una lista de entidades y convierte todas las cadenas a Mayúsculas
	 * 
	 * @param entities
	 *            Vector de entidades o entidad
	 * @return Lista de Entidades con cadenas capitalizadas
	 */
	public <T> T[] analize(final T ... entities) {

		for (T entity : entities) {
			analize(entity);
		}
		return entities;
	}

	private <T> boolean handleAnnotation(final T object, final Field f,
			final CaseSensitive caseSensitive) {

		if (caseSensitive != null)
			return false;
		try {
			if (f.getType().equals(String.class)) {
				f.setAccessible(true);
				String value = (String) f.get(object);
				if (value != null) {
					value = value.toUpperCase(languageBean.getCurrentLocale());
					f.set(object, value);
				}
				return true;
			} else
				return false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
}
