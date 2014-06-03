/*
 * @SIGHAdvancedControllerTest.java 1.0 May 26, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.business.ISIGHBaseLogic;
import py.una.pol.karaku.controller.SIGHAdvancedController;
import py.una.pol.karaku.domain.AuditTrail;
import py.una.pol.karaku.exception.UniqueConstraintException;
import py.una.pol.karaku.test.base.BaseControllerTest;
import py.una.pol.karaku.test.configuration.ControllerTestConfiguration;
import py.una.pol.karaku.test.util.TestControllerHelper;
import py.una.pol.karaku.test.util.TestI18nHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 26, 2014
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SIGHAdvancedControllerTest extends BaseControllerTest {

	@Configuration
	static class ContextConfiguration extends ControllerTestConfiguration {

		@Bean
		public TestController testController() {

			return new TestController();
		}

	}

	@Autowired
	private TestController controller;

	@Autowired
	private TestControllerHelper helper;

	@Autowired
	private TestI18nHelper i18nHelper;

	@Before
	public void befor() {

		i18nHelper.addString("FIELD_DUPLICATE", "1");
		i18nHelper.addString("FIELD_DUPLICATE_DETAIL", "2");
		i18nHelper.addString("FIELDS_DUPLICATED", "2");
		i18nHelper.addString("FIELDS_DUPLICATED_DETAIL", "2 {} {}");
	}

	@Test
	public void testHandleException() throws Exception {

		UniqueConstraintException uce = new UniqueConstraintException("bla",
				"descripcion");

		assertTrue(controller.handleException(uce));

		assertEquals("1", helper.getLastMessage().getSummary());
		assertEquals("2", helper.getLastMessage().getDetail());
		assertEquals("descripcion", helper.getLastMessageComponentId());

		UniqueConstraintException uce2 = new UniqueConstraintException("ble",
				"descripcion", "pais");
		assertTrue(controller.handleException(uce2));
		assertEquals("2", helper.getLastMessage().getSummary());
		assertEquals("2 descripcion pais", helper.getLastMessage().getDetail());
		assertEquals(null, helper.getLastMessageComponentId());

	}

	static class TestController extends
			SIGHAdvancedController<AuditTrail, Long> {

		@Override
		public ISIGHBaseLogic<AuditTrail, Long> getBaseLogic() {

			return null;
		}

	}
}
