/*
 * @QuantityConverterTest.java 1.0 Nov 27, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.math;

import static org.junit.Assert.assertEquals;
import javax.faces.context.FacesContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.jsf.converters.QuantityConverter;
import py.una.med.base.math.Quantity;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.util.FormatProvider;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 27/11/2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class QuantityConverterTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	QuantityConverter converter;

	@Autowired
	FormatProvider formatProvider;

	@Before
	public void init() {

		converter = new QuantityConverter() {

			@Override
			protected FormatProvider getFormatProvider(FacesContext context) {

				return formatProvider;
			}
		};
	}

	@Test
	public void testAsObject() throws Exception {

		assertEquals(converter.getAsObject(null, null, "1.0"), new Quantity(
				"10"));
		assertEquals(converter.getAsObject(null, null, "10.000"), new Quantity(
				"10000"));
		assertEquals(converter.getAsObject(null, null, "25.500"), new Quantity(
				"25500"));

	}

	@Test
	public void testAsString() throws Exception {

		assertEquals(converter.getAsString(null, null, new Quantity("10")),
				"10");
		assertEquals(converter.getAsString(null, null, new Quantity("10000")),
				"10.000");
		assertEquals(converter.getAsString(null, null, new Quantity("350000")),
				"350.000");

	}
}
