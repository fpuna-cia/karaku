/*
 * @ExampleTest.java 1.0 Aug 19, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.base;

import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import py.una.med.base.test.configuration.BaseTestConfiguration;

/**
 * 
 * TODO documentar
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 */
@ActiveProfiles(profiles = { BaseTestConfiguration.TEST_PROFILE })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, })
public abstract class BaseTest implements ApplicationContextAware {

	protected ApplicationContext applicationContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;
	}

}
