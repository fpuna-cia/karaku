/*
 * @KarakuReflectionUtils.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

import java.lang.reflect.ParameterizedType;

/**
 * Provee funcionalidades básicas para utilizar Reflection.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * 
 */
public final class KarakuReflectionUtils {

	/**
	 * Retorna el tipo parámetrico implementado por <i>root</i> de un nivel de
	 * jerarquía.
	 * 
	 * @param leaf
	 *            clase raíz (implementación final)
	 * @param index
	 *            número de clase paramétrica
	 * @return clase paramétrica.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getParameterizedClass(Object leaf, int index) {

		ParameterizedType type = (ParameterizedType) leaf.getClass()
				.getGenericSuperclass();
		return ((Class<T>) type.getActualTypeArguments()[index]);
	}
}
