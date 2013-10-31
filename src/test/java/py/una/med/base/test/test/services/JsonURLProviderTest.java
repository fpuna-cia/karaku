/*
 * @JsonURLProviderTset.java 1.0 Oct 21, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.services.client.JsonURLProvider;
import py.una.med.base.services.client.WSInformationProvider;
import py.una.med.base.services.client.WSInformationProvider.Info;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.test.util.TestPropertiesUtil;
import py.una.med.base.test.util.TestUtils;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 21, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class JsonURLProviderTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		WSInformationProvider wsInformationProvider() {

			return new JsonURLProvider();
		}
	}

	@Autowired
	TestPropertiesUtil util;

	@Autowired
	WSInformationProvider provider;

	@Before
	public void before() {

		util.put("karaku.menu.json_urls",
				TestUtils.getSiblingResourceName(getClass(), "urls.json"));
	}

	@Test
	public void read() {

		Info i = provider.getInfoByKey("test");
		assertNotNull(i);
	}

	@Test
	public void getByTag() {

		List<Info> infos = provider.getInfoByTag("WS_MENU");
		assertThat(infos.size(), is(2));
	}

}
