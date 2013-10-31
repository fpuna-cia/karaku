/*
 * @RegisterServicesTest.java 1.0 Oct 18, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import py.una.med.base.services.server.ServiceDefinitionRegister;
import py.una.med.base.services.server.WebServiceDefinition;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 18, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class RegisterServicesTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		public ServiceDefinitionRegister serviceDefinitionRegister() {

			return new ServiceDefinitionRegister();
		}

		@Bean
		public TestEndPoint testEndPoint() {

			return new TestEndPoint();
		}

		@Bean
		public TestBean testBean() {

			return new TestBean();
		}

	}

	@Autowired
	TestEndPoint testEndPoint;

	@Autowired
	TestBean testBean;

	@Test
	public void testRegister() {

		Map<String, DefaultWsdl11Definition> map = applicationContext
				.getBeansOfType(DefaultWsdl11Definition.class);
		assertNotNull(testEndPoint.testBean);

		assertTrue(map.containsKey("test"));

		assertNotNull(map.get("test"));

	}

	@Component
	@WebServiceDefinition(xsds = { "/META-INF/schemas/karaku/menu/menu.xsd" })
	public static class TestEndPoint {

		@Autowired(required = true)
		TestBean testBean;
	}

	public static class TestBean {

	}
}
