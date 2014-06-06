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
package py.una.pol.karaku.test.test.menu;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.ws.test.server.ResponseMatchers;
import py.una.pol.karaku.menu.schemas.MenuRequest;
import py.una.pol.karaku.menu.schemas.MenuResponse;
import py.una.pol.karaku.menu.server.MenuServerLogic;
import py.una.pol.karaku.menu.server.MenuServiceEndpoint;
import py.una.pol.karaku.test.base.BaseTestWebService;
import py.una.pol.karaku.test.configuration.WebServiceTestConfiguration;
import py.una.pol.karaku.test.util.TestI18nHelper;
import py.una.pol.karaku.test.util.TestPropertiesUtil;
import py.una.pol.karaku.test.util.TestUtils;

/**
 *
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 18, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MenuServerTest extends BaseTestWebService {

	@Configuration
	static class ContextConfiguration extends WebServiceTestConfiguration {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?>[] getClassesToBound() {

			return TestUtils.getAsClassArray(MenuRequest.class,
					MenuResponse.class);
		}

		@Bean
		MenuServerLogic menuServerLogic() {

			return new MenuServerLogic();
		}

		@Bean
		MenuServiceEndpoint menuServiceEndpoint() {

			return new MenuServiceEndpoint();
		}

	}

	@Autowired
	TestPropertiesUtil propertiesUtil;

	@Autowired
	TestI18nHelper helper;

	@Before
	public void before() {

		propertiesUtil.put(MenuServerLogic.KARAKU_MENU_LOCATION_KEY,
				TestUtils.getSiblingResourceName(getClass(), "testMenu.xml"));
		helper.addString("TEST_STRING", "test");
	}

	@Test
	public void testCallMocked() {

		MenuRequest mr = new MenuRequest();
		sendRequest(mr).andExpect(ResponseMatchers.noFault());
		MenuResponse response = getResponse();
		assertEquals("test", response.getMenu().getName());
	}
}
