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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.exception.KarakuWrongConfigurationFileException;
import py.una.pol.karaku.test.util.TestUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 27, 2014
 * 
 */
public class PropertiesUtilTest {

	private PropertiesUtil properties;
	private PropertiesUtil prop;

	@Before
	public void makeProperties() {

		properties = new PropertiesUtil();
		properties.setLocation(TestUtils.getSiblingResource(getClass(),
				"test.properties"));
		properties.postProcessBeanFactory(new DefaultListableBeanFactory());
	}

	@Test
	public void testProcessProperties() {

		assertEquals("a", properties.get("string"));
		assertEquals("b", properties.get("empty", "b"));
		assertEquals(1, properties.getInteger("int", -1));

		assertTrue(properties.getBoolean("boolean", false));
		assertTrue(properties.getBoolean("noexiste", true));
		assertTrue(properties.getBoolean("boolean2", false));
		assertFalse(properties.getBoolean("string", false));
		assertFalse(properties.getBoolean("boolean3", false));

	}

	@Test(expected = KarakuRuntimeException.class)
	public void testNotFound() throws Exception {

		properties.get("unnnn");

	}

	@Test(expected = KarakuWrongConfigurationFileException.class)
	public void testNotExitingFile() throws Exception {

		Properties p = new Properties();
		p.setProperty(PropertiesUtil.ANOTHER_KEY, "/test");

		prop = new PropertiesUtil();
		prop.setProperties(p);
		prop.postProcessBeanFactory(new DefaultListableBeanFactory());
	}

	@Test(expected = KarakuRuntimeException.class)
	public void testWrongIntegerRead() throws Exception {

		properties.getInteger("string", 1);
	}
}
