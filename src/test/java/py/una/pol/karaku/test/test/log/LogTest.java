/*
 * @LogTest.java 1.0 Sep 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.test.test.log;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Test sencillo que prueba que un test funcione correctamente.
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 11, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class LogTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Log
	Logger log;
	
	@Log
	public void setLog(Logger log) {
		assertNotNull(log);
	}

	/**
	 * Verifica si se importa correctamente
	 */
	@Test
	public void testLog() {

		assertNotNull(log);

	}

	/**
	 * Verifica si la configuración es correcta.
	 */
	@Test
	public void testConfiguration() {

		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// print logback's internal status

		StringBuilder sb = new StringBuilder();
		StatusManager sm = lc.getStatusManager();

		for (Status s : sm.getCopyOfStatusList()) {
			StatusPrinter.buildStr(sb, "", s);
		}
		assertThat(sb.toString(), containsString("Found resource"));
	}
}
