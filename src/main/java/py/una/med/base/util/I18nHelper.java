/**
 * @I18nHelper 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.exception.KeyNotFoundException;
import py.una.med.base.model.DisplayName;

/**
 * Clase que sirve como punto de acceso unico para la internacionalizacion
 * 
 * @author Arturo Volpe
 */

@Component
public class I18nHelper {

	private static PropertiesUtil propertiesUtil;

	private static ArrayList<ResourceBundle> bundles;

	private static List<String> getBundlesLocation() {

		// Resource resource = new ClassPathResource(
		// SIGHConfiguration.CONFIG_LOCATION);
		//
		// Properties prop = new Properties();
		// try {
		// prop.load(resource.getInputStream());
		// } catch (IOException e) {
		// throw new NotLoadFileConfigurationException();
		// }
		String value = propertiesUtil
				.getProperty(SIGHConfiguration.LANGUAGE_BUNDLES_KEY);

		return Arrays.asList(value.split("\\s+"));
	}

	private static List<ResourceBundle> getBundles() {

		if (bundles != null) {
			return bundles;
		} else {
			initializeBundles();
		}
		return bundles;
	}

	private synchronized static void initializeBundles() {

		if (bundles != null) {
			return;
		}
		List<String> bundlesLocation = getBundlesLocation();
		bundles = new ArrayList<ResourceBundle>(bundlesLocation.size());
		for (String bundle : bundlesLocation) {
			if (bundle.equals("")) {
				continue;
			}
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Locale locale = facesContext.getViewRoot().getLocale();
			ResourceBundle toAdd = ResourceBundle.getBundle(bundle, locale);
			bundles.add(toAdd);
		}
	}

	private static String findInBundles(String key) throws KeyNotFoundException {

		for (ResourceBundle bundle : getBundles()) {
			if (bundle.containsKey(key)) {

				return bundle.getString(key);
			}
		}
		throw new KeyNotFoundException(key);
	}

	/**
	 * Retorna la cadena internacionalizada de la llave pasada, busca en todos
	 * los archivos de internacionalizacion definidos en el karaku.properties.
	 * 
	 * @param key
	 *            llave del archivo de internacionalizacion
	 * @return cadena internacionalizad de acuerdo al locale actual
	 */
	public String getString(String key) {

		return findInBundles(key);
	}

	public static String getMessage(String key) {

		return findInBundles(key);
	}

	public static List<String> convertStrings(String ... strings) {

		ArrayList<String> convert = new ArrayList<String>(strings.length);
		for (String s : strings) {
			convert.add(getMessage(s));
		}
		return convert;
	}

	public static boolean compare(String key, String value) {

		return getMessage(key).equals(value);
	}

	public static String getName(DisplayName displayName) {

		if ("".equals(displayName.toString())) {
			return "";
		}
		return getMessage(displayName.key().substring(1,
				displayName.key().length() - 1));
	}

	/**
	 * Asigna un properties util a la clase estatica.
	 * 
	 * @param propertiesUtil
	 *            bean properties util
	 */
	@Autowired
	public void setPropertiesUtil(PropertiesUtil propertiesUtil) {

		I18nHelper.propertiesUtil = propertiesUtil;
	}

}
