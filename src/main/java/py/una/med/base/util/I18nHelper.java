/**
 * @I18nHelper 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import javax.faces.context.FacesContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
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

	private static ArrayList<ResourceBundle> bundles;

	private static List<String> getBundlesLocation() {

		Resource resource = new ClassPathResource(
				SIGHConfiguration.CONFIG_LOCATION);
		Scanner sc;
		try {
			sc = new Scanner(resource.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String line = "";
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			if (line.startsWith("language_bundles")) {
				break;
			}
		}
		sc.close();
		if (!line.contains("=")) {
			throw new RuntimeException(
					"Archivo de configuraicon no contiene ubicacion de archivos de idiomas");
		}
		String value = line.split("=")[1];

		return Arrays.asList(value.split(" "));
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
		bundles = new ArrayList<ResourceBundle>();
		List<String> bundlesLocation = getBundlesLocation();
		for (String bundle : bundlesLocation) {
			if (bundle.equals(""))
				continue;
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Locale locale = facesContext.getViewRoot().getLocale();
			ResourceBundle toAdd = ResourceBundle.getBundle(bundle, locale);
			bundles.add(toAdd);
		}
	}

	private static String findInBundles(String key) throws KeyNotFoundException {

		for (ResourceBundle bundle : getBundles()) {
			try {
				if (bundle.getString(key) != null)
					return bundle.getString(key);
			} catch (Exception e) {

			}
		}
		throw new KeyNotFoundException(key);
	}

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

		if ("".equals(displayName.toString()))
			return "";
		return getMessage(displayName.key().substring(1,
				displayName.key().length() - 1));
	}

}
