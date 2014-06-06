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
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.reports.GridBlockBuilder;
import py.una.pol.karaku.reports.KarakuReportBlockGrid;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.test.util.TestI18nHelper;

/**
 * Test del builder que construye bloques de reportes del tipo grilla.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 28/05/2014
 * 
 */

@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class GridBlockBuilderTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Autowired
	private TestI18nHelper i18nHelper;

	@Before
	public void testBefore() {

		i18nHelper.addString("TITLE", "Bloque con internacionalizacion");
		i18nHelper.addString("COLUMNA1", "Contacto");
		i18nHelper.addString("COLUMNA2", "Telefono");
		i18nHelper.addString("PRUEBA", "Lista de contactos");
	}

	@Test
	public void testCreate() throws Exception {

		GridBlockBuilder builder1 = new GridBlockBuilder(
				"Bloque sin internacionalizacion", false);

		assertEquals("Bloque sin internacionalizacion", builder1.getTitle());
		assertTrue(builder1.getColumns().isEmpty());

		GridBlockBuilder builder2 = new GridBlockBuilder("TITLE", true);

		assertEquals("Bloque con internacionalizacion", builder2.getTitle());
		assertTrue(builder2.getColumns().isEmpty());
	}

	@Test
	public void testAddColumn() throws Exception {

		/*
		 * Caso1. Con internacionalizacion del titulo de cada columna
		 */
		GridBlockBuilder builder1 = new GridBlockBuilder("TITLE");

		assertTrue(builder1.getColumns().isEmpty());

		builder1.addColumn("COLUMNA1", true, "nombre");
		builder1.addColumn("COLUMNA2", true, "apellido");

		assertFalse(builder1.getColumns().isEmpty());
		assertEquals(2, builder1.getColumns().size());
		assertEquals("Contacto", builder1.getColumns().get(0).getTitle());
		assertEquals("Telefono", builder1.getColumns().get(1).getTitle());

		/*
		 * Caso2. Sin internacionalizacion de los titulos
		 */
		GridBlockBuilder builder2 = new GridBlockBuilder("TITLE");

		assertTrue(builder2.getColumns().isEmpty());

		builder2.addColumn("Contacto", false, "contacto");
		builder2.addColumn("Telefono", false, "telefono");

		assertFalse(builder1.getColumns().isEmpty());
		assertEquals(2, builder2.getColumns().size());
		assertEquals("Contacto", builder2.getColumns().get(0).getTitle());
		assertEquals("Telefono", builder2.getColumns().get(1).getTitle());
	}

	@Test
	public void testBuild() throws Exception {

		GridBlockBuilder builder = new GridBlockBuilder("PRUEBA");
		builder.addColumn("Contacto", false, "contacto");
		builder.addColumn("Telefono", false, "telefono");

		KarakuReportBlockGrid build = builder.build(getData());

		assertEquals("Lista de contactos", build.getTitle());
		assertEquals(2, build.getColumns().size());

	}

	private List<String[]> getData() {

		List<String[]> data = new ArrayList<String[]>();

		String[] row = new String[2];
		row[0] = "LABORATORIO LASCA";
		row[1] = "(021)620-252";

		row = new String[2];
		row[0] = "BOTICA MAGISTRAL";
		row[1] = "(021)900-156";
		data.add(row);

		return data;

	}
}
