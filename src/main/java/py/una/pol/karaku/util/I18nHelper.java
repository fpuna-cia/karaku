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
package py.una.pol.karaku.util;

import static py.una.pol.karaku.util.Checker.notNull;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import py.una.pol.karaku.configuration.KarakuBaseConfiguration;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.model.DisplayName;

/**
 * Clase que sirve como punto de acceso único para la internacionalizacion.
 * 
 * <p>
 * Versión 3 le agrega cadenas parametrizadas, ver
 * {@link #getString(String, Object...)}
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 3
 */
public class I18nHelper {

	private static List<ResourceBundle> bundles;

	private static WeakReference<I18nHelper> weakSingleton;

	public static final String I18N_LENIENT = "karaku.i18n.lenient";

	private boolean lenient;
	@Log
	private Logger log;

	private static ApplicationContext context;

	@Autowired
	private PropertiesUtil util;

	/**
	 * @param context
	 *            context para setear
	 */
	@Autowired
	protected void setContext(ApplicationContext context) {

		setStaticContext(context);
	}

	/**
	 * @return weakSingleton
	 */
	public static WeakReference<I18nHelper> getWeakSingleton() {

		return weakSingleton;
	}

	/**
	 * @param weakSingleton
	 *            weakSingleton para setear
	 */
	protected static void setWeakSingleton(
			WeakReference<I18nHelper> weakSingleton) {

		I18nHelper.weakSingleton = weakSingleton;
	}

	@PostConstruct
	public void initialize() {

		lenient = util.getBoolean(I18N_LENIENT, true);
		String value = util.get(KarakuBaseConfiguration.LANGUAGE_BUNDLES_KEY);
		getSingleton().initializeBundles(Arrays.asList(value.split("\\s+")));
	}

	private static void setStaticContext(ApplicationContext newContext) {

		context = newContext;
	}

	/**
	 * Retorna el {@link I18nHelper} que actualmente esta siendo utilizado por
	 * la aplicación.
	 * 
	 * @return
	 */
	public static I18nHelper getSingleton() {

		if (weakSingleton != null) {
			return weakSingleton.get();
		}

		synchronized (I18nHelper.class) {
			if (weakSingleton == null) {
				weakSingleton = new WeakReference<I18nHelper>(
						context.getBean(I18nHelper.class));
			}
		}
		return weakSingleton.get();
	}

	protected Locale getLocale() {

		return new Locale("es", "PY");
	}

	protected static List<ResourceBundle> getBundles() {

		return bundles;
	}

	protected synchronized void initializeBundles(List<String> bundlesLocation) {

		notNull(bundlesLocation,
				"Can't initialize bundles without bundles paths");

		bundles = new ArrayList<ResourceBundle>(bundlesLocation.size());
		for (String bundle : bundlesLocation) {
			if ("".equals(bundle)) {
				continue;
			}
			ResourceBundle toAdd = ResourceBundle
					.getBundle(bundle, getLocale());
			bundles.add(toAdd);
		}
	}

	private String findInBundles(String key) {

		String toRet = getStringOrNull(key);
		if (toRet != null) {
			return toRet;
		}
		if (!lenient) {
			return key;
		}
		return key + "&&&";
	}

	protected String getStringOrNull(String key) {

		if (bundles == null) {
			initialize();
		}

		for (ResourceBundle bundle : getBundles()) {
			if (bundle.containsKey(key)) {

				return bundle.getString(key);
			}
		}
		return null;
	}

	/**
	 * Retorna la cadena internacionalizada de la llave pasada.
	 * 
	 * <p>
	 * Si la cadena pasada es:
	 * 
	 * <pre>
	 * 	KEY = El auto con chapa {} fue creado correctamente a las {} horas.
	 * </pre>
	 * 
	 * Se muestran diferentes invocaciones con diferentes resultados:
	 * 
	 * <pre>
	 * 	getString("KEY", "ADB 333", "15:00") 
	 * 	=>	El auto con chapa ADB 333 fue creado correctamente a las 15:00 horas
	 * 
	 * 	getString("KEY")
	 * 	=>	El auto con chapa {} fue creado correctamente a las {} horas.
	 * 
	 * 	getString("KEY", "123 CCC")
	 * 	=>	El auto con chapa 123 CCC fue creado correctamente a las {} horas.
	 * 
	 * </pre>
	 * 
	 * </p>
	 * 
	 * <p>
	 * La obtención de las cadenas internacionalizadas se define en
	 * {@link #initializeBundles(List)}, el cual recibe una lista de nombres de
	 * archivos con cadenas de internacionalización, las mismas se defien en
	 * <code>karaku.properties</code>.
	 * </p>
	 * 
	 * @param key
	 *            llave del archivo de internacionalizacion
	 * @param arguments
	 *            (since 3.0) argumentos de la cadena parametrizada.
	 * @return cadena internacionalizad de acuerdo al locale actual
	 */
	public String getString(String key, Object ... arguments) {

		return format(findInBundles(key), arguments);
	}

	/**
	 * Retorna la cadena internacionalizada de la llave pasada, busca en todos
	 * los archivos de internacionalizacion definidos en el karaku.properties.
	 * 
	 * @param key
	 *            llave del archivo de internacionalizacion
	 * @return cadena internacionalizad de acuerdo al locale actual
	 * @see #getString(String, Object...)
	 */
	public static String getMessage(String key) {

		return getSingleton().getString(key);
	}

	/**
	 * Invoca al método {@link #getMessage(String)} por cada cadena pasada, y lo
	 * agrega a una lista.
	 * 
	 * @param keys
	 *            claves del archivo de internacionalización
	 * @return lista con los valores internacionalizados.
	 */
	public List<String> convertStrings(String string, String ... keys) {

		List<String> convert = new ArrayList<String>();
		convert.add(getString(string));
		if (!ArrayUtils.isEmpty(keys)) {
			for (String s : keys) {
				convert.add(getString(s));
			}
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

		return getString(key).equals(value);
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
		if (StringUtils.isInvalid(displayName.key())) {
			return "";
		}
		String key = displayName.key();
		char startWith = key.charAt(0);
		char endWith = key.charAt(key.length() - 1);

		if (startWith != '{' && endWith != '}') {
			return getMessage(key);
		}

		return getMessage(displayName.key().substring(1,
				displayName.key().length() - 1));
	}

	protected String format(@Nonnull String base, Object[] arguments) {

		if (ArrayUtils.isEmpty(arguments)) {
			return base;
		}

		StringBuilder builder = new StringBuilder();
		int templateStart = 0;
		int i = 0;
		while (i < arguments.length) {
			int placeholderStart = base.indexOf("{}", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			builder.append(base.substring(templateStart, placeholderStart));
			builder.append(arguments[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(base.substring(templateStart));

		return builder.toString();
	}
}
