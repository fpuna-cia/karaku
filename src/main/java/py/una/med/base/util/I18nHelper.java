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
import javax.validation.constraints.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.exception.KeyNotFoundException;
import py.una.med.base.model.DisplayName;

/**
 * Clase que sirve como punto de acceso único para la internacionalizacion
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 2.1
 */

@Component
public class I18nHelper {

	private static PropertiesUtil propertiesUtil;

	private static List<ResourceBundle> bundles;

	private static List<String> getBundlesLocation() {

		String value = getPropertiesUtil().getProperty(
				SIGHConfiguration.LANGUAGE_BUNDLES_KEY);

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

	/**
	 * Retorna la cadena internacionalizada de la llave pasada, busca en todos
	 * los archivos de internacionalizacion definidos en el karaku.properties.
	 * 
	 * @param key
	 *            llave del archivo de internacionalizacion
	 * @return cadena internacionalizad de acuerdo al locale actual
	 */
	public static String getMessage(String key) {

		return findInBundles(key);
	}

	/**
	 * Invoca al método {@link #getMessage(String)} por cada cadena pasada, y lo
	 * agrega a una lista.
	 * 
	 * @param keys
	 *            claves del archivo de internacionalización
	 * @return lista con los valores internacionalizados.
	 */
	public static List<String> convertStrings(@NotNull String ... keys) {

		ArrayList<String> convert = new ArrayList<String>(keys.length);
		for (String s : keys) {
			convert.add(getMessage(s));
		}
		return convert;
	}

	/**
	 * Compara una clave con un valor internacionalizado.
	 * 
	 * @param key
	 *            clave del archivo
	 * @param value
	 *            supuesto valor internacionalizado
	 * @return <code>true</code> si es el valor, <code>false</code> en otro
	 *         caso.
	 */
	public static boolean compare(String key, String value) {

		return getMessage(key).equals(value);
	}

	/**
	 * Retorna el valor internacionalizado de una anotación {@link DisplayName}.
	 * 
	 * @param displayName
	 *            anotación
	 * @return "" si es <code>null</code> o esta vacía, en otro caso del valor
	 *         internacionalizado.
	 */
	public static String getName(DisplayName displayName) {

		if (displayName == null) {
			return "";
		}
		if ("".equals(displayName.toString())) {
			return "";
		}
		return getMessage(displayName.key().substring(1,
				displayName.key().length() - 1));
	}

	private synchronized static PropertiesUtil getPropertiesUtil() {

		if (propertiesUtil == null) {
			propertiesUtil = new PropertiesUtil();
			propertiesUtil.setLocation(new ClassPathResource(
					SIGHConfiguration.CONFIG_LOCATION));
		}

		return propertiesUtil;
	}

}
