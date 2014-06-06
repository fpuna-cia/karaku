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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import py.una.pol.karaku.util.I18nHelper;

/**
 * Archivo de test Para I18nHelper.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 * 
 */
public class TestI18nHelper extends I18nHelper {

	private HashMap<String, String> extraKeys;

	public void addString(String key, String value) {

		if (extraKeys == null) {
			extraKeys = new HashMap<String, String>();
		}

		extraKeys.put(key, value);
	}

	@Override
	protected String getStringOrNull(String key) {

		if (extraKeys != null && extraKeys.containsKey(key)) {
			return extraKeys.get(key);
		}
		return super.getStringOrNull(key);
	}

	@Override
	protected synchronized void initializeBundles(List<String> bundlesLocation) {

		if (getBundles() != null) {
			return;
		}
		List<String> realNames = new ArrayList<String>(bundlesLocation.size());
		for (String bundle : bundlesLocation) {
			realNames.add(bundle.substring(3));
		}
		super.initializeBundles(realNames);
	}

	@Override
	@Autowired
	protected void setContext(ApplicationContext context) {

		super.setContext(context);
		setWeakSingleton(null);
	}
}
