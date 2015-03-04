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
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import py.una.pol.karaku.survey.components.DynamicSurveyFieldOption;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 24, 2015
 * 
 */
public class DynamicSurveyFieldOptionTest {

    DynamicSurveyFieldOption fieldOption = new DynamicSurveyFieldOption("TYPE");

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyFieldOption#isOneOptionResponse()}
     * .
     */
    @Test
    public void testIsOneOptionResponse() {

        OpcionRespuesta option = new OpcionRespuesta();
        option.setDescripcion(null);

        assertFalse(fieldOption.isOneOptionResponse());

        fieldOption.setOneOption(option);
        assertFalse(fieldOption.isOneOptionResponse());

        option.setDescripcion("OPCION");
        fieldOption.setOneOption(option);
        assertTrue(fieldOption.isOneOptionResponse());
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.survey.components.DynamicSurveyFieldOption#enableCheckText()}
     * .
     */
    @Test
    public void testEnableCheckText() {

        fieldOption.setType("NOCHECK");
        OpcionRespuesta option = new OpcionRespuesta();
        option.setCompletar("SI");
        fieldOption.setOneOption(option);
        fieldOption.enableCheckText();
        assertTrue(fieldOption.getVisibilityCheckText());

        fieldOption.setVisibilityCheckText(false);
        fieldOption.setType("CHECK");
        fieldOption.enableCheckText();
        List<OpcionRespuesta> listaOpciones = new ArrayList<OpcionRespuesta>();
        listaOpciones.add(option);
        fieldOption.setManyOptions(listaOpciones);
        assertTrue(fieldOption.getVisibilityCheckText());

        fieldOption.setVisibilityCheckText(false);
        option.setCompletar("NO");
        listaOpciones.add(option);
        fieldOption.setManyOptions(listaOpciones);
        assertFalse(fieldOption.getVisibilityCheckText());
    }
}
