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
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.exception.ReportException;
import py.una.pol.karaku.reports.Column;
import py.una.pol.karaku.reports.KarakuReportBlockField;
import py.una.pol.karaku.reports.KarakuReportBlockField.Field;
import py.una.pol.karaku.reports.KarakuReportBlockGrid;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.ControllerTestConfiguration;
import py.una.pol.karaku.util.ReportBuilder;
import py.una.pol.karaku.util.ReportHelper;

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
    public void testGetJasperPrinter() throws ReportException {

        ReportBuilder builder = new ReportBuilder(
                "Reporte del tipo cabecera-detalle", "pdf");
        builder.setMaster(getBlockHeader());
        builder.addDetail(getBlockDetail());
        JasperPrint r = reportHelper.getJasperPrinter(builder, null);

        assertEquals(true, r != null);
    }

    @Test
    public void testGenerateMasterDetail() {

        ReportBuilder builder = new ReportBuilder(
                "Reporte del tipo cabecera-detalle", "pdf");
        builder.setMaster(getBlockHeader());
        builder.addDetail(getBlockDetail());

        reportHelper.generateMasterDetail(builder);
    }

    @Test
    public void testgenerateReportBlocks() throws Exception {

        ReportBuilder builder = new ReportBuilder(
                "Reporte que contiene una lista de bloques", "pdf");
        builder.addBlock(getBlockDetail());
        builder.addBlock(getBlockDetail());
        builder.addBlock(getBlockDetail());
        builder.addBlock(getBlockDetail());

        reportHelper.generateReportBlocks(builder);
    }

    @Test
    public void testgenerateReportBlocksBlank() throws Exception {

        ReportBuilder builder = new ReportBuilder("Reporte sin bloques", "pdf");

        reportHelper.generateReportBlocks(builder);
    }

    protected KarakuReportBlockGrid getBlockDetail() {

        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("Operadora", "operadora"));
        columns.add(new Column("Numero", "numero"));

        KarakuReportBlockGrid block2 = new KarakuReportBlockGrid("Teléfonos",
                "tel_paciente", columns, getData());
        return block2;
    }

    protected KarakuReportBlockField getBlockHeader() {

        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field("Nombre", "Daniel"));
        fields.add(new Field("Apellido", "Quintana"));
        fields.add(new Field("Sexo", "Masculino"));

        KarakuReportBlockField block1 = new KarakuReportBlockField(
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
