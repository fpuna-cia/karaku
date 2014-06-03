/**
 * @PropertiesUtil 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import py.una.pol.karaku.exception.KarakuPropertyNotFoundException;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.exception.KarakuWrongConfigurationFileException;
import py.una.pol.karaku.util.Util;

/**
 * PlaceHolder para el acceso programatico a las opciones de configuración del
 * sistema
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0
 * 
 */
public class PropertiesUtil extends PropertyPlaceholderConfigurer {

	private Map<String, String> propertiesMap;
	/**
	 * Ubicacion del archivo donde se almacena la información sensible.
	 */
	public static final String ANOTHER_KEY = "karaku.changing.properties";

	@Override
	protected void processProperties(
			final ConfigurableListableBeanFactory beanFactory,
			final Properties props) {

		super.processProperties(beanFactory, mergeProperties(props));
		propertiesMap = new HashMap<String, String>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();

			propertiesMap.put(keyStr, props.getProperty(keyStr));
		}
	}

	/**
	 * Dado un nombre de archivo lo carga a las propiedades, si no es un path
	 * del classpath, lo carga del sistema operativo.
	 * 
	 * @param properties
	 *            al que se le añadiran propiedades
	 */
	protected Properties mergeProperties(Properties main) {

		String filePath = main.getProperty(ANOTHER_KEY, "config.properties");
		Properties properties = new Properties();
		try {
			if (filePath.startsWith("/")) {
				FileInputStream fis = new FileInputStream(filePath);
				properties.load(fis);
				fis.close();
			} else {
				properties.load(new ClassPathResource(filePath)
						.getInputStream());
			}
		} catch (FileNotFoundException e) {
			throw new KarakuWrongConfigurationFileException(filePath, e);
		} catch (IOException e) {
			throw new KarakuWrongConfigurationFileException(filePath, e);
		}

		for (Entry<Object, Object> entry : properties.entrySet()) {
			Object value = entry.getValue();
			value = ((String) value).trim();
			main.put(entry.getKey(), value);
		}
		return main;
	}

	/**
	 * Retorna el valor almacenado, en caso de no estar contenido, retorna el
	 * valor por defecto.
	 * 
	 * @param key
	 *            llave a buscar
	 * @param def
	 *            valor por defecto para retornar
	 * @return valor almacenado
	 */
	public String get(final String key, final String def) {

		if (propertiesMap.containsKey(key)) {
			return propertiesMap.get(key);
		}
		return def;
	}

	/**
	 * Parsea la cadena intentando convertirla a un booleano.
	 * 
	 * <p>
	 * Retorna el valor almacenado, en caso de no estar contenido, retorna el
	 * valor por defecto.
	 * </p>
	 * <p>
	 * Los valores posibles son:
	 * <ol>
	 * <li><b>'1'</b> retorna <code>true</code>
	 * <li>
	 * <li><b>'true'</b> retorna <code>true</code>
	 * <li>
	 * <li><b>otro</b> retorna <code>false</code>
	 * <li>
	 * <p>
	 * 
	 * @param key
	 * @param def
	 * @return valor encontrado o def
	 */
	public boolean getBoolean(final String key, boolean def) {

		String defStr = def ? "1" : "0";
		String property = get(key, defStr);

		if ("1".equals(property.trim()) || "true".equals(property.trim())) {
			return true;
		}
		if (defStr.equals(property)) {
			return def;
		}
		return false;
	}

	/**
	 * Parsea la cadena intentando convertirla a un entero.
	 * 
	 * <p>
	 * Retorna el valor almacenado, en caso de no estar contenido, retorna el
	 * valor por defecto.
	 * </p>
	 * 
	 * @param key
	 *            cadena a buscar
	 * @param def
	 *            valor por defecto
	 * @return valor encontrado o def
	 */
	public int getInteger(String key, int def) {

		try {
			return Integer.parseInt(get(key, def + ""));
		} catch (NumberFormatException nfe) {
			throw new KarakuRuntimeException("The key " + key
					+ " doesn't contain a integer", nfe);
		}
	}

	/**
	 * Retorna el valor almacenado, en caso de no estar en el contenido, lanza
	 * una excepcion.
	 * 
	 * @param key
	 * @return valor almacenado
	 */
	public String get(final String key) {

		String toRet = get(key, null);
		if (toRet != null) {
			return toRet.trim();
		}
		throw new KarakuPropertyNotFoundException(key);
	}

	/**
	 * Retorna una instancia de esta clase, este método solo puede ser invocado
	 * dentro de un contexto de JSF.
	 * 
	 * @return {@link PropertiesUtil} para el contexto actual.
	 */
	public static PropertiesUtil getCurrentFromJSF() {

		return Util.getSpringBeanByJSFContext(null, PropertiesUtil.class);
	}
}
