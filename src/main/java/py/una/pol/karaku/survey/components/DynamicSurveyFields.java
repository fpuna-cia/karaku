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

import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;
import py.una.pol.karaku.util.KarakuConverter;

/**
 * 
 * @author Nathalia Ochoa
 * @author Gabriela Vazquez
 * @since 1.0
 * @version 1.0 07/06/2013
 * 
 */

public class DynamicSurveyFields extends DynamicSurveyBlock {

	private DynamicSurveyFieldOption[] fields;
	private int fieldsNumber = 0;
	public static final String TYPE = "py.una.pol.karaku.survey.components.DynamicSurveyFields";
	private boolean expandir = false;

	/**
	 * Construye un bloque del tipo Simple.
	 * 
	 * @param questions
	 *            Lista de preguntas del bloque.
	 * @param index
	 *            Ubicacion del bloque dentro de la encuesta.
	 */
	public DynamicSurveyFields(List<EncuestaPlantillaPregunta> questions,
			int index) {

		super(questions, index);
		initFields(questions.size());
	}

	public DynamicSurveyFields buildFields() {

		this.fields = validateFields();
		return this;
	}

	private void initFields(int fieldsNumber) {

		this.fields = new DynamicSurveyFieldOption[fieldsNumber];
		this.setFieldsNumber(fieldsNumber);

	}

	public DynamicSurveyFieldOption[] getFields() {

		return fields.clone();
	}

	public void setFields(DynamicSurveyFieldOption[] fields) {

		this.fields = fields.clone();
	}

	/**
	 * 
	 * @param fieldValue
	 * @param index
	 *            ubicacion de la pregunta dentro del bloque
	 */
	public void setField(String fieldValue, int index) {

		this.fields[index - 1].setFieldValue(fieldValue);
	}

	public void setExpadir(boolean expandir) {

		this.expandir = expandir;
	}

	public boolean isExpadir() {

		return this.expandir;
	}

	@Override
	public String getType() {

		return TYPE;
	}

	/**
	 * Agrega un field al bloque.
	 * 
	 * @param element
	 */
	public void addField(DynamicSurveyFieldOption element) {

		this.fields[element.getField().getIndex() - 1] = element;
	}

	/**
	 * Valida los campos para cada pregunta.
	 * 
	 * @return
	 */
	private DynamicSurveyFieldOption[] validateFields() {

		for (int i = 0; i < fieldsNumber; i++) {
			if (fields[i] == null) {
				fields[i] = new DynamicSurveyFieldOption(getQuestions().get(i)
						.getTipoObjeto().getNombre());

				fields[i].setField(DynamicSurveyField
						.fieldFactory(getQuestions().get(i)));
				if (getQuestions().get(i).isObligatoria()) {
					setExpadir(true);
				}
			} 
		}
		return fields;
	}

	/**
	 * Obtiene el tipo de objeto de un field en particular.
	 * 
	 * @param index
	 *            Orden del Field o pregunta dentro del bloque.
	 * @return Puede retornar alguna de las siguientes opciones: TEXTO,
	 *         RADIO,CHECK,COMBO,TEXTO_FECHA,TEXTO_AREA.
	 */
	public String getTypeField(int index) {

		return getQuestions().get(index).getTipoObjeto().getNombre();
	}

	/**
	 * Busca el numero de la pregunta teniendo el tag.
	 * 
	 * @param tag
	 *            tag de la pregunta que deseamos buscar
	 * @return retorna un entero que representa el numero de orden de la
	 *         pregunta cuyo tag coincide con el pasado como parametro
	 */
	public int getQuestionNumber(String tag) {
		List<EncuestaPlantillaPregunta> listaPreguntas = getQuestions();
		for (EncuestaPlantillaPregunta pregunta : listaPreguntas) {
			if (pregunta.getTag().equals(tag)) {
				return pregunta.getOrden();
			}
		}
		return 0;
	}

	/**
	 * Retorna true si la pregunta recibida como parametro es requerida, de lo
	 * contrario retorna false.
	 * 
	 * @param index
	 *            Posicion de la pregunta dentro de la encuesta
	 * @return true si es requerida false si no es requerida
	 */
	public Boolean isRequiredField(int index) {

		return getQuestions().get(index).isObligatoria();
	}

	/**
	 * Retorna un String que representa el Style que tendrá una fila de tipo
	 * inputText. El tamaño es calculado teniendo en cuenta la longitud de la
	 * pregunta.
	 * 
	 * @param index
	 *            Posicion de la pregunta dentro de la encuesta
	 * @return tamaño del Style
	 */
	public String getStyleQuestion(int index) {
		Integer size = getQuestions().get(index).getLongitudRespuesta();
		String width = "width: ";
		if (size < 10) {
			return width + size * 15 + "px;";
		} else if (size < 100) {
			return width + size * 10 + "px;";
		}
		return width + size * 5 + "px;";
	}

	public Boolean isEditableField(int index) {

		return getQuestions().get(index).isEditable();
	}

	public int getFieldsNumber() {

		return fieldsNumber;
	}

	public final void setFieldsNumber(int fieldsNumber) {

		this.fieldsNumber = fieldsNumber;
	}

	/**
	 * Obtiene las opciones de respuestas para una determinada pregunta si
	 * aplica.
	 * 
	 * @param index
	 *            Ubicacion de la pregunta dentro del bloque
	 * @return
	 */

	public List<SelectItem> getAnswerOptions(int index) {

		List<SelectItem> list = new ArrayList<SelectItem>();
		for (OpcionRespuesta opcion : getQuestion(index + 1)
				.getOpcionRespuesta()) {
			list.add(new SelectItem(opcion, opcion.getDescripcion()));

		}
		return list;
	}

	/**
	 * Este converter es utilizado por los field del tipo ComboBox y radio, los
	 * cuales deben mantener el elemento que ha sido seleccionado.
	 * 
	 * @return converter
	 */
	public KarakuConverter getConverter() {

		return KarakuConverter.getInstance();
	}

	/**
	 * Este converter se utiliza para los field del tipo fecha,lo que hace es
	 * convertir el objeto fecha a string.
	 * 
	 * @return converterString
	 */
	public KarakuConverterString getConverterString() {

		return new KarakuConverterString();
	}

	/**
	 * Habilita el texto asociado a un check o radio, especificamente la opcion
	 * que permite completar. Ejemplo "Otros".
	 */
	public void enableCheckText() {

		int index = Integer.valueOf(getRequestParameter("index"));
		fields[index].setVisibilityCheckText(false);
		fields[index].enableCheckText();
	}

	/**
	 * Setea el valor ingresado en la caja de texto asociado a un check o radio
	 * en particular. Esto se debe hacer debido a que cuando se selecciona otra
	 * opcion el valor ingresado en la caja de texto aun no es submiteado motivo
	 * por el cual si no submiteamos explicitamente perderemos el valor.
	 * 
	 * @param event
	 */
	public void completeChangeListener(AjaxBehaviorEvent event) {

		int index = Integer.valueOf(getRequestParameter("index"));
		String value = (String) ((UIInput) event.getComponent()).getValue();
		fields[index].setFieldValue(value);
	}

}
