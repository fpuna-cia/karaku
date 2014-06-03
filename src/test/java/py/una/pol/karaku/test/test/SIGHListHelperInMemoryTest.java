package py.una.med.base.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import py.una.med.base.util.SIGHListHelperInMemory;

public class SIGHListHelperInMemoryTest {

	private SIGHListHelperInMemory<String> listHelper;
	private List<String> list;

	@Before
	public void before() {

		list = new ArrayList<String>();

		for (int i = 0; i < 99; i++) {
			list.add(UUID.randomUUID().toString());
		}

		listHelper = new SIGHListHelperInMemory<String>(list);
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
