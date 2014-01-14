/*
 * @DynamicSurveyFieldOption.java 1.0 13/06/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.survey.components;

import java.util.ArrayList;
import java.util.List;
import py.una.med.base.survey.components.DynamicSurveyField.SurveyField;
import py.una.med.base.survey.domain.OpcionRespuesta;

/**
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 13/06/2013
 * 
 */
public class DynamicSurveyFieldOption {

	private SurveyField field;
	private List<OpcionRespuesta> manyOptions;
	private OpcionRespuesta oneOption;
	private boolean visibilityCheckText;
	private String type;

	/**
	 * 
	 */
	public DynamicSurveyFieldOption(String type) {

		this.manyOptions = new ArrayList<OpcionRespuesta>();
		this.oneOption = new OpcionRespuesta();
		this.type = type;

	}

	public List<OpcionRespuesta> getManyOptions() {

		return manyOptions;
	}

	public void setManyOptions(List<OpcionRespuesta> options) {

		this.manyOptions = options;
		enableCheckText();
	}

	public SurveyField getField() {

		return field;
	}

	public void setField(SurveyField field) {

		this.field = field;
	}

	public void setFieldValue(String value) {

		this.field.setValue(value);
	}

	public OpcionRespuesta getOneOption() {

		return oneOption;
	}

	public void setOneOption(OpcionRespuesta oneOption) {

		this.oneOption = oneOption;
		enableCheckText();
	}

	public boolean getVisibilityCheckText() {

		return visibilityCheckText;
	}

	public void setVisibilityCheckText(boolean visibilityCheckText) {

		this.visibilityCheckText = visibilityCheckText;
	}

	/**
	 * Verifica si al opcion "OTROS" se encuentra seleccionada, en dicho caso
	 * habilita la caja de texto asociada.
	 */
	public void enableCheckText() {

		if ("RADIO".equals(type) && oneOption != null) {
			if ("SI".equals(oneOption.getCompletar())) {
				this.setVisibilityCheckText(true);
			}
		} else {
			if ("CHECK".equals(type)) {
				for (OpcionRespuesta option : getManyOptions()) {
					if ("SI".equals(option.getCompletar())) {
						this.setVisibilityCheckText(true);
					}
				}
			}
		}
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

}
