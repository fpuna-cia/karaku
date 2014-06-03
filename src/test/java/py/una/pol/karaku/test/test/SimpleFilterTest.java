/*
 * @SimpleFilterTest.java 1.0 21/02/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import py.una.pol.karaku.util.SimpleFilter;
import py.una.pol.karaku.util.SimpleFilter.ChangeListenerSimpleFilter;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 2.0
 * @version 1.0 21/02/2014
 * 
 */
public class SimpleFilterTest {

	private final class TestChangeListener implements
			ChangeListenerSimpleFilter {

		String valueExpected;
		String optionExpected;

		@Override
		public void onChange(SimpleFilter thizz, String value, String option) {

			assertEquals(valueExpected, value);
			assertEquals(optionExpected, option);

		}
	}

	@Test
	public void testSetChangeListener() {

		SimpleFilter sf = new SimpleFilter();

		sf.setValue("AS");
		sf.setOption("DESCRIPCION");

		assertEquals(sf.getValue(), "AS");
		assertEquals(sf.getOption(), "DESCRIPCION");

		sf.clear();

		assertNull(sf.getValue());
		assertNull(sf.getOption());

		sf.changeValueListener();

		TestChangeListener tcl = new TestChangeListener();
		tcl.valueExpected = "PRIMERA";
		tcl.optionExpected = "DESCRIPCION";

		sf.setChangeListener(tcl);
		sf.setValue("PRIMERA");
		sf.setOption("DESCRIPCION");

		sf.changeValueListener();
	}

}
