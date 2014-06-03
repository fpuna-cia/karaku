/*
 * @ExampleTest.java 1.0 Aug 19, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.test.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * Base clase para Test que utilicen un contexto de Spring.
 * 
 * <p>
 * La anotación {@literal @}{@link RunWith} se hereda, así que no es necesario
 * reescribirla, en cambio si es necesario que cada clase heredada tenga la
 * anotación {@literal @}{@link ContextConfiguration}
 * </p>
 * 
 * <p>
 * Una clase que desee hacer test deberá ser como sigue:
 * 
 * <pre>
 * {@literal @}{@link ContextConfiguration}(loader = AnnotationConfigContextLoader.class)
 * public class Test extends BaseTest{
 * 
 * 	{@literal @}{@link Configuration}
 * 	static class ContextConfiguration extends {@link BaseTestConfiguration} {
 * 
 * 		{@literal @}{@link Bean}
 * 		public MyBean mybean() {
 * 			return new MyBean();
 * 		}
 * 
 * 	}
 * 
 * 	{@literal @}{@link Autowired}	// Podemos inyectar el bean que creamos más arriba
 * 	MyBean mybean;
 * 
 * 	{@literal @}{@link Test}
 * 	public void testAddInvalidData () {
 * 		...
 * 	}
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * Se utiliza una configuración independiente por cada {@link BaseTest} para
 * intentar que sean lo mas independientes unos de otros. En términos de
 * rendimiento, no hay demasiado detrimento, pues la mayoría de los componentes
 * son cacheados en memoria.
 * </p>
 * <p>
 * Al utilizar una configuración que herede de {@link BaseTestConfiguration} se
 * disponibilizan en el contexto (via {@link Autowired}) la mayoría de los
 * {@link Bean} que forman parte de Karaku.
 * </p>
 * 
 * <p>
 * Esta clase es muy similar a {@link AbstractJUnit4SpringContextTests}, su
 * existencia se debe a que no se conocía la existencia de la citada.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * @see BaseTestConfiguration
 * 
 */
@ActiveProfiles(profiles = { BaseTestConfiguration.TEST_PROFILE })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, })
public abstract class BaseTest implements ApplicationContextAware {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;
	}

}
