/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.test.test.services;

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
import py.una.pol.karaku.services.server.ServiceDefinitionRegister;
import py.una.pol.karaku.services.server.WebServiceDefinition;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

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
	@WebServiceDefinition(xsds = { "/META-INF/schemas/menu/menu.xsd" })
	public static class TestEndPoint {

		@Autowired(required = true)
		TestBean testBean;
	}

	public static class TestBean {

	}
}
