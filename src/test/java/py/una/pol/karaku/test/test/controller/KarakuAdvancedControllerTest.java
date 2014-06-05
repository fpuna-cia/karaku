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
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.controller.KarakuAdvancedController;
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
public class KarakuAdvancedControllerTest extends BaseControllerTest {

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
			KarakuAdvancedController<AuditTrail, Long> {

		@Override
		public IKarakuBaseLogic<AuditTrail, Long> getBaseLogic() {

			return null;
		}

	}
}
