/*
 * @SIGHReportBlockFieldTest.java 1.0 Feb 11, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sf.jasperreports.engine.JRException;
import org.junit.Test;
import py.una.pol.karaku.reports.SIGHReportBlockField;
import py.una.pol.karaku.util.ListHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 11, 2014
 * 
 */
public class SIGHReportBlockFieldTest {

	@Test
	public void testSIGHReportBlockField() throws JRException {

		SIGHReportBlockField.Field f1 = new SIGHReportBlockField.Field("l1",
				"f1");
		f1.setLabel("l3");
		f1.setValue("v3");
		SIGHReportBlockField srbf = new SIGHReportBlockField("t1", "nds",
				ListHelper.getAsList(
						new SIGHReportBlockField.Field("l1", "f1"),
						new SIGHReportBlockField.Field("l2", "f2"), f1), 100,
				100);

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
