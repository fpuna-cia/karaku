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
import javax.faces.context.FacesContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.jsf.converters.QuantityConverter;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.util.FormatProvider;

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
