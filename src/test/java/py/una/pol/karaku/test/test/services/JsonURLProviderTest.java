/*
 * @JsonURLProviderTset.java 1.0 Oct 21, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.services.client.JsonURLProvider;
import py.una.pol.karaku.services.client.WSInformationProvider;
import py.una.pol.karaku.services.client.WSInformationProvider.Info;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.test.util.TestUtils;

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

	}

	@Test
	public void read() throws IOException {

		WSInformationProvider provider = new JsonURLProvider(TestUtils
				.getSiblingResource(getClass(), "urls.json").getInputStream());
		Info i = provider.getInfoByKey("test");
		assertNotNull(i);
	}

	@Test
	public void getByTag() throws IOException {

		WSInformationProvider provider = new JsonURLProvider(TestUtils
				.getSiblingResource(getClass(), "urls.json").getInputStream());
		List<Info> infos = provider.getInfoByTag("WS_MENU");
		assertThat(infos.size(), is(2));
	}

}
