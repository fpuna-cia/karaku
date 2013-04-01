/**
 * @PropertiesUtil 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

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

	@Override
	protected void processProperties(
			final ConfigurableListableBeanFactory beanFactory,
			final Properties props) throws BeansException {

		super.processProperties(beanFactory, props);
		if (propertiesMap == null) {
			propertiesMap = new HashMap<String, String>();
		}
		for (Object key : props.keySet()) {
			String keyStr = key.toString();

			propertiesMap.put(keyStr, props.getProperty(keyStr));
		}
	}

	public String getProperty(final String name) {

		return propertiesMap.get(name);
	}
}
