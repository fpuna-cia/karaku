/*
 * @DynamicSurveyBlock.java 1.0 04/06/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.components;

import java.util.ArrayList;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;

/**
 * r
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

	/*
	 * (non-Javadoc)
	 *
	 * @see py.una.pol.karaku.survey.IDynamicSurveyBlock#getType()
	 */
	@Override
	public String getType() {

		// TODO Auto-generated method stub
		return "";
	}

}
