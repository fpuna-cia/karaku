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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import py.una.pol.karaku.dao.entity.types.QuantityCustomType;
import py.una.pol.karaku.math.Quantity;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 24, 2015
 * 
 */
public class QuantityCustomTypeTest {

    private final QuantityCustomType quantityType;

    public QuantityCustomTypeTest() {

        this.quantityType = new QuantityCustomType();
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.dao.entity.types.QuantityCustomType#sqlTypes()}.
     */
    @Test
    public void testSqlTypes() {

        int[] intArray = quantityType.sqlTypes();

        assertNotNull(intArray);
        assertEquals(1, intArray.length);
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.dao.entity.types.QuantityCustomType#returnedClass()}
     * .
     */
    @Test
    public void testReturnedClass() {

        assertEquals(Quantity.class, quantityType.returnedClass());
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.dao.entity.types.QuantityCustomType#equals(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testEqualsObjectObject() {

        Object o1 = new Object();
        Object o2 = new Object();
        assertFalse(quantityType.equals(o1, o2));

        o1 = o2;
        assertTrue(quantityType.equals(o1, o2));

        o1 = null;
        assertFalse(quantityType.equals(o1, o2));

        o2 = null;
        assertTrue(quantityType.equals(o1, o2));

    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.dao.entity.types.QuantityCustomType#deepCopy(java.lang.Object)}
     * .
     */
    @Test
    public void testDeepCopy() {

        Integer i = new Integer(5);
        Object q = quantityType.deepCopy(i);
        assertNotNull(q);
        Object o = new Object();
        try {
            q = quantityType.deepCopy(o);
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
        o = null;
        q = quantityType.deepCopy(o);
        assertNull(q);

    }

}
