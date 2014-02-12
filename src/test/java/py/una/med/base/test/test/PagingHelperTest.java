/*
 * @PagingHelperTest.java 1.0 Feb 12, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.util.PagingHelper;
import py.una.med.base.util.PagingHelper.ChangeListener;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 12, 2014
 * 
 */
public class PagingHelperTest {

	PagingHelper pg;
	TestChangeListener changeListener;

	@Before
	public void init() {

		pg = new PagingHelper(10);
		pg.udpateCount(99L);
		changeListener = new TestChangeListener();

	}

	@Test
	public void testNavigation() {

		pg.setChangeListener(changeListener);

		assertEquals(10, pg.getMaxReadablePage());

		pg.next();
		assertEquals(i(2), pg.getReadablePage());
		assertNotify(1, 0);

		pg.next();
		assertEquals(i(3), pg.getReadablePage());
		assertNotify(2, 1);

		pg.last();
		assertEquals(i(10), pg.getReadablePage());
		assertNotify(9, 2);

		pg.last();
		assertEquals(i(10), pg.getReadablePage());
		assertFalse(pg.hasNext());
		assertNotify(null, null);

		pg.next();
		assertEquals(i(10), pg.getReadablePage());
		assertTrue(pg.hasPrevious());
		assertNotify(null, null);

		pg.previous();
		assertEquals(i(9), pg.getReadablePage());
		assertTrue(pg.hasPrevious());
		assertNotify(8, 9);

		pg.first();
		assertFalse(pg.hasPrevious());
		assertEquals(i(1), pg.getReadablePage());
		assertNotify(0, 8);

		pg.previous();
		assertEquals(i(1), pg.getReadablePage());
		assertNotify(null, null);

		pg.first();
		assertEquals(i(1), pg.getReadablePage());
		assertFalse(pg.hasPrevious());
		assertNotify(null, null);

	}

	@Test
	public void testSetPage() {

		pg.setReadablePage(10);
		assertTrue(pg.hasPrevious());
		assertFalse(pg.hasNext());
		assertEquals(i(10), pg.getReadablePage());

		pg.setReadablePage(100);
		assertTrue(pg.hasPrevious());
		assertFalse(pg.hasNext());
		assertEquals(i(10), pg.getReadablePage());

		pg.setReadablePage(-100);
		assertFalse(pg.hasPrevious());
		assertTrue(pg.hasNext());
		assertEquals(i(1), pg.getReadablePage());
	}

	@Test
	public void testUpdateCount() {

		pg.udpateCount(5L);

		assertEquals(i(1), pg.getReadablePage());
		assertFalse(pg.hasNext());
		assertFalse(pg.hasPrevious());
		assertEquals(1, pg.getMinReadablePage());
		assertEquals(1, pg.getMaxReadablePage());

		pg.udpateCount(100L);
		pg.next();
		pg.next();
		pg.next();
		assertEquals(i(4), pg.getReadablePage());

		pg.udpateCount(20L);
		assertEquals(i(1), pg.getReadablePage());
		assertTrue(pg.hasNext());
		assertFalse(pg.hasPrevious());
		assertEquals(1, pg.getMinReadablePage());
		assertEquals(2, pg.getMaxReadablePage());
	}

	@Test
	public void testSearchParam() {

		ISearchParam isp = pg.getISearchparam();
		assertEquals(i(0), isp.getOffset());
		assertEquals(i(10), isp.getLimit());

		pg.next();
		isp = pg.getISearchparam();
		assertEquals(i(10), isp.getOffset());
		assertEquals(i(10), isp.getLimit());

		pg.next();
		isp = pg.getISearchparam();
		assertEquals(i(20), isp.getOffset());
		assertEquals(i(10), isp.getLimit());

		pg.udpateCount(15L);

		isp = pg.getISearchparam();
		assertEquals(i(0), isp.getOffset());
		assertEquals(i(10), isp.getLimit());

		pg.next();
		isp = pg.getISearchparam();
		assertEquals(i(10), isp.getOffset());
		assertEquals(i(10), isp.getLimit());
	}

	private void assertNotify(Integer current, Integer previus) {

		assertEquals(current, changeListener.current);
		assertEquals(previus, changeListener.previus);
		changeListener.clear();
	}

	private Integer i(int i) {

		return i;
	}

	private static class TestChangeListener implements ChangeListener {

		Integer current;
		Integer previus;

		public void clear() {

			current = null;
			previus = null;
		}

		@Override
		public void onChange(PagingHelper thizz, int previousPage,
				int currentPage) {

			current = currentPage;
			previus = previousPage;
		}
	}

}
