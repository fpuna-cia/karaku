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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static py.una.pol.karaku.util.DateUtils.clearDate;
import static py.una.pol.karaku.util.DateUtils.cloneCalendar;
import static py.una.pol.karaku.util.DateUtils.cloneDate;
import static py.una.pol.karaku.util.DateUtils.isAfterOrEqual;
import static py.una.pol.karaku.util.DateUtils.isBefore;
import static py.una.pol.karaku.util.DateUtils.isBeforeOrEqual;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.util.DateUtils;

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
     * {@link py.una.pol.karaku.util.DateUtils#cloneDate(java.util.Date)}.
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
     * {@link py.una.pol.karaku.util.DateUtils#cloneCalendar(java.util.Calendar)}
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
    public void testBeforeOrEqual1() throws Exception {

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
    public void testBeforeOrEqual2() throws Exception {

        Calendar before = Calendar.getInstance();
        before.add(Calendar.MINUTE, -10);
        Date dBefore = before.getTime();

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Date || After: Date
         */
        assertTrue(isBeforeOrEqual(dBefore, before.getTime()));

        assertTrue(isBeforeOrEqual(dBefore, new Date()));

        assertFalse(isBeforeOrEqual(new Date(), dBefore));

    }

    @Test
    public void testBeforeOrEqual3() throws Exception {

        Calendar before = Calendar.getInstance();
        before.add(Calendar.MINUTE, -10);

        Date dBefore = before.getTime();
        Timestamp dBeforeTimeStamp = new Timestamp(before.getTime().getTime());

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Date || After: Timestamp
         */
        assertTrue(isBeforeOrEqual(dBefore, dBeforeTimeStamp));

        // Retrocedemos 10 Min el Reloj
        before.add(Calendar.MINUTE, -10);
        assertFalse(isBeforeOrEqual(dBefore, new Timestamp(before.getTime()
                .getTime())));

        // Adelantamos 100 Min el Reloj
        before.add(Calendar.MINUTE, 100);
        assertTrue(isBeforeOrEqual(new Date(), new Timestamp(before.getTime()
                .getTime())));

    }

    @Test
    public void testBeforeOrEqual4() throws Exception {

        Calendar before = Calendar.getInstance();
        before.add(Calendar.MINUTE, -10);

        Date dBefore = before.getTime();
        Timestamp dBeforeTimeStamp = new Timestamp(before.getTime().getTime());

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Timestamp || After: Date
         */
        assertTrue(isBeforeOrEqual(dBeforeTimeStamp, dBefore));

        // Retrocedemos 10 Min el Reloj
        before.add(Calendar.MINUTE, -10);
        assertFalse(isBeforeOrEqual(dBeforeTimeStamp, new Date(before.getTime()
                .getTime())));

        // Adelantamos 100 Min el Reloj
        before.add(Calendar.MINUTE, 100);
        assertTrue(isBeforeOrEqual(dBeforeTimeStamp, new Date(before.getTime()
                .getTime())));

    }

    @Test
    public void testBeforeOrEqual5() throws Exception {

        Calendar before = Calendar.getInstance();
        before.add(Calendar.MINUTE, -10);

        Date dBefore = before.getTime();
        Timestamp dBeforeTimeStamp = new Timestamp(before.getTime().getTime());

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Timestamp || After: Timestamp
         */
        assertTrue(isBeforeOrEqual(dBeforeTimeStamp, new Timestamp(before
                .getTime().getTime())));

        // Retrocedemos 10 Min el Reloj
        before.add(Calendar.MINUTE, -10);
        assertFalse(isBeforeOrEqual(dBeforeTimeStamp, new Timestamp(before
                .getTime().getTime())));

        // Adelantamos 100 Min el Reloj
        before.add(Calendar.MINUTE, 100);
        assertTrue(isBeforeOrEqual(dBeforeTimeStamp, new Timestamp(before
                .getTime().getTime())));

    }

    @Test
    public void testAfterOrEqual1() throws Exception {

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
    public void testAfterOrEqual2() throws Exception {

        Calendar after = Calendar.getInstance();

        Date dAfter = after.getTime();

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Date || After: Date
         */
        assertTrue(isAfterOrEqual(dAfter, after.getTime()));

        // Adelantamos el Reloj en 10 min
        after.add(Calendar.MINUTE, 10);
        assertTrue(isAfterOrEqual(new Date(), new Date(after.getTime()
                .getTime())));

        // Retrocedemos el Reloj en 10 min
        after.add(Calendar.MINUTE, -100);
        assertFalse(isAfterOrEqual(dAfter, new Date(after.getTime().getTime())));

    }

    @Test
    public void testAfterOrEqual3() throws Exception {

        Calendar after = Calendar.getInstance();

        Date dAfter = after.getTime();
        Timestamp dAfterTimeStamp = new Timestamp(after.getTime().getTime());

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Date || After: Timestamp
         */
        assertTrue(isAfterOrEqual(dAfter, dAfterTimeStamp));

        // Adelantamos el Reloj en 10 Minutos
        after.add(Calendar.MINUTE, 10);
        assertTrue(isAfterOrEqual(dAfter, new Timestamp(after.getTime()
                .getTime())));

        // Retrocedemos el Reloj en 100 Minutos
        after.add(Calendar.MINUTE, -100);
        assertFalse(isAfterOrEqual(dAfter, new Timestamp(after.getTime()
                .getTime())));

    }

    @Test
    public void testAfterOrEqual4() throws Exception {

        Calendar after = Calendar.getInstance();

        Date dAfter = after.getTime();
        Timestamp dAfterTimeStamp = new Timestamp(after.getTime().getTime());

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Timestamp || After: Date
         */
        assertTrue(isAfterOrEqual(dAfterTimeStamp, dAfter));

        // Adelantamos el Reloj en 10 Minutos
        after.add(Calendar.MINUTE, 10);
        assertTrue(isAfterOrEqual(dAfterTimeStamp, new Date(after.getTime()
                .getTime())));

        // Retrocedemos el Reloj en 100 Minutos
        after.add(Calendar.MINUTE, -100);
        assertFalse(isAfterOrEqual(dAfterTimeStamp, new Date(after.getTime()
                .getTime())));

    }

    @Test
    public void testAfterOrEqual5() throws Exception {

        Calendar after = Calendar.getInstance();
        after.add(Calendar.MINUTE, 10);

        Timestamp dAfterTimeStamp = new Timestamp(after.getTime().getTime());

        /*
         * Realizamos las pruebas correspondientes para el siguiente caso de:
         * Before: Timestamp || After: TimeStamp
         */
        assertTrue(isAfterOrEqual(dAfterTimeStamp, dAfterTimeStamp));

        // Adelantamos el Reloj en 10 Minutos
        after.add(Calendar.MINUTE, 10);
        assertTrue(isAfterOrEqual(dAfterTimeStamp, new Timestamp(after
                .getTime().getTime())));

        // Retrocedemos el Reloj en 100 Minutos
        after.add(Calendar.MINUTE, -100);
        assertFalse(isAfterOrEqual(dAfterTimeStamp, new Timestamp(after
                .getTime().getTime())));

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

    @Test
    public void testClearDate() throws Exception {

        Calendar dateClear = Calendar.getInstance();
        dateClear.set(Calendar.HOUR_OF_DAY, 0);
        dateClear.set(Calendar.MINUTE, 0);
        dateClear.set(Calendar.SECOND, 0);
        dateClear.set(Calendar.MILLISECOND, 0);

        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 12);
        date.set(Calendar.MINUTE, 12);
        date.set(Calendar.SECOND, 12);
        date.set(Calendar.MILLISECOND, 12);

        assertEquals(clearDate(date.getTime()), dateClear.getTime());
        assertEquals(clearDate(Calendar.getInstance().getTime()),
                dateClear.getTime());

    }

    @Test
    public void testGetEdad() {

        assertEquals(DateUtils.getEdad(new Date()), "1 día");
        assertEquals(DateUtils.getEdad(null), null);
        // prueba con años
        Calendar dateClear = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        dateClear.set(Calendar.DAY_OF_YEAR, 1);
        dateClear.set(Calendar.MONTH, 1);
        dateClear.set(Calendar.YEAR, 1);
        assertEquals(DateUtils.getEdad(dateClear.getTime()),
                DateUtils.calculateYearsFromNow(dateClear.getTime()) + " años");
        // prueba con año
        dateClear = Calendar.getInstance();
        dateClear.set(Calendar.DAY_OF_YEAR, c1.get(Calendar.DAY_OF_YEAR));
        dateClear.set(Calendar.MONTH, c1.get(Calendar.MONTH));
        dateClear.set(Calendar.YEAR, c1.get(Calendar.YEAR) - 1);
        assertEquals(DateUtils.getEdad(dateClear.getTime()), "1 año");

        // prueba con mes
        dateClear.set(Calendar.DAY_OF_MONTH, c1.get(Calendar.DAY_OF_MONTH));
        if (c1.get(Calendar.MONTH) == 0) {
            dateClear.set(Calendar.MONTH, 11);
        } else {
            dateClear.set(Calendar.MONTH, c1.get(Calendar.MONTH) - 1);
        }

        dateClear.set(Calendar.YEAR, c1.get(Calendar.YEAR));
        assertEquals(DateUtils.getEdad(dateClear.getTime()), "1 mes");

    }
}
