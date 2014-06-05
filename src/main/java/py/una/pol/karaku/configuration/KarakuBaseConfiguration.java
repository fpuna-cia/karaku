/**
 * @KarakuBaseConfiguration 1.0 25/03/13. Sistema Integral de Gestion
 *                          Hospitalaria
 */
package py.una.pol.karaku.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import py.una.pol.karaku.math.MathContextProvider;
import py.una.pol.karaku.services.util.NumberAdapter;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.Util;

/**
 * Clase de configuración de la aplicación.
 * 
 * @author Arturo Volpe
 * @since 1.1
 * @version 1.0
 * 
 */
@Configuration
public class KarakuBaseConfiguration {

	private Logger log = LoggerFactory.getLogger(KarakuBaseConfiguration.class);

	// @Autowired
	private static PropertiesUtil propertiesUtil;

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
	 * Para que un {@link org.springframework.stereotype.Component} que forme
	 * parte de este contexto continue con vida, es suficiente con que haya una
	 * referencia al mismo en la página xhtml.
	 * </p>
	 */
	public static final String SCOPE_CONVERSATION = "conversation.access";

	/**
	 * Define un scope de conversación de acceso para el uso de apache
	 * orchestra, esta cadena se configura, además, en el archivo
	 * applicationContext-orchestra.xml
	 * 
	 * <p>
	 * Para que un {@link org.springframework.stereotype.Component} que forme
	 * parte de este contexto continue con vida, es suficiente con que exista,
	 * para eliminarlo, se debe invocar a
	 * {@link org.apache.myfaces.orchestra.conversation.Conversation#invalidate()}
	 * .
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
	 * {@link org.springframework.stereotype.Component} que provee las
	 * propiedades con las que se inicia la aplicación.
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

	@Bean
	public I18nHelper helper() {

		i18nHelper = new I18nHelper();
		return i18nHelper;
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

				return i18nHelper.getString((String) key);
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
	public static final boolean isDevelop() {

		return FacesContext.getCurrentInstance().isProjectStage(
				ProjectStage.Development);
	}

	@Bean
	public MathContextProvider mathContextProvider() {

		return MathContextProvider.INSTANCE;
	}

	/**
	 * Retorna true si el entorno actual de ejecucion es de Debug
	 * 
	 * @return true si se esta debugeando, false si se esta ejecutando
	 *         normalmente
	 */
	public static final boolean isDebug() {

		return java.lang.management.ManagementFactory.getRuntimeMXBean()
				.getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}

	@Bean
	NumberAdapter quantityAdapter() {

		return NumberAdapter.INSTANCE;
	}

}
