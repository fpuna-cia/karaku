/*
 * @MenuClientTest.java 1.0 Oct 18, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.menu;

import java.io.IOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.menu.client.AbstractMenuProvider;
import py.una.pol.karaku.menu.client.MenuWSCaller;
import py.una.pol.karaku.menu.client.WSMenuProvider;
import py.una.pol.karaku.menu.schemas.MenuRequest;
import py.una.pol.karaku.menu.schemas.MenuResponse;
import py.una.pol.karaku.menu.server.MenuServerLogic;
import py.una.pol.karaku.menu.server.MenuServiceEndpoint;
import py.una.pol.karaku.services.client.JsonURLProvider;
import py.una.pol.karaku.services.client.WSInformationProvider;
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
public class MenuProviderTest extends BaseTestWebService {

	@Configuration
	@EnableScheduling
	static class ContextConfiguration extends WebServiceTestConfiguration {

		@Bean
		MenuServerLogic menuServerLogic() {

			return new MenuServerLogic();
		}

		@Bean
		MenuServiceEndpoint menuServiceEndpoint() {

			return new MenuServiceEndpoint();
		}

		@Bean
		AbstractMenuProvider menuClientLogic() {

			return new WSMenuProvider();
		}

		@Bean
		@Override
		protected WSInformationProvider wsInformationProvider()
				throws IOException {

			return new JsonURLProvider(TestUtils.getSiblingResource(
					MenuProviderTest.class, "urls.json").getInputStream());
		}

		@Bean
		MenuWSCaller menuWSCaller() {

			return new MenuWSCaller();
		}

		@Override
		public Class<?>[] getClassesToBound() {

			return TestUtils.getAsClassArray(MenuRequest.class,
					MenuResponse.class);
		}

	}

	@Autowired
	TestPropertiesUtil propertiesUtil;

	@Autowired
	TestI18nHelper helper;

	@Autowired
	AbstractMenuProvider menuClientLogic;

	@Before
	public void before() {

		propertiesUtil.put(MenuServerLogic.KARAKU_MENU_LOCATION_KEY,
				TestUtils.getSiblingResourceName(getClass(), "testMenu.xml"));

		helper.addString("TEST_STRING", "test");
	}

	@Test
	@Ignore
	public void testCallMocked() throws InterruptedException {

		Thread.sleep(11000);
		menuClientLogic.getMenu();
	}
}
