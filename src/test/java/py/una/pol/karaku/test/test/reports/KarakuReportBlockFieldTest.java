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
package py.una.pol.karaku.test.test.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.jasperreports.engine.JRException;
import org.junit.Test;
import py.una.pol.karaku.reports.KarakuReportBlockField;
import py.una.pol.karaku.util.ListHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 11, 2014
 * 
 */
public class KarakuReportBlockFieldTest {

	@Test
	public void testReportBlockField() throws JRException {

		KarakuReportBlockField.Field f1 = new KarakuReportBlockField.Field(
				"l1", "f1");
		f1.setLabel("l3");
		f1.setValue("v3");
		KarakuReportBlockField srbf = new KarakuReportBlockField("t1", "nds",
				ListHelper
						.getAsList(
								new KarakuReportBlockField.Field("l1", "f1"),
								new KarakuReportBlockField.Field("l2", "f2"),
								f1), 100, 100);

		assertEquals(100, srbf.getWidthLabel());
		assertEquals(100, srbf.getWidthValue());
		assertNotNull(srbf.getDataSource());

		srbf.setWidthLabel(99);
		srbf.setWidthValue(99);

		assertEquals(99, srbf.getWidthLabel());
		assertEquals(99, srbf.getWidthValue());

		assertTrue(srbf.getDataSource().next());
		assertTrue(srbf.getDataSource().next());
		assertTrue(srbf.getDataSource().next());
		assertFalse(srbf.getDataSource().next());
	}

}
