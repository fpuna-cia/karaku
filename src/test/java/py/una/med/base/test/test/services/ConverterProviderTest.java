/*
 * @ConverterProviderTest.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.services;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.Shareable;
import py.una.med.base.services.AbstractConverter;
import py.una.med.base.services.Converter;
import py.una.med.base.services.ConverterProvider;
import py.una.med.base.services.ReflectionConverter;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ConverterProviderTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		ConverterProvider converterProvider() {

			return new ConverterProvider();
		}

		@Bean
		Converter1 converter1() {

			return new Converter1();
		}

		@Bean
		Converter2 converter2() {

			return new Converter2();
		}

	}

	@Autowired
	private ConverterProvider converterProvider;

	@Test
	public void testGetDefault() throws Exception {

		assertTrue(converterProvider.getConverter(Shareable.class, DTO.class) instanceof ReflectionConverter);

	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testGetContextConverters() throws Exception {

		Converter c = converterProvider.getConverter(Sharable1.class,
				DTO1.class);
		assertTrue(c.getClass().equals(Converter1.class));

		c = converterProvider.getConverter(Sharable2.class, DTO2.class);
		assertTrue(c.getClass().equals(Converter2.class));
	}

	static class Converter1 extends AbstractConverter<Sharable1, DTO1> {

	}

	static class Converter2 extends AbstractConverter<Sharable2, DTO2> {

	}

	static class Sharable1 implements Shareable {

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

	static class Sharable2 implements Shareable {

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

	static class DTO1 implements DTO {

		@Override
		public String getUri() {

			return null;
		}

		@Override
		public boolean isActive() {

			return false;
		}

	}

	static class DTO2 implements DTO {

		@Override
		public String getUri() {

			return null;
		}

		@Override
		public boolean isActive() {

			return false;
		}

	}
}
