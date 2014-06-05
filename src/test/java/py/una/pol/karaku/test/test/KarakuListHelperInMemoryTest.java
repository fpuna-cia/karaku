/*
 * 
 */
package py.una.pol.karaku.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import py.una.pol.karaku.util.KarakuListHelperInMemory;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 5, 2014
 * 
 */
public class KarakuListHelperInMemoryTest {

	private KarakuListHelperInMemory<String> listHelper;
	private List<String> list;

	@Before
	public void before() {

		list = new ArrayList<String>();

		for (int i = 0; i < 99; i++) {
			list.add(UUID.randomUUID().toString());
		}

		listHelper = new KarakuListHelperInMemory<String>(list);
	}

	@Test
	public void testGetEntities() {

		assertNotNull("El pagingHelper es nulo", listHelper.getHelper());
		assertNotNull("La lista es nula", listHelper.getEntities());

		assertEquals("Tamanho de lista incorrecto para el paginado", 5,
				listHelper.getEntities().size());
		listHelper.getHelper().last();
		assertEquals("Tamanho de lista incorrecto para el paginado", 4,
				listHelper.getEntities().size());

		listHelper.setListMemory(null);

		assertTrue(listHelper.getEntities().isEmpty());

	}

}
