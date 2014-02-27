/*
 * @PropertiesUtilTest.java 1.0 Feb 27, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.exception.KarakuWrongConfigurationFileException;
import py.una.med.base.test.util.TestUtils;

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
