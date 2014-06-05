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
