/*
 * @ReportHelperTest.java 1.0 31/03/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.reports.Column;
import py.una.med.base.reports.SIGHReportBlockField;
import py.una.med.base.reports.SIGHReportBlockField.Field;
import py.una.med.base.reports.SIGHReportBlockGrid;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.ControllerTestConfiguration;
import py.una.med.base.util.ReportBuilder;
import py.una.med.base.util.ReportHelper;

/**
 * Test del helper utilizado para la generacion de reportes.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 31/03/2014
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReportHelperTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends ControllerTestConfiguration {

		@Bean
		public ReportHelper reportHelper() {

			return new ReportHelper();
		}
	}

	@Autowired
	private ReportHelper reportHelper;

	@Test
	public void testGenerateMasterDetail() {

		ReportBuilder builder = new ReportBuilder(
				"Reporte del tipo cabecera-detalle", "pdf");
		builder.setMaster(getBlockHeader());
		builder.addDetail(getBlockDetail());

		reportHelper.generateMasterDetail(builder);
	}

	protected SIGHReportBlockGrid getBlockDetail() {

		List<Column> columns = new ArrayList<Column>();
		columns.add(new Column("Operadora", "operadora"));
		columns.add(new Column("Número", "numero"));

		SIGHReportBlockGrid block2 = new SIGHReportBlockGrid("Teléfonos",
				"tel_paciente", columns, getData());
		return block2;
	}

	protected SIGHReportBlockField getBlockHeader() {

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("Nombre", "Daniel"));
		fields.add(new Field("Apellido", "Quintana"));
		fields.add(new Field("Sexo", "Masculino"));

		SIGHReportBlockField block1 = new SIGHReportBlockField(
				"Datos del paciente", "datos_paciente", fields, 10, 50);
		return block1;
	}

	private List<String[]> getData() {

		List<String[]> data = new ArrayList<String[]>();

		String[] row = new String[2];
		row[0] = "Valor de: columna 1, fila 1";
		row[1] = "Valor de: columna 2, fila 1";

		row = new String[2];
		row[0] = "Valor de: columna 1, fila 2";
		row[1] = "Valor de: columna 2, fila 2";
		data.add(row);

		return data;

	}
}
