/*
 * @TestConfiguration.java 1.0 Aug 19, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.configuration;

import java.io.IOException;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.server.MockWebServiceClient;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.services.client.JsonURLProvider;
import py.una.med.base.services.client.WSCaller;
import py.una.med.base.services.client.WSInformationProvider;
import py.una.med.base.services.client.WSSecurityInterceptor;
import py.una.med.base.services.server.WebServiceDefinition;
import py.una.med.base.test.util.TestUtils;
import py.una.med.base.test.util.TestWSCaller;

/**
 * Clase base de configuración para test que utilizan
 * {@link WebServiceDefinition}.
 * 
 * <p>
 * Disponibiliza dos beans para serialización/desserialización, los cuales son
 * {@link #marshaller()} y {@link #unmarshaller()} respectivamente.
 * <p>
 * 
 * <p>
 * El método {@link #getClassesToBound()} debe retornar las clases que se pueden
 * marshalizar/desmarshalizar
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * 
 */
public abstract class WebServiceTestConfiguration extends BaseTestConfiguration
		implements AsyncConfigurer {

	private Jaxb2Marshaller marshaller;

	public abstract Class<?>[] getClassesToBound();

	private Logger log = LoggerFactory
			.getLogger(WebServiceTestConfiguration.class);

	@Bean
	Marshaller marshaller() {

		return jaxb2Marshaller();
	}

	@Bean
	Unmarshaller unmarshaller() {

		return jaxb2Marshaller();
	}

	/**
	 * @return
	 */
	private Jaxb2Marshaller jaxb2Marshaller() {

		if (marshaller == null) {
			marshaller = new Jaxb2Marshaller();
			marshaller.setClassesToBeBound(getClassesToBound());
		}
		return marshaller;
	}

	@Bean
	WSSecurityInterceptor wsSecurityInterceptor() {

		return new WSSecurityInterceptor();
	}

	@Override
	public Executor getAsyncExecutor() {

		return executor();
	}

	@Bean
	Executor executor() {

		return new SyncTaskExecutor();
	}

	@Bean
	public WebServiceTemplate webServiceTemplate() {

		WebServiceTemplate wst = new WebServiceTemplate();
		wst.setMarshaller(marshaller());
		wst.setUnmarshaller(unmarshaller());
		return wst;

	}

	@Bean
	@Autowired
	public MockWebServiceClient mockWebServiceClient(
			ApplicationContext applicationContext) {

		return MockWebServiceClient.createClient(applicationContext);
	}

	@Bean
	WSCaller wsCaller() {

		return new TestWSCaller();
	}

	@Bean
	protected WSInformationProvider wsInformationProvider() throws IOException {

		try {
			Class<?> clazz = getClass();
			if (getClass().getEnclosingClass() != null) {
				clazz = getClass().getEnclosingClass();
			}
			return new JsonURLProvider(TestUtils.getSiblingResource(clazz,
					"urls.json").getInputStream());
		} catch (KarakuRuntimeException kre) {
			log.debug(
					"Can not find the file urls.json for this test, disabling urlprovider (%s)",
					kre.getMessage());
			return null;
		} catch (IOException io) {
			log.debug(
					"Can not find the file urls.json for this test, disabling urlprovider (%s)",
					io.getMessage());
			return null;
		}
	}
}
