/*
 * @DateUtilsTest.java 1.0 Nov 15, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.util.DateUtils;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 15, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class DateUtilsTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	/**
	 * Test method for
	 * {@link py.una.med.base.util.DateUtils#cloneDate(java.util.Date)}.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testGetCopy() {

		Date date = new Date();
		assertNotSame(date, DateUtils.cloneDate(date));
		assertEquals(date, DateUtils.cloneDate(date));
		Date copy = DateUtils.cloneDate(date);
		copy.setMinutes(copy.getMinutes() + 1);
		assertNotEquals(date, copy);
	}

	/**
	 * Test method for
	 * {@link py.una.med.base.util.DateUtils#cloneCalendar(java.util.Calendar)}
	 * .
	 */
	@Test
	public void testGetCopyCalendar() {

		Calendar date = Calendar.getInstance();
		assertNotSame(date, DateUtils.cloneCalendar(date));
		assertEquals(date, DateUtils.cloneCalendar(date));
		Calendar copy = DateUtils.cloneCalendar(date);
		date.add(Calendar.MINUTE, 1);
		assertNotEquals(date, copy);
	}

}
