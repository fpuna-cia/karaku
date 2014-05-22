/*
 * @ReportBuilderTest.java 1.0 28/03/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test;

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
import py.una.med.base.reports.Column;
import py.una.med.base.reports.SIGHReportBlock;
import py.una.med.base.reports.SIGHReportBlockField;
import py.una.med.base.reports.SIGHReportBlockField.Field;
import py.una.med.base.reports.SIGHReportBlockGrid;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.test.util.TestI18nHelper;
import py.una.med.base.util.ReportBuilder;

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
	public void testSetMaster() {

		ReportBuilder builder = new ReportBuilder("TITLE", "pdf");
		builder.setMaster(getBlockField());

		assertEquals("Ficha Médica", builder.getParams().get("titleReport"));
		assertNotNull(builder.getMaster());
		assertEquals("Datos del paciente", builder.getMaster().getTitle());

	}

	@Test
	public void testAddDetail() {

		ReportBuilder builder = new ReportBuilder("TITLE", "pdf");

		builder.addDetail(getBlockGrid());
		builder.addDetail(getBlockGrid());

		assertEquals(2, builder.getDetails().size());
		assertEquals("Teléfonos", builder.getDetails().get(0).getTitle());
	}

	@Test
	public void testGetBlocks() {

		ReportBuilder builder = new ReportBuilder("TITLE", "pdf");
		builder.setMaster(getBlockField());
		builder.addDetail(getBlockGrid());

		List<SIGHReportBlock> blocks = builder.getBlocksMasterDetail();

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

	protected SIGHReportBlockField getBlockField() {

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("Nombre", "Daniel"));
		fields.add(new Field("Apellido", "Quintana"));
		fields.add(new Field("Sexo", "Masculino"));

		SIGHReportBlockField block = new SIGHReportBlockField(
				"Datos del paciente", "datos_paciente", fields, 10, 50);
		return block;
	}

	protected SIGHReportBlockGrid getBlockGrid() {

		List<Column> columns = new ArrayList<Column>();
		columns.add(new Column("Operadora", "operadora"));
		columns.add(new Column("Número", "numero"));

		SIGHReportBlockGrid block = new SIGHReportBlockGrid("Teléfonos",
				"tel_paciente", columns, getData());
		return block;
	}

}
