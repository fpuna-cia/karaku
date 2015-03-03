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
package py.una.pol.karaku.survey.components;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;

/**
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 04/06/2013
 * 
 */
public class DynamicSurveyBlock implements IDynamicSurveyBlock {

    private String title;
    private final String id;
    private List<EncuestaPlantillaPregunta> questions;

    /**
     * 
     * @param questions
     *            Lista de preguntas relacionadas al bloque
     * @param index
     *            Ubicacion del bloque dentro de la encuesta
     */
    public DynamicSurveyBlock(List<EncuestaPlantillaPregunta> questions,
            int index) {

        this.title = questions.get(0).getBloque().getTitulo();
        this.questions = questions;
        this.id = generateIdBlock(index);
    }

    /**
     * Obtiene el Label a ser visualizado para cada pregunta del bloque
     */
    @Override
    public List<String> getLabels() {

        List<String> columns = new ArrayList<String>();
        for (EncuestaPlantillaPregunta pregunta : questions) {
            columns.add(pregunta.getDescripcion());
        }
        return columns;
    }

    /**
     * @return questions
     */
    protected List<EncuestaPlantillaPregunta> getQuestions() {

        return questions;
    }

    /**
     * @param questions
     *            questions para setear
     */
    protected void setQuestions(List<EncuestaPlantillaPregunta> questions) {

        this.questions = questions;
    }

    /**
     * Obtiene una pregunta en particular del bloque.
     * 
     * @param index
     *            Representa el orden de la pregunta dentro del bloque
     * @return
     */
    public EncuestaPlantillaPregunta getQuestion(int index) {

        return this.questions.get(index - 1);
    }

    /**
     * Genera un identificador para el bloque en cuestion
     */

    public final String generateIdBlock(int index) {

        return "_block_" + (index + 1);
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    @Override
    public String getId() {

        return id;
    }

    /**
     * Obtiene el valor del parametro asociado a un compoenente.
     * 
     * @param name
     *            nombre del parametro
     * @return
     */
    public String getRequestParameter(String name) {

        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        return externalContext.getRequestParameterMap().get(name);
    }

    @Override
    public String getType() {

        return "";
    }

}
