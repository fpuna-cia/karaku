/*-
 * Copyright (c)
 *
 * 		2012-2015, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2015, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
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

package py.una.pol.karaku.test.test.survey.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.survey.components.DynamicSurveyDataTable;
import py.una.pol.karaku.survey.components.DynamicSurveyField;
import py.una.pol.karaku.survey.components.DynamicSurveyField.Required;
import py.una.pol.karaku.survey.components.DynamicSurveyRow;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.ControllerTestConfiguration;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 24, 2015
 * 
 */

@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class DynamicSurveyDataTableTest extends BaseTest {

    @Configuration
    static class ContextConfiguration extends ControllerTestConfiguration {

    }

    private final DynamicSurveyDataTable survey;

    public DynamicSurveyDataTableTest() {

        List<EncuestaPlantillaPregunta> listaEncuesta = new ArrayList<EncuestaPlantillaPregunta>();
        EncuestaPlantillaPregunta encuesta = new EncuestaPlantillaPregunta();
        EncuestaPlantillaBloque bloque = new EncuestaPlantillaBloque();
        bloque.setTitulo("TITULO");
        encuesta.setBloque(bloque);
        encuesta.setDescripcion("DESC");
        encuesta.setOrden(1);
        encuesta.setLongitudRespuesta(10);
        encuesta.setObligatoria("SI");
        listaEncuesta.add(encuesta);

        survey = new DynamicSurveyDataTable(listaEncuesta, 0);
        survey.setRows(new ArrayList<DynamicSurveyRow>());
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyDataTable#addRow()}
     * 
     */
    @Test
    public void testAddRow() {

        for (int i = 0; i < 50; i++) {
            survey.addRow();
        }
        assertEquals("", survey.getGlobalMessage());
        assertEquals(50, survey.getRowsNumber());

        survey.addRow();
        /*
         * No existe cade internacionalizada para NOT_ADD_ROW, por eso se
         * utiliza "NOT_ADD_ROW&&&" en la siguiente comparación
         */
        assertEquals("NOT_ADD_ROW&&&", survey.getGlobalMessage());
        assertEquals(50, survey.getRowsNumber());
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyDataTable#deleteRow(int)}
     * 
     */
    @Test
    public void testDeleteRow() {

        for (int k = 0; k < 10; k++) {
            survey.addRow();
        }
        survey.deleteRow(0);
        assertEquals(9, survey.getRowsNumber());

        for (int k = 9; k > 0; k--) {
            survey.deleteRow(0);
            assertEquals(k - 1, survey.getRowsNumber());
        }

        survey.deleteRow(0);
        assertEquals("NOT_DELETE_ROW&&&", survey.getGlobalMessage());
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyDataTable#testBuildRows()}
     * 
     */
    @Test
    public void testBuildRows() {

        List<DynamicSurveyRow> rows = new ArrayList<DynamicSurveyRow>();
        EncuestaPlantillaPregunta plantilla = new EncuestaPlantillaPregunta();
        plantilla.setObligatoria("SI");

        DynamicSurveyField.Required[] fields = new DynamicSurveyField.Required[10];
        for (int i = 0; i < 10; i++) {
            plantilla.setId((long) i);
            plantilla.setOrden(i);
            plantilla.setLongitudRespuesta(7);
            plantilla.setDescripcion(String.valueOf(i));
            fields[i] = (Required) DynamicSurveyField.fieldFactory(plantilla);
        }

        DynamicSurveyRow surveyRow = new DynamicSurveyRow(fields);
        rows.add(surveyRow);
        surveyRow = new DynamicSurveyRow("", 0, 1);

        DynamicSurveyDataTable dynamic2 = survey.buildRows(rows);

        assertNotNull(dynamic2);

        rows.clear();
        rows.add(surveyRow);
        dynamic2 = survey.buildRows(rows);
    }
}
