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
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import py.una.pol.karaku.survey.components.DynamicSurveyBlock;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaBloque;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 24, 2015
 * 
 */
public class DynamicSurveyBlockTest {

    DynamicSurveyBlock2 dynamic;

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyBlock#getLabels()}
     * .
     */
    @Test
    public void testGetLabels() {

        EncuestaPlantillaPregunta encuesta = new EncuestaPlantillaPregunta();
        EncuestaPlantillaBloque bloque = new EncuestaPlantillaBloque();
        bloque.setTitulo("TITULO");
        encuesta.setBloque(bloque);
        encuesta.setDescripcion("DESCRIPCION");
        List<EncuestaPlantillaPregunta> listEncuesta = new ArrayList<EncuestaPlantillaPregunta>();
        listEncuesta.add(encuesta);

        dynamic = new DynamicSurveyBlock2(listEncuesta, 0);

        List<String> labels = dynamic.getLabels();

        assertEquals("DESCRIPCION", labels.get(0));
        dynamic.emptyQuestions();
        labels = dynamic.getLabels();
        assertEquals(0, labels.size());

    }
}

class DynamicSurveyBlock2 extends DynamicSurveyBlock {

    /**
     * @param questions
     * @param index
     */
    public DynamicSurveyBlock2(List<EncuestaPlantillaPregunta> questions,
            int index) {

        super(questions, index);
    }

    /*
     * Solo para propósitos de Test
     */
    public void emptyQuestions() {

        super.setQuestions(new ArrayList<EncuestaPlantillaPregunta>());
    }

}
