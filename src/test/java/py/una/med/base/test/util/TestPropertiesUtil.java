/*
 * @TestPropertiesUtil.java 1.0 Oct 21, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.util;

import java.util.HashMap;
import java.util.Map;
import py.una.med.base.configuration.PropertiesUtil;

/**
 * {@link PropertiesUtil} para los test.
 *
 * <p>
 * Añade la posibilidad de agregar propiedades dinámicamente.
 *
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 21, 2013
 *
 */
public class TestPropertiesUtil extends PropertiesUtil {

	private Map<String, String> propertiesMap;

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String put(String key, String value) {

		if (propertiesMap == null) {
			propertiesMap = new HashMap<String, String>();

		}

		return propertiesMap.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String get(String key, String def) {

		if ((propertiesMap != null) && propertiesMap.containsKey(key)) {
			return propertiesMap.get(key);
		}
		return super.get(key, def);
	}
}
