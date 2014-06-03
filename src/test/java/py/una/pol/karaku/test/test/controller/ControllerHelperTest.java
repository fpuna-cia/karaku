/*
 * @ControllerHelperTest.java 1.0 May 8, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.controller;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.test.util.TestControllerHelper;
import py.una.pol.karaku.test.util.TestI18nHelper;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.UniqueHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 8, 2014
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Ignore
public class ControllerHelperTest extends BaseTest {

	private static final String M4 = "4";

	private static final String M3 = "3";

	private static final String M2 = "2";

	private static final String M1 = "1";

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		ControllerHelper c() {

			return new TestControllerHelper();
		}

		@Bean
		UniqueHelper helper() {

			return new UniqueHelper();
		}

	}

	@Autowired
	private TestControllerHelper h;

	@Autowired
	private TestI18nHelper helper;

	@Before
	public void before() {

		helper.addString(M1, "uno");
		helper.addString(M2, "{}");
		helper.addString(M3, "uno {} uno");
		helper.addString(M4, "uno {} uno {}");
	}

	@Test
	public void testCreateMessages() throws Exception {

		h.addInfoMessage(M1);
		check(null, "uno", null);

		h.addInfoMessage(M2, "1");
		check(null, "1", null);

	}

	private void check(String id, String summary, String detail) {

		assertEquals("Wrong id", id, h.getLastMessageComponentId());
		assertEquals("Wrond summary", summary, h.getLastMessage().getSummary());
		assertEquals("Wrong detail", detail, h.getLastMessage().getDetail());
	}

}
