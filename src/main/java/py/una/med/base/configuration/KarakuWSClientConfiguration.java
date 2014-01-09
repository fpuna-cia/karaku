/*
 * @KarakuWSClientConfiguration.java 1.0 Jun 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.services.client.EntityURLProvider;
import py.una.med.base.services.client.JsonURLProvider;
import py.una.med.base.services.client.WSInformationProvider;

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
	private static final String KARAKU_WS_CLIENT_PACKAGES_TO_SCAN_SPACES = "karaku.ws.client.packages_to_scan.with_spaces";
	private static final String KARAKU_WS_CLIENT_ENABLED = "karaku.ws.client.enabled";

	@Autowired
	private PropertiesUtil properties;

	/**
	 * Instancia un nuevo bean del Tipo {@link WebServiceTemplate}, utilizado
	 * para realizar llamadas del tipo SOAP.
	 * 
	 * @return {@link WebServiceTemplate} para realizar llamadas
	 */
	@Bean
	public WebServiceTemplate webServiceTemplate() {

		if (properties.get(KARAKU_WS_CLIENT_ENABLED).equals("false")) {
			return null;
		}

		try {
			WebServiceTemplate wst = new WebServiceTemplate();
			wst.setMarshaller(getJaxb2Marshaller());
			wst.setUnmarshaller(getJaxb2Marshaller());
			return wst;
		} catch (Exception e) {
			throw new KarakuRuntimeException(
					"Wrong version of WS dependencies, please check your pom",
					e);
		}

	}

	/**
	 * Define el tipo de proveedor de URl que a ser utilizado.
	 * 
	 * Si la persistencia esta activa se retorna un {@link EntityURLProvider}
	 * que busca en la base de datos. En caso contrario retorna un
	 * {@link JsonURLProvider}.
	 * 
	 * @return {@link WSInformationProvider}
	 * @throws Exception
	 */
	@Bean
	WSInformationProvider wsInformationProvider() throws IOException {

		if (properties.getBoolean(KarakuPersistence.KARAKU_JPA_ENABLED, true)) {
			return new EntityURLProvider();
		}
		String url = properties.get("karaku.menu.json_urls", "urls.json");
		InputStream is;
		if (url.startsWith("/")) {
			is = new FileInputStream(url);
		} else {
			is = new ClassPathResource(properties.get("karaku.menu.json_urls",
					"urls.json")).getInputStream();
		}
		return new JsonURLProvider(is);
	}

	/**
	 * Crea un bean para ser utilizado como marshaller (serializador). <br>
	 * Utiliza
	 * 
	 * <pre>
	 * {@literal
	 * 	<bean id="marshaller" class="org.springframework.oxm.jaxb.Jaxb2Marshaller">
	 * 		<property name="packagesToScan">
	 * 			<list>
	 * 				<value>py.una.med.identificacion.ws.schema</value>
	 * 				<value>py.una.med.configuracion.ws.schema</value>
	 * 			</list>
	 * 		</property>
	 * 	</bean>}
	 * </pre>
	 */
	@Bean
	public Marshaller marshaller() {

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
	public Unmarshaller unmarshaller() {

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

		addSpecificPackages(packagesFound);

		return instanciateMarshaller(packagesFound);
	}

	/**
	 * Agrega los paquetes definidos en la propiedad
	 * {@link #KARAKU_WS_CLIENT_PACKAGES_TO_SCAN_SPACES}.
	 * 
	 * @param packagesFound
	 *            paquetes en donde se deben agregar
	 */
	private void addSpecificPackages(List<String> packagesFound) {

		String paquetes = properties.get(
				KARAKU_WS_CLIENT_PACKAGES_TO_SCAN_SPACES, "").trim();
		if (!"".equals(paquetes)) {
			for (String pa : paquetes.split("\\s+")) {
				packagesFound.add(pa);
			}
		}
	}

	/**
	 * Crea un nuevo marhsaller.
	 * 
	 * @param packagesFound
	 *            lista de paquetes.
	 * @return
	 */
	private Jaxb2Marshaller instanciateMarshaller(List<String> packagesFound) {

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
