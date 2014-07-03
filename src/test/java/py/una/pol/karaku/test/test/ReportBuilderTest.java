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
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.reports.Column;
import py.una.pol.karaku.reports.KarakuReportBlock;
import py.una.pol.karaku.reports.KarakuReportBlockField;
import py.una.pol.karaku.reports.KarakuReportBlockField.Field;
import py.una.pol.karaku.reports.KarakuReportBlockGrid;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.test.util.TestI18nHelper;
import py.una.pol.karaku.util.ListHelper;
import py.una.pol.karaku.util.ReportBuilder;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 28/03/2014
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReportBuilderTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Autowired
	private TestI18nHelper i18nHelper;

	@Before
	public void testBefore() {

		i18nHelper.addString("TITLE", "Ficha Médica");
	}

	@Test
	public void testMasterDetail() {

		ReportBuilder builder = new ReportBuilder("TITLE", "pdf");
		builder.setMaster(getBlockField());
		KarakuReportBlockGrid detalle1 = getBlockGrid();
		detalle1.setTitle("PRIMERO");
		KarakuReportBlockGrid detalle2 = getBlockGrid();
		detalle2.setTitle("SEGUNDO");
		builder.addDetail(detalle1);
		builder.addDetail(detalle2);

		List<KarakuReportBlock> blocks = builder.getBlocksMasterDetail();

		assertEquals(3, blocks.size());
		assertEquals("Ficha Médica", builder.getParams().get("titleReport"));
		assertNotNull(builder.getMaster());
		assertEquals(2, builder.getDetails().size());
		assertEquals("Datos del paciente", blocks.get(0).getTitle());
		assertEquals("PRIMERO", blocks.get(1).getTitle());
		assertEquals("SEGUNDO", blocks.get(2).getTitle());
	}

	@Test
	public void testBlocks() {

		ReportBuilder builder = new ReportBuilder("TITLE", "pdf");
		builder.addBlock(getBlockField());
		builder.addBlock(getBlockGrid());

		List<KarakuReportBlock> blocks = builder.getBlocks();

		assertEquals(2, blocks.size());

	}

	@Test
	public void testBlocksAll() {

		ReportBuilder builder = new ReportBuilder("TITLE", "pdf");
		builder.addBlocks(ListHelper.getAsList(getBlockField(), getBlockGrid()));

		List<KarakuReportBlock> blocks = builder.getBlocks();

		assertEquals(2, blocks.size());

	}

	private List<String[]> getData() {

		List<String[]> data = new ArrayList<String[]>();

		String[] row = new String[2];
		row[0] = "Tigo";
		row[1] = "0984-999-666";
		data.add(row);

		row = new String[2];
		row[0] = "Personal";
		row[1] = "0984-585-222";
		data.add(row);

		return data;

	}

	protected KarakuReportBlockField getBlockField() {

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("Nombre", "Daniel"));
		fields.add(new Field("Apellido", "Quintana"));
		fields.add(new Field("Sexo", "Masculino"));

		KarakuReportBlockField block = new KarakuReportBlockField(
				"Datos del paciente", "datos_paciente", fields, 10, 50);
		return block;
	}

	protected KarakuReportBlockGrid getBlockGrid() {

		List<Column> columns = new ArrayList<Column>();
		columns.add(new Column("Operadora", "operadora"));
		columns.add(new Column("Número", "numero"));

		KarakuReportBlockGrid block = new KarakuReportBlockGrid("Teléfonos",
				"tel_paciente", columns, getData());
		return block;
	}

}
