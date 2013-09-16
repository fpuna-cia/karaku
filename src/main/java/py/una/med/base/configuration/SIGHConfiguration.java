/**
 * @SIGHConfiguration 1.0 25/03/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.myfaces.orchestra.conversation.Conversation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import py.una.med.base.domain.Menu;
import py.una.med.base.domain.Menu.Menus;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.MenuHelper;
import py.una.med.base.util.Util;

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
	private ApplicationContext applicationContext;

	private Logger log = LoggerFactory.getLogger(SIGHConfiguration.class);

	// @Autowired
	private static PropertiesUtil propertiesUtil;

	@Autowired
	private I18nHelper i18nHelper;

	@Autowired
	private Util util;

	/**
	 * Imprime un mensaje de bienvenida
	 */
	@PostConstruct
	public void postConstruct() {

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(" _   __                _          ").append("\n");
		sb.append("| | / /               | |         ").append("\n");
		sb.append("| |/ /  __ _ _ __ __ _| | ___   _ ").append("\n");
		sb.append("|    \\ / _` | '__/ _` | |/ / | | |").append("\n");
		sb.append("| |\\  \\ (_| | | | (_| |   <| |_| |").append("\n");
		sb.append("\\_| \\_/\\__,_|_|  \\__,_|_|\\_\\\\__,_|").append("\n");
		sb.append("\n");
		sb.append("Sistema: ").append(util.getNameSystem().trim()).append("\n");
		sb.append("Version: ").append(util.getVersion().trim()).append("\n");
		log.info(sb.toString());

	}

	/**
	 * Define un scope de conversación de acceso para el uso de apache
	 * orchestra, esta cadena se configura, además, en el archivo
	 * applicationContext-orchestra.xml.
	 * <p>
	 * Para que un {@link Component} que forme parte de este contexto continue
	 * con vida, es suficiente con que haya una referencia al mismo en la página
	 * xhtml.
	 * </p>
	 */
	public static final String SCOPE_CONVERSATION = "conversation.access";

	/**
	 * Define un scope de conversación de acceso para el uso de apache
	 * orchestra, esta cadena se configura, además, en el archivo
	 * applicationContext-orchestra.xml
	 * 
	 * <p>
	 * Para que un {@link Component} que forme parte de este contexto continue
	 * con vida, es suficiente con que exista, para eliminarlo, se debe invocar
	 * a {@link Conversation#invalidate()}.
	 * </p>
	 */
	public static final String SCOPE_CONVERSATION_MANUAL = "conversation.manual";
	/**
	 * Clave del archivo de propiedades de la ubicación de los archivos de
	 * internacionalización
	 */
	public static final String LANGUAGE_BUNDLES_KEY = "language_bundles";

	/**
	 * Ubicación del archivo de configuraciones
	 */
	public static final String CONFIG_LOCATION = "karaku.properties";

	/**
	 * {@link Component} que provee las propiedades con las que se inicia la
	 * aplicación.
	 * <p>
	 * Las mismas se definen en {@link #CONFIG_LOCATION}.
	 * </p>
	 * 
	 * @return {@link PropertiesUtil} base de karaku.
	 * @see PropertiesUtil
	 */
	@Bean
	public static PropertiesUtil propertyPlaceholder() {

		propertiesUtil = new PropertiesUtil();
		propertiesUtil.setLocation(new ClassPathResource(CONFIG_LOCATION));
		return propertiesUtil;
	}

	/**
	 * Construye una instancia de {@link Menus}.
	 * 
	 * @return nueva instancia de Menus
	 * @throws IOException
	 *             si el archivo no existe
	 * @throws JAXBException
	 *             si no se puede parsear el archivo
	 */
	@Bean
	public MenuHelper menuHelper() throws IOException, JAXBException {

		// XXX mejorar mexclar arboles
		Menus toRet = new Menus();
		String menus = propertiesUtil.getProperty("menu_location");
		String[] menusPath = menus.split("\\s+");
		for (String menuPath : menusPath) {
			Resource resource = new ClassPathResource(menuPath);
			InputStream inputStream;
			JAXBContext jaxbContext;
			inputStream = resource.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			jaxbContext = JAXBContext.newInstance(Menus.class);
			Unmarshaller um = jaxbContext.createUnmarshaller();
			Menus toConcat = (Menus) um.unmarshal(reader);

			cleanMenuID(resource.getFilename(), toConcat);
			toRet.getMenus().addAll(toConcat.getMenus());
		}

		MenuHelper mh = new MenuHelper(toRet);
		return mh;
	}

	private void cleanMenuID(String file, Menus loaded) {

		int location = file.indexOf(".");
		String pre;
		if (location <= 0) {
			pre = file;
		} else {
			pre = file.substring(0, location);
		}
		for (Menu m : loaded.getMenus()) {
			m.setId(pre + m.getId());
			if (m.getIdFather() != null && !"".equals(m.getIdFather())) {
				m.setIdFather(pre + m.getIdFather());
			}
		}
	}

	/**
	 * Crea un {@link Map} que contiene las cadenas de internacionalización
	 * actuales.
	 * 
	 * @return Bean para internacionalización.
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
