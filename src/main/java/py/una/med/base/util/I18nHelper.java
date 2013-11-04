/**
 * @I18nHelper 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.exception.KeyNotFoundException;
import py.una.med.base.model.DisplayName;

/**
 * Clase que sirve como punto de acceso único para la internacionalizacion.
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 2.1
 */
public class I18nHelper {

	public static final Logger LOG = LoggerFactory.getLogger(I18nHelper.class);

	@Autowired
	private PropertiesUtil util;

	/**
	 *
	 */
	public I18nHelper() {

		setSingleton(this);
	}

	@PostConstruct
	public void initialize() {

		String value = util.get(SIGHConfiguration.LANGUAGE_BUNDLES_KEY);

		i18nHelper.initializeBundles(Arrays.asList(value.split("\\s+")));
	}

	/**
	 *
	 * XXX Este atributo no es final para que pueda ser reemplazado por otra
	 * implementación, por ejemplo en los test.
	 */
	private static I18nHelper i18nHelper;

	public static I18nHelper getSingleton() {

		return i18nHelper;
	}

	protected static void setSingleton(I18nHelper newSingleton) {

		i18nHelper = newSingleton;
	}

	protected Locale getLocale() {

		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			if (facesContext != null) {
				return facesContext.getViewRoot().getLocale();
			}
		} catch (Exception e) {
			LOG.debug("Can't get locale", e);
		}
		return new Locale("es", "PY");
	}

	private static List<ResourceBundle> bundles;

	protected static List<ResourceBundle> getBundles() {

		return bundles;
	}

	public synchronized void initializeBundles(List<String> bundlesLocation) {

		if (bundles != null) {
			return;
		}
		bundles = new ArrayList<ResourceBundle>(bundlesLocation.size());
		for (String bundle : bundlesLocation) {
			if (bundle.equals("")) {
				continue;
			}
			ResourceBundle toAdd = ResourceBundle
					.getBundle(bundle, getLocale());
			bundles.add(toAdd);
		}
	}

	private String findInBundles(String key) {

		if (bundles == null) {
			initialize();
		}

		for (ResourceBundle bundle : getBundles()) {
			if (bundle.containsKey(key)) {

				return bundle.getString(key);
			}
		}
		LOG.warn("String not found in current bundles {}", key,
				new KeyNotFoundException(key));
		return key + "&&&";
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

		return i18nHelper.findInBundles(key);
	}

	/**
	 * Invoca al método {@link #getMessage(String)} por cada cadena pasada, y lo
	 * agrega a una lista.
	 *
	 * @param keys
	 *            claves del archivo de internacionalización
	 * @return lista con los valores internacionalizados.
	 */
	public List<String> convertStrings(@NotNull String ... keys) {

		assert keys != null;
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
	public boolean compare(String key, String value) {

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

	protected static void setBundles(List<ResourceBundle> bundles) {

		I18nHelper.bundles = bundles;
	}
}
