/*
 * @DynamicSurveyFields.java 1.0 07/06/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.survey.components;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import py.una.pol.karaku.survey.domain.EncuestaPlantillaPregunta;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;
import py.una.pol.karaku.util.SIGHConverterV2;

/**
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 07/06/2013
 * 
 */
public class DynamicSurveyFields extends DynamicSurveyBlock {

	private DynamicSurveyFieldOption[] fields;
	private int fieldsNumber = 0;
	public static final String TYPE = "py.una.pol.karaku.survey.components.DynamicSurveyFields";

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
	public SIGHConverterV2 getConverter() {

		return new SIGHConverterV2();
	}

	/**
	 * Este converter se utiliza para los field del tipo fecha,lo que hace es
	 * convertir el objeto fecha a string.
	 * 
	 * @return converterString
	 */
	public SIGHConverterString getConverterString() {

		return new SIGHConverterString();
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
