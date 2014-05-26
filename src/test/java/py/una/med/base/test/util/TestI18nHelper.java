/*
 * @TestI18nHelper.java 1.0 Oct 17, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
