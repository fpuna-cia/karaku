/*
 * @TestI18nHelper.java 1.0 Oct 17, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import py.una.med.base.util.I18nHelper;

/**
 * Archivo de test Para I18nHelper.
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 *
 */
public class TestI18nHelper extends I18nHelper {

	HashMap<String, String> aditionals;

	public void addString(String key, String value) {

		if (aditionals == null) {
			aditionals = new HashMap<String, String>();
		}

		aditionals.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(String key) {

		if ((aditionals != null) && aditionals.containsKey(key)) {
			return aditionals.get(key);
		}
		return super.getString(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Locale getLocale() {

		return new Locale("es", "PY");
	}

	@Override
	public synchronized void initializeBundles(List<String> bundlesLocation) {

		if (bundles != null) {
			return;
		}
		bundles = new ArrayList<ResourceBundle>(bundlesLocation.size());
		for (String bundle : bundlesLocation) {
			if (bundle.equals("")) {
				continue;
			}
			bundle = bundle.substring(3);
			ResourceBundle toAdd = ResourceBundle
					.getBundle(bundle, getLocale());
			bundles.add(toAdd);
		}
	}
}
