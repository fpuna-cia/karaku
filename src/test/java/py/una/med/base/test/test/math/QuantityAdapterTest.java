/*
 * @QuantityAdapterTest.java 1.0 Oct 14, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.adapter.QuantityAdapter;
import py.una.med.base.math.Quantity;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 14, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class QuantityAdapterTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Test
	public void marshal() {

		assertEquals("1", QuantityAdapter.marshal(Quantity.ONE));
		assertEquals("0", QuantityAdapter.marshal(Quantity.ZERO));
		assertEquals("1,12", QuantityAdapter.marshal(new Quantity("1.1234")));
		assertEquals("-1", QuantityAdapter.marshal(Quantity.ONE.negate()));
	}

	@Test
	public void unMarshal() {

		assertEquals(Quantity.ONE, QuantityAdapter.unmarshal("1"));
		assertEquals(Quantity.TWO, QuantityAdapter.unmarshal("00000000000002"));
		assertEquals(Quantity.ONE, QuantityAdapter.unmarshal("1,000012345"));
		assertEquals(new Quantity("1000258.7"),
				QuantityAdapter.unmarshal("1.000.258,7"));
		assertEquals(Quantity.TWO, QuantityAdapter.unmarshal("1,99999"));
		assertEquals(Quantity.ONE.negate(), QuantityAdapter.unmarshal("-1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException() throws Exception {

		QuantityAdapter.unmarshal("No Number");

	}
}
