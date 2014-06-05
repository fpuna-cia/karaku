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
