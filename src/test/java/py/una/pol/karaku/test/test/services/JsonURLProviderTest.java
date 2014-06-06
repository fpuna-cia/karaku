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
