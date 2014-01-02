/*
 * @DateUtilsTest.java 1.0 Nov 15, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static py.una.med.base.util.DateUtils.cloneCalendar;
import static py.una.med.base.util.DateUtils.cloneDate;
import static py.una.med.base.util.DateUtils.isAfterOrEqual;
import static py.una.med.base.util.DateUtils.isBeforeOrEqual;
import static py.una.med.base.util.DateUtils.isBefore;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

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
		assertNotSame(date, cloneDate(date));
		assertEquals(date, cloneDate(date));
		Date copy = cloneDate(date);
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
		assertNotSame(date, cloneCalendar(date));
		assertEquals(date, cloneCalendar(date));
		Calendar copy = cloneCalendar(date);
		date.add(Calendar.MINUTE, 1);
		assertNotEquals(date, copy);
	}

	@Test
	public void testBeforeOrEqual() throws Exception {

		Calendar before = Calendar.getInstance();
		before.add(Calendar.MINUTE, -1);
		Date dBefore = before.getTime();

		assertTrue(isBeforeOrEqual(dBefore, new Date()));
		assertTrue(isBeforeOrEqual(dBefore, dBefore));
		assertFalse(isBeforeOrEqual(null, new Date()));
		assertTrue(isBeforeOrEqual(new Date(), null));
		assertTrue(isBeforeOrEqual(null, null));
		assertFalse(isBeforeOrEqual(new Date(), dBefore));
	}

	@Test
	public void testAfterOrEqual() throws Exception {

		Calendar after = Calendar.getInstance();
		after.add(Calendar.MINUTE, 10);
		Date dAfter = after.getTime();

		assertTrue(isAfterOrEqual(new Date(), dAfter));
		assertTrue(isAfterOrEqual(dAfter, dAfter));
		assertFalse(isAfterOrEqual(new Date(), null));
		assertTrue(isAfterOrEqual(null, new Date()));
		assertTrue(isAfterOrEqual(null, null));
		assertFalse(isAfterOrEqual(dAfter, new Date()));
	}

	@Test
	public void testBefore() throws Exception {

		Calendar before = Calendar.getInstance();
		before.add(Calendar.MINUTE, -1);
		Date dBefore = before.getTime();

		assertTrue(isBefore(dBefore, new Date()));
		assertFalse(isBefore(dBefore, dBefore));
		assertFalse(isBefore(null, new Date()));
		assertTrue(isBefore(new Date(), null));
		assertFalse(isBefore(null, null));
		assertFalse(isBefore(new Date(), dBefore));
	}
}
