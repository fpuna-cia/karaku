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
package py.una.pol.karaku.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import py.una.pol.karaku.configuration.PropertiesUtil;

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

		if (propertiesMap != null && propertiesMap.containsKey(key)) {
			return propertiesMap.get(key);
		}
		return super.get(key, def);
	}

	@Override
	protected Properties mergeProperties(Properties main) {

		return main;
	}
}
