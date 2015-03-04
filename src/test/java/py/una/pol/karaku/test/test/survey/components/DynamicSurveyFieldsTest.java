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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.junit.Test;
import py.una.pol.karaku.survey.components.DynamicSurveyFieldOption;
import py.una.pol.karaku.survey.components.DynamicSurveyFields;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;
import py.una.pol.karaku.survey.domain.TipoObjeto;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 25, 2015
 * 
 */
public class DynamicSurveyFieldsTest {

    DynamicSurveyFields fields;

    public DynamicSurveyFieldsTest() {

        EncuestaPlantillaPregunta encuesta = new EncuestaPlantillaPregunta();
        encuesta.setOrden(0);
        encuesta.setDescripcion("DESC");
        encuesta.setLongitudRespuesta(7);
        encuesta.setObligatoria("SI");
        TipoObjeto tipoObjeto = new TipoObjeto();
        tipoObjeto.setId(0L);
        tipoObjeto.setNombre("TIPOOBJETO");
        encuesta.setTipoObjeto(tipoObjeto);
        EncuestaPlantillaBloque bloque = new EncuestaPlantillaBloque();
        bloque.setTitulo("TITULO_BLOQUE");
        encuesta.setBloque(bloque);

        List<EncuestaPlantillaPregunta> listaEncuesta = new ArrayList<EncuestaPlantillaPregunta>();
        listaEncuesta.add(encuesta);

        fields = new DynamicSurveyFields(listaEncuesta, 0);
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyFields#buildFields()}
     * .
     */
    @Test
    public void testBuildFields() {

        fields.buildFields();

        assertTrue(fields.isExpadir());

        fields.setFields(new DynamicSurveyFieldOption[1]);

        fields.setExpadir(false);
        fields.getQuestion(1).setObligatoria("NO");

        fields.buildFields();

        assertFalse(fields.isExpadir());

        fields.buildFields();

        assertFalse(fields.isExpadir());
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyFields#getAnswerOptions(int)}
     * .
     */
    @Test
    public void testGetAnswerOptions() {

        List<OpcionRespuesta> opcionRespuesta = new ArrayList<OpcionRespuesta>();
        OpcionRespuesta opcion = new OpcionRespuesta();
        opcion.setDescripcion("OPCION");
        opcionRespuesta.add(opcion);

        fields.getQuestion(1).setOpcionRespuesta(opcionRespuesta);

        List<SelectItem> respuestas = fields.getAnswerOptions(0);
        assertEquals(1, respuestas.size());
    }
}
