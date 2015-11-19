/*-
 * Copyright (c)
 *
 * 		2012-2015, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2015, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
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

package py.una.pol.karaku.test.test.dao.entity.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.hibernate.HibernateException;
import org.junit.Test;
import py.una.pol.karaku.dao.entity.types.QuantityTypeDescriptor;
import py.una.pol.karaku.math.Quantity;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 24, 2015
 * 
 */
public class QuantityTypeDescriptorTest {

    private final QuantityTypeDescriptor descriptor;

    public QuantityTypeDescriptorTest() {

        this.descriptor = new QuantityTypeDescriptor();
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.dao.entity.types.QuantityTypeDescriptor#unwrap(py.una.pol.karaku.math.Quantity, java.lang.Class, org.hibernate.type.descriptor.WrapperOptions)}
     * .
     */
    @Test
    public void testUnwrap() throws HibernateException {

        Object b = null;
        Quantity q = new Quantity(8);

        b = descriptor.unwrap(q, Byte.class, null);
        assertNotNull(b);

        b = descriptor.unwrap(q, Short.class, null);
        assertNotNull(b);

        b = descriptor.unwrap(q, Integer.class, null);
        assertNotNull(b);

        b = descriptor.unwrap(q, Long.class, null);
        assertNotNull(b);

        b = descriptor.unwrap(q, Float.class, null);
        assertNotNull(b);

        b = descriptor.unwrap(q, Double.class, null);
        assertNotNull(b);

        b = descriptor.unwrap(q, BigDecimal.class, null);
        assertNotNull(b);

        b = descriptor.unwrap(q, BigInteger.class, null);
        assertNotNull(b);

        try {
            b = descriptor.unwrap(q, String.class, null);
        } catch (Exception e) {
            assertEquals(HibernateException.class, e.getClass());
        }

        b = descriptor.unwrap(null, String.class, null);
        assertNull(b);

    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.dao.entity.types.QuantityTypeDescriptor#wrap(java.lang.Object, org.hibernate.type.descriptor.WrapperOptions)}
     * .
     */
    @Test
    public void testWrap() {

        Quantity q = null;
        Object o = new Object();

        o = descriptor.wrap(q, null);
        assertNull(o);

        Quantity p;
        q = new Quantity(5);
        p = descriptor.wrap(q, null);
        assertEquals(5, p.intValue());

        BigDecimal bd = new BigDecimal(10);
        p = descriptor.wrap(bd, null);
        assertEquals(10, p.intValue());

        BigInteger bi = new BigInteger("12");
        p = descriptor.wrap(bi, null);
        assertEquals(12, p.intValue());

        AtomicLong al = new AtomicLong(13);
        p = descriptor.wrap(al, null);
        assertEquals(13, p.intValue());

    }

}
