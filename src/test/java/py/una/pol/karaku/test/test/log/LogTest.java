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
