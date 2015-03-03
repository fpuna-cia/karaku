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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import py.una.pol.karaku.survey.components.DynamicSurveyField;
import py.una.pol.karaku.survey.components.DynamicSurveyField.Required;
import py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 24, 2015
 * 
 */
public class DynamicSurveyFieldTest {

    EncuestaPlantillaPregunta encuesta = new EncuestaPlantillaPregunta();

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyField#fieldFactory(py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta)}
     * .
     */
    @Test
    public void testFieldFactory() {

        encuesta.setOrden(5);
        encuesta.setLongitudRespuesta(7);
        encuesta.setObligatoria("SI");
        SurveyField survey = DynamicSurveyField.fieldFactory(encuesta);

        assertNotNull(survey);

        encuesta.setObligatoria("NO");
        survey = null;
        survey = DynamicSurveyField.fieldFactory(encuesta);

        assertNotNull(survey);
    }

    @Test
    public void testRequired() {

        DynamicSurveyField.Required required;

        required = new Required(0, 5);
        required.setIndex(0);
        required.setValue("HOLA");
        assertTrue(required.isValidate());

        required.setValue("HOLA12456");
        assertFalse(required.isValidate());

        required.setValue(null);
        assertTrue(required.isValidate());

    }
}
