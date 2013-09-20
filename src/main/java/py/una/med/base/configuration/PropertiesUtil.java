/**
 * @PropertiesUtil 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.faces.context.FacesContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import py.una.med.base.exception.KarakuPropertyNotFoundException;
import py.una.med.base.exception.KarakuWrongConfigurationFileException;
import py.una.med.base.util.Util;

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
	private static final String ANOTHER_KEY = "karaku.changing.properties";

	@Override
	protected void processProperties(
			final ConfigurableListableBeanFactory beanFactory,
			final Properties props) throws BeansException {

		super.processProperties(beanFactory, mergeProperties(props));
		if (propertiesMap == null) {
			propertiesMap = new HashMap<String, String>();
		}
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
	private Properties mergeProperties(Properties main) {

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
			if (entry.getValue() instanceof String) {
				value = ((String) value).trim();
			}
			main.put(entry.getKey(), value);
		}
		return main;
	}

	/**
	 * Retorna el valor almacenado, en caso de no estar contenido, retorna el
	 * valor por defecto.
	 * 
	 * @param key
	 * @param def
	 * @return valor almacenado
	 */
	public String get(final String key, final String def) {

		if (propertiesMap.containsKey(key)) {
			return propertiesMap.get(key);
		}
		return def;
	}

	/**
	 * Retorna el valor almacenado, en caso de no estar en el contenido, lanza
	 * una excepcion.
	 * 
	 * @param key
	 * @return valor almacenado
	 */
	public String get(final String key) {

		if (propertiesMap.containsKey(key))
			return propertiesMap.get(key);
		else
			throw new KarakuPropertyNotFoundException(key);
	}

	public String getProperty(final String name) {

		return propertiesMap.get(name);
	}

	/**
	 * Retorna una instancia de esta clase, este método solo puede ser invocado
	 * dentro de un contexto de JSF.
	 * 
	 * @return {@link PropertiesUtil} para el contexto actual.
	 */
	public static PropertiesUtil getCurrentFromJSF() {

		return Util.getSpringBeanByJSFContext(
				FacesContext.getCurrentInstance(), PropertiesUtil.class);
	}
}
