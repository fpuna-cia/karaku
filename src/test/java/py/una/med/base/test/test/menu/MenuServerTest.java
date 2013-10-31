/*
 * @MenuClientTest.java 1.0 Oct 18, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.menu;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.ws.test.server.ResponseMatchers;
import py.una.med.base.menu.schemas.MenuRequest;
import py.una.med.base.menu.schemas.MenuResponse;
import py.una.med.base.menu.server.MenuServerLogic;
import py.una.med.base.menu.server.MenuServiceEndpoint;
import py.una.med.base.test.base.BaseTestWebService;
import py.una.med.base.test.configuration.WebServiceTestConfiguration;
import py.una.med.base.test.util.TestI18nHelper;
import py.una.med.base.test.util.TestPropertiesUtil;
import py.una.med.base.test.util.TestUtils;

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
