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

package py.una.pol.karaku.test.test.breadcrumb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import py.una.pol.karaku.breadcrumb.BreadcrumbItem;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 25, 2015
 * 
 */
public class BreadcrumbItemTest {

    /**
     * Test method for
     * {@link py.una.pol.karaku.breadcrumb.BreadcrumbItem#hashCode()}.
     */
    @Test
    public void testHashCode() {

        BreadcrumbItem x, y;
        x = new BreadcrumbItem("0", "");
        y = new BreadcrumbItem("0", "");

        int hashX = x.hashCode();
        int hashY = y.hashCode();

        assertEquals(hashX, hashY);

        y.setUri(null);
        hashY = y.hashCode();

        assertTrue(hashX != hashY);

    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.breadcrumb.BreadcrumbItem#equals(java.lang.Object)}
     * .
     */
    @Test
    public void testEqualsObject() {

        BreadcrumbItem x, y;
        Object z = new Object();
        x = new BreadcrumbItem("", "");
        y = new BreadcrumbItem("", "");

        assertTrue(x.equals(x));

        assertTrue(x.equals(y));

        assertFalse(x.equals(z));

        x.setUri(null);
        assertFalse(x.equals(y));

        y.setUri(null);
        assertTrue(x.equals(y));

        x.setUri("+x");
        y.setUri("-y");
        assertFalse(x.equals(y));

    }

}
