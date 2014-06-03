/*
 * @CheckerTest.java 1.0 Nov 25, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.util.Checker;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class CheckerTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Test
	public void testFormat() throws Exception {

		assertEquals("hola hola1 hola2",
				Checker.format("hola %s %s", "hola1", "hola2"));

	}
}
