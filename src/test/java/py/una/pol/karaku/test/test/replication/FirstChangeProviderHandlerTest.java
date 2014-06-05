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
package py.una.pol.karaku.test.test.replication;

import static org.junit.Assert.assertEquals;
import java.util.Set;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.replication.server.FirstChangeProvider;
import py.una.pol.karaku.replication.server.FirstChangeProviderHandler;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 4, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class FirstChangeProviderHandlerTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		FirstChangeProviderHandler handler() {

			return new FirstChangeProviderHandler();
		}

		@Bean
		ProviderAll all() {

			return new ProviderAll();
		}

		@Bean
		ProviderClass2 providerClass2() {

			return new ProviderClass2();
		}

	}

	@Autowired
	private FirstChangeProviderHandler handler;

	@Autowired
	private ProviderAll all;

	@Autowired
	private ProviderClass2 class2;

	@Test
	public void testRecoverty() throws Exception {

		assertEquals(all, handler.getChangeProvider(Shareable.class));
		assertEquals(class2, handler.getChangeProvider(Class2.class));
	}

	static class ProviderAll implements FirstChangeProvider<Shareable> {

		@Override
		public Set<Shareable> getChanges(Class<? extends Shareable> clazz) {

			return null;
		}

		@Override
		public Class<Shareable> getSupportedClass() {

			return Shareable.class;
		}

		@Override
		public Integer getPriority() {

			return Integer.MIN_VALUE;
		}

	}

	static class Class2 implements Shareable {

		@Override
		public String getUri() {

			return null;
		}

		@Override
		public void inactivate() {

		}

		@Override
		public void activate() {

		}

		@Override
		public boolean isActive() {

			return false;
		}

	}

	static class ProviderClass2 implements FirstChangeProvider<Class2> {

		@Override
		public Set<Class2> getChanges(Class<? extends Class2> clazz) {

			return null;
		}

		@Override
		public Class<Class2> getSupportedClass() {

			return Class2.class;
		}

		@Override
		public Integer getPriority() {

			return 1;
		}
	}
}
