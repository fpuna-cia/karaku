/*
 * @FormatProviderTest.java 1.0 Sep 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.util.FormatProvider;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 11, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class FormatProviderTest extends BaseTest {

	private static final String EMPTY_STRING = "";

	private static final String SHORT_DATE = "01-01-13";

	private static final String LONG_DATE = "01-01-2013";

	private static final String TIME = "06:30";

	private static final String DATE_TIME = "01-01-2013 06:30";

	private static final String DATE_SHORT_TIME = "01-01-13 06:30";

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Autowired
	private FormatProvider fp;

	private Calendar calendar;

	private Date date;

	private BigDecimal veryLarge;
	private BigDecimal large;
	private BigDecimal small;
	private BigDecimal verySmall;
	private BigDecimal decimal;
	private BigDecimal decimalUP;
	private BigDecimal smallDecimal;
	private BigDecimal smallDecimalUP;

	/**
	 *
	 */
	@Before
	public void setUp() {

		calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.YEAR, 2013);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);

		calendar.set(Calendar.HOUR_OF_DAY, 6);
		calendar.set(Calendar.MINUTE, 30);
		calendar.set(Calendar.SECOND, 10);

		date = calendar.getTime();

		veryLarge = getByString("1000000000000");
		large = getByString("100000");
		small = getByString("100");
		verySmall = getByString("1");
		decimal = getByString("0.111");
		decimalUP = getByString("0.119");
		smallDecimal = getByString("0.0111");
		smallDecimalUP = getByString("0.0159");
	}

	/**
	 * 
	 * TODO mejorar test.
	 */
	@Test
	public void testGetReference() {

		assertNotNull(fp);

		assertSame(fp, applicationContext.getBean(FormatProvider.class));

		assertSame(fp, applicationContext.getBean("fp"));

	}

	@Test
	public void testShortDate() throws ParseException {

		assertEquals(SHORT_DATE, fp.asShortDate(date));
		assertTrue(DateUtils.isSameDay(date, fp.parseShortDate(SHORT_DATE)));

		assertEquals(EMPTY_STRING, fp.asShortDate((Date) null));
		assertEquals(null, fp.parseShortDate(EMPTY_STRING));
		assertEquals(null, fp.parseShortDate((String) null));
		assertEquals(SHORT_DATE, fp.asShortDate(fp.parseShortDate(SHORT_DATE)));

	}

	@Test(expected = ParseException.class)
	public void testWrongFormatShortDate() throws ParseException {

		fp.parseShortDate(LONG_DATE);
	}

	@Test(expected = ParseException.class)
	public void testLenientFormatShortDate() throws ParseException {

		fp.parseShortDate("31-31-31");
	}

	@Test
	public void testLongDate() throws ParseException {

		assertEquals(LONG_DATE, fp.asDate(date));
		assertTrue(DateUtils.isSameDay(date, fp.parseDate(LONG_DATE)));

		assertEquals(EMPTY_STRING, fp.asDate(null));
		assertEquals(null, fp.parseDate(EMPTY_STRING));
		assertEquals(null, fp.parseDate(null));

		assertEquals(LONG_DATE, fp.asDate(fp.parseDate(LONG_DATE)));
	}

	@Test(expected = ParseException.class)
	public void testWrongFormatLongDate() throws ParseException {

		fp.parseDate(SHORT_DATE);
	}

	@Test(expected = ParseException.class)
	public void testLenientFormatLongDate() throws ParseException {

		fp.parseShortDate("31-31-2031");
	}

	@Test
	public void testTime() throws ParseException {

		assertEquals(TIME, fp.asTime(date));
		assertTrue(hasSameHourAndMinute(date, fp.parseTime(TIME)));

		assertEquals(EMPTY_STRING, fp.asTime(null));
		assertEquals(null, fp.parseTime(EMPTY_STRING));
		assertEquals(null, fp.parseTime(null));

		assertEquals(TIME, fp.asTime(fp.parseTime(TIME)));
	}

	@Test(expected = ParseException.class)
	public void testWrongFormatTime() throws ParseException {

		fp.parseTime(SHORT_DATE);
	}

	@Test(expected = ParseException.class)
	public void testLenientFormatTime() throws ParseException {

		fp.parseTime("99:99");
	}

	@Test
	public void testLongDateTime() throws ParseException {

		assertEquals(DATE_TIME, fp.asDateTime(date));
		assertTrue(hasSameHourMinuteAndDate(date, fp.parseDateTime(DATE_TIME)));

		assertEquals(EMPTY_STRING, fp.asDateTime(null));
		assertEquals(null, fp.parseDateTime(EMPTY_STRING));
		assertEquals(null, fp.parseDateTime(null));

		assertEquals(DATE_TIME, fp.asDateTime(fp.parseDateTime(DATE_TIME)));
	}

	@Test(expected = ParseException.class)
	public void testWrongFormatDateTime() throws ParseException {

		fp.parseDateTime(SHORT_DATE);
	}

	@Test(expected = ParseException.class)
	public void testLenientFormatDateTime() throws ParseException {

		fp.parseDateTime("31-12-2031 99:99");
	}

	@Test
	public void testShortDateTime() throws ParseException {

		assertEquals(DATE_SHORT_TIME, fp.asShortDateTime(date));
		assertTrue(hasSameHourMinuteAndDate(date,
				fp.parseShortDateTime(DATE_SHORT_TIME)));

		assertEquals(EMPTY_STRING, fp.asShortDateTime(null));
		assertEquals(null, fp.parseShortDateTime(EMPTY_STRING));
		assertEquals(null, fp.parseShortDateTime(null));

		assertEquals(DATE_SHORT_TIME,
				fp.asShortDateTime(fp.parseShortDateTime(DATE_SHORT_TIME)));
	}

	@Test(expected = ParseException.class)
	public void testWrongFormatShortDateTime() throws ParseException {

		fp.parseShortDateTime(SHORT_DATE);
	}

	@Test(expected = ParseException.class)
	public void testLenientFormatShortDateTime() throws ParseException {

		fp.parseShortDateTime("31-12-31 24:40");
	}

	@Test
	public void testNumbrerFormat() {

		assertEquals("1.000.000.000.000", fp.asNumber(veryLarge));
		assertEquals("100.000", fp.asNumber(large));
		assertEquals("100", fp.asNumber(small));
		assertEquals("1", fp.asNumber(verySmall));
		assertEquals("0", fp.asNumber(BigDecimal.ZERO));
		assertEquals("0,11", fp.asNumber(decimal));
		assertEquals("0,12", fp.asNumber(decimalUP));
		assertEquals("0,01", fp.asNumber(smallDecimal));
		assertEquals("0,02", fp.asNumber(smallDecimalUP));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotNull() {

		assertEquals(null, fp.asNumber(null));
	}

	private boolean hasSameHourMinuteAndDate(Date one, Date two) {

		return DateUtils.isSameDay(one, two) && hasSameHourAndMinute(one, two);
	}

	/**
	 * @param date2
	 * @param date
	 * @return
	 */
	private boolean hasSameHourAndMinute(Date date2, Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		return c.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
				&& c.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE);
	}

	private BigDecimal getByString(String string) {

		return new BigDecimal(string);
	}
}
