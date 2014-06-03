/*
 * @MathTest.java 1.0 Sep 26, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.test.test.math;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.math.MathContextProvider;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.math.QuantityNotFitException;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 26, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class QuantityTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Autowired
	MathContextProvider mcp;

	private Quantity fromBD;

	private Quantity p0000;

	private Quantity p0009;
	private Quantity P0010;

	private Quantity d100000;
	private Quantity d200000;

	private Quantity d3p5346;

	@Before
	public void before() {

		fromBD = new Quantity(new BigDecimal("100000000000000"));

		p0000 = new Quantity("0.0000");
		p0009 = new Quantity("0.0009");
		P0010 = new Quantity("0.0010");

		d100000 = new Quantity("100000");
		d200000 = new Quantity("200000");

		d3p5346 = new Quantity("3.5346");
	}

	@Test
	public void testInject() {

		assertNotNull(mcp);
	}

	@Test
	public void testConstructor() {

		assertQuantity(new Quantity(100000), bd("100000"));
		assertQuantity(new Quantity(10000000L), bd("10000000"));
		assertQuantity(new Quantity(new BigDecimal("100000000000000")),
				bd("100000000000000"));
		assertQuantity(new Quantity("0.00004"), bd("0.0000"));
		assertQuantity(new Quantity("0.00005"), bd("0.0001"));
		assertQuantity(new Quantity("0.00094"), bd("0.0009"));
		assertQuantity(new Quantity("0.00095"), bd("0.0010"));
		assertQuantity(new Quantity("100000"), bd("100000"));
		assertQuantity(new Quantity("200000"), bd("200000"));
		assertQuantity(new Quantity("100000.0004"), bd("100000.0004"));

		assertQuantity(new Quantity("3.53456"), bd("3.5346"));

		assertFalse(new Quantity().equals(BigDecimal.ZERO));
	}

	@Test
	public void testAbsAndNegate() {

		assertQuantity(new Quantity("-100").abs(), new Quantity("100"));
		assertQuantity(new Quantity("100").negate(), new Quantity("-100"));
	}

	@Test
	public void testGetValue() {

		assertThat((Integer) new Quantity("1").intValue(), is(1));;
		assertThat((Double) new Quantity("1.5").doubleValue(), is(1.5D));;
		assertThat((Long) new Quantity("100000000000").longValue(),
				is(100000000000L));;
		assertThat((Float) new Quantity("1.5").floatValue(), is(1.5F));;
	}

	@Test(expected = QuantityNotFitException.class)
	public void testLossPrecision() {

		fromBD.intValue();
	}

	@Test(expected = AssertionError.class)
	public void testAssertEqual() {

		assertQuantity(new Quantity("1"), new Quantity());
	}

	@Test
	public void testSum() {

		assertQuantity(p0000.add(p0009), p0009);
		assertQuantity(p0000.add(P0010), P0010);
		assertQuantity(d3p5346.add(d200000), bd("200003.5346"));
		assertQuantity(d3p5346.add(d200000), bd("200003.5346"));

	}

	@Test
	public void testSubtract() {

		assertQuantity(p0000.subtract(p0009), p0009.negate());
		assertQuantity(p0000.subtract(P0010), P0010.negate());
		assertQuantity(p0000.subtract(p0000), BigDecimal.ZERO);
		assertQuantity(d200000.subtract(d100000), d100000);
		assertQuantity(d100000.subtract(d100000), BigDecimal.ZERO);
		assertQuantity(d100000.subtract(d3p5346), bd("99996.4654"));

		assertQuantity(
				new Quantity("100.0059111").subtract(new Quantity("100.0059")),
				BigDecimal.ZERO);

	}

	@Test
	public void testTimesInt() {

		assertQuantity(new Quantity("0.0005").times(2), new Quantity("0.0010"));
		assertQuantity(new Quantity("5").times(20), new Quantity("100"));
		assertQuantity(new Quantity("100000000.99").times(100), new Quantity(
				"10000000099"));
		assertQuantity(new Quantity(".123456789").times(100000), bd("12350"));
	}

	@Test
	public void testTimesQuantity() {

		assertQuantity(new Quantity("0.0005").times(new Quantity("2")),
				new Quantity("0.0010"));
		assertQuantity(new Quantity("5").times(new Quantity("20")),
				new Quantity("100"));
		assertQuantity(new Quantity("100000000.99").times(new Quantity("100")),
				new Quantity("10000000099"));
	}

	@Test
	public void testDivideInt() {

		assertQuantity(new Quantity("0.0003").divide(2), new Quantity("0.0002"));
		assertQuantity(new Quantity("5").divide(20), new Quantity("0.25"));
		assertQuantity(new Quantity("999").divide(100), new Quantity("9.99"));
	}

	@Test
	public void testDivideQuantity() {

		assertQuantity(new Quantity("0.0003").divide(new Quantity("0.0003")),
				new Quantity("1"));
		assertQuantity(new Quantity("5").divide(new Quantity(10D)),
				new Quantity("0.5"));
		assertQuantity(new Quantity("999").divide(new Quantity(100D)),
				new Quantity("9.99"));
	}

	@Test
	public void negateAndPlusTest() {
		Quantity plus = Quantity.ONE;
		Quantity negate = new Quantity("-1");
		assertTrue(plus == plus.abs());
		assertTrue(negate == negate.negate());
		assertEquals(plus, negate.abs());
		assertEquals(negate, plus.negate());
		
	}
	private BigDecimal bd(String string) {

		return new BigDecimal(string);
	}

	private void assertQuantity(Quantity actual, BigDecimal bd) {

		int result = bd.compareTo(actual.bigDecimalValue());
		if (result != 0) {
			throw new ComparisonFailure("", actual.toString(), bd.toString());
		}
	}

	private void assertQuantity(Quantity actual, Quantity expected) {

		assertEquals(expected, actual);
	}
}
