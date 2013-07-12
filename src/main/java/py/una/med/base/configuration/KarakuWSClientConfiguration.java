/*
 * @KarakuWSClientConfiguration.java 1.0 Jun 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 7, 2013
 * 
 */
@Configuration
public class KarakuWSClientConfiguration {

	/**
	 * 
	 */
	private static final String DEFAULT_PACKAGES_TO_SCAN_EXPRESSION = "[\\w\\.]*services\\.schemas";

	private final Logger log = LoggerFactory
			.getLogger(KarakuWSClientConfiguration.class);

	/**
	 * 
	 */
	private static final String KARAKU_WS_CLIENT_PACKAGES_TO_SCAN = "karaku.ws.client.packages_to_scan";
	private static final String KARAKU_WS_CLIENT_ENABLED = "karaku.ws.client.enabled";

	@Autowired
	private PropertiesUtil properties;

	/**
	 * 
	 */
	public KarakuWSClientConfiguration() {

	}

	@Bean
	public WebServiceTemplate webServiceTemplate() {

		if (properties.get(KARAKU_WS_CLIENT_ENABLED).equals("false")) {
			return null;
		}

		try {
			Class<?> clazz = Class
					.forName("org.springframework.ws.client.core.WebServiceTemplate");
			Class<?> clazzMarshaller = Class
					.forName("org.springframework.oxm.Marshaller");
			Class<?> clazzUnMarshaller = Class
					.forName("org.springframework.oxm.Unmarshaller");

			Object o = clazz.newInstance();
			clazz.getMethod("setMarshaller", clazzMarshaller).invoke(o,
					marshaller());
			clazz.getMethod("setUnmarshaller", clazzUnMarshaller).invoke(o,
					unmarshaller());
			return (WebServiceTemplate) o;
		} catch (ClassNotFoundException e) {
			throw new KarakuRuntimeException(
					"Can not find the WebServiceTemplate base class in the classpath, "
							+ "please, check your pom and add the ws dependencies",
					e);
		} catch (Exception e) {
			throw new KarakuRuntimeException(
					"Wrong version of WS dependencies, please check your pom",
					e);
		}

	}

	/**
	 * Crea un bean para ser utilizado como marshaller (serializador). <br>
	 * Utiliza
	 * 
	 * <pre>
	 * {@literal 
	 * 		<bean id="marshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
	 * 			<property name="packagesToScan">
	 * 				<list>
	 * 					<value>py.una.med.identificacion.ws.schema</value>
	 * 					<value>py.una.med.configuracion.ws.schema</value>
	 * 				</list>
	 * 			</property>
	 * 		</bean>}
	 * </pre>
	 */
	@Bean
	public org.springframework.oxm.Marshaller marshaller() {

		return getJaxb2Marshaller();
	}

	/**
	 * Crea un bean para ser utilizado como unmarshaller (serializador). <br>
	 * Utiliza
	 * 
	 * <pre>
	 * {@literal 
	 * 		<bean id="unmarshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
	 * 			<property name="packagesToScan">
	 * 				<list>
	 * 					<value>py.una.med.identificacion.ws.schema</value>
	 * 					<value>py.una.med.configuracion.ws.schema</value>
	 * 				</list>
	 * 			</property>
	 * 		</bean>}
	 * </pre>
	 */
	@Bean
	public Object unmarshaller() {

		return getJaxb2Marshaller();
	}

	// @SuppressWarnings("unchecked")
	private Jaxb2Marshaller getJaxb2Marshaller() {

		if (!(properties.get(KARAKU_WS_CLIENT_ENABLED).equals("true"))) {
			return null;
		}
		Pattern pattern = Pattern.compile(properties.get(
				KARAKU_WS_CLIENT_PACKAGES_TO_SCAN,
				DEFAULT_PACKAGES_TO_SCAN_EXPRESSION));

		Package[] packages = Package.getPackages();
		List<String> packagesFound = new ArrayList<String>();
		for (Package pa : packages) {
			Matcher matcher = pattern.matcher(pa.getName());
			if (matcher.matches()) {
				log.info("Found a package to add to the marshaller: "
						+ pa.getName());
				packagesFound.add(pa.getName());
			}
		}

		try {

			Class<?> clazz = Class
					.forName("org.springframework.oxm.jaxb.Jaxb2Marshaller");
			Object o = clazz.newInstance();
			String[] clases = packagesFound.toArray(new String[0]);
			clazz.getMethod("setPackagesToScan", String[].class).invoke(o,
					(Object) clases);
			return (Jaxb2Marshaller) o;
		} catch (ClassNotFoundException e) {
			throw new KarakuRuntimeException(
					"Can not find the Jaxb2Marshaller base class in the classpath, "
							+ "please, check your pom and add the ws (oxm) dependencies",
					e);
		} catch (Exception e) {
			throw new KarakuRuntimeException(
					"Wrong version of WS dependencies, please check your pom",
					e);
		}

	}
}
