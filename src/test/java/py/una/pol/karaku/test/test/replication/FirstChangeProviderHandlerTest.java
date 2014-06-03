/*
 * @FirstChangeProviderHandlerTest.java 1.0 Dec 4, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.test.test.replication;

import static org.junit.Assert.assertEquals;
import java.util.Set;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.replication.Shareable;
import py.una.med.base.replication.server.FirstChangeProvider;
import py.una.med.base.replication.server.FirstChangeProviderHandler;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

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
