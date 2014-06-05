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
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.util.FormatProvider;

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

		veryLarge = this.getByString("1000000000000");
		large = this.getByString("100000");
		small = this.getByString("100");
		verySmall = this.getByString("1");
		decimal = this.getByString("0.111");
		decimalUP = this.getByString("0.119");
		smallDecimal = this.getByString("0.0111");
		smallDecimalUP = this.getByString("0.0159");
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
		assertTrue(this.hasSameHourAndMinute(date, fp.parseTime(TIME)));

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
		assertTrue(this.hasSameHourMinuteAndDate(date, fp.parseDateTime(DATE_TIME)));

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
		assertTrue(this.hasSameHourMinuteAndDate(date,
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

	@Test
	public void testNumberParse() throws Exception {

		assertEquals(Quantity.ONE, fp.parseLongQuantity("1"));
		assertEquals(Quantity.ZERO, fp.parseLongQuantity("0"));
		assertEquals(Quantity.ONE.negate(), fp.parseLongQuantity("-1"));
		assertEquals(Quantity.ONE, fp.parseLongQuantity("1,0000000123"));

	}

	@Test
	public void testParseQuantity() throws Exception {

		assertEquals(Quantity.ONE, fp.parseQuantity("1"));
		assertEquals(new Quantity("10"), fp.parseQuantity("1.0"));
		assertEquals(new Quantity("1000"), fp.parseQuantity("1.000"));
		assertEquals(new Quantity("25360"), fp.parseQuantity("25.360"));

	}


	@Test(expected = IllegalArgumentException.class)
	public void testNotNull() {

		assertEquals(null, fp.asNumber((Quantity) null));
	}

	private boolean hasSameHourMinuteAndDate(Date one, Date two) {

		return DateUtils.isSameDay(one, two) && this.hasSameHourAndMinute(one, two);
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
