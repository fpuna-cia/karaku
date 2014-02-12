/*
 * @TestConfiguration.java 1.0 Aug 19, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import py.una.med.base.test.util.TestControllerHelper;
import py.una.med.base.util.UniqueHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * 
 */
@Configuration
@Profile(ControllerTestConfiguration.TEST_PROFILE)
public class ControllerTestConfiguration extends BaseTestConfiguration {

	@Bean
	TestControllerHelper controllerHelper() {

		return new TestControllerHelper();
	}

	@Bean
	UniqueHelper object() {

		return new UniqueHelper();
	}

}
