/**
 * @SIGHConfiguration 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import py.una.med.base.domain.Menu.Menus;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.MenuHelper;

/**
 * Clase de configuración de la aplicación.
 * 
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.0
 * 
 */
@Configuration
public class SIGHConfiguration {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	PropertiesUtil propertiesUtil;

	@Autowired
	I18nHelper i18nHelper;
	/**
	 * Define un scope de conversacion de acceso para el uso de apache
	 * orchestra, esta cadena se configura, ademas, en el archvo
	 * applicationContext-orchestra.xml
	 */
	public static final String SCOPE_CONVERSATION = "conversation.access";
	public static final String SCOPE_CONVERSATION_MANUAL = "conversation.manual";

	/**
	 * Ubicacion del archivo de configuraciones
	 */
	public static final String CONFIG_LOCATION = "config.properties";

	/**
	 * Construye una instancia de {@link Menus}.
	 * 
	 * @return nueva instancia de Menus
	 * @throws Exception
	 *             imposible leer archivo
	 */
	@Bean
	public Menus getMenu() throws Exception {

		// XXX mejorar mexclar arboles
		Menus toRet = new Menus();
		String menus = propertiesUtil.getProperty("menu_location");
		String[] menusPath = menus.split(" ");
		for (String menuPath : menusPath) {
			Resource resource = new ClassPathResource(menuPath);
			InputStream inputStream;
			JAXBContext jaxbContext;
			inputStream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			jaxbContext = JAXBContext.newInstance(Menus.class);
			Unmarshaller um = jaxbContext.createUnmarshaller();
			Menus toConcat = MenuHelper.createHierarchy(((Menus) um
					.unmarshal(reader)));
			toRet.menus.addAll(toConcat.menus);
		}
		return toRet;
	}

	/**
	 * <resource-bundle> <base-name>../language.properties.base</base-name>
	 * <var>_b</var> </resource-bundle>
	 * 
	 * <resource-bundle>
	 * 
	 * <base-name>../language.properties.farmacia</base-name> <var>msg</var>
	 * </resource-bundle>
	 * 
	 * @return
	 */
	@Bean(name = "msg")
	public Map<String, String> getLanguagesMap() {

		return new HashMap<String, String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String get(Object key) {

				String sKey = (String) key;
				return i18nHelper.getString(sKey);
			}
		};
	}

	/**
	 * Retorna true si la aplicacion esta en estado de desarrollo y false si
	 * esta en otro estado.
	 * 
	 * @see ProjectStage
	 * @return true si es develop, false en otro caso
	 */
	public final static boolean isDevelop() {

		return FacesContext.getCurrentInstance().isProjectStage(
				ProjectStage.Development);
	}

	/**
	 * Retorna true si el entorno actual de ejecucion es de Debug
	 * 
	 * @return true si se esta debugeando, false si se esta ejecutando
	 *         normalmente
	 */
	public final static boolean isDebug() {

		return java.lang.management.ManagementFactory.getRuntimeMXBean()
				.getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}

}
