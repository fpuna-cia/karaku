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
package py.una.pol.karaku.test.test.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.services.util.NumberAdapter;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 14, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class NumberAdapterTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		NumberAdapter numberAdapter() {
			return NumberAdapter.INSTANCE;
		}
	}

	@Autowired
	private NumberAdapter numberAdapter;

	@Test
	public void marshal() {

		assertEquals("1", numberAdapter.marshal(Quantity.ONE));
		assertEquals("0", numberAdapter.marshal(Quantity.ZERO));
		assertEquals("1,12", numberAdapter.marshal(new Quantity("1.1234")));
		assertEquals("-1", numberAdapter.marshal(Quantity.ONE.negate()));
	}

	@Test
	public void unMarshal() {

		assertEquals(Quantity.ONE, numberAdapter.unmarshal("1"));
		assertEquals(Quantity.TWO, numberAdapter.unmarshal("00000000000002"));
		assertEquals(Quantity.ONE, numberAdapter.unmarshal("1,000012345"));
		assertEquals(new Quantity("1000258.7"),
				numberAdapter.unmarshal("1.000.258,7"));
		assertEquals(Quantity.TWO, numberAdapter.unmarshal("1,99999"));
		assertEquals(Quantity.ONE.negate(), numberAdapter.unmarshal("-1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentException() throws Exception {

		numberAdapter.unmarshal("No Number");

	}
}
