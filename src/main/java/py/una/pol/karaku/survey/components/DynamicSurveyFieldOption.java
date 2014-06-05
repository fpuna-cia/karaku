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
import py.una.pol.karaku.survey.components.DynamicSurveyField.SurveyField;
import py.una.pol.karaku.survey.domain.OpcionRespuesta;
import py.una.pol.karaku.util.ListHelper;

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

	/**
	 * Verifica si el componente tiene asociadas varias respuestas.
	 * 
	 * <p>
	 * {@link #manyOptions} representa la lista de respuestas seleccionadas para
	 * un SelectManyCheckBox, entonces este método verifica si el componente
	 * posee al menos una respuesta seleccionada.
	 * 
	 * @return<code><b></code> Si posee al menos una respuesta. <br>
	 *                         <code>false</code> Caso contrario
	 */
	public boolean isManyOptionResponse() {

		return ListHelper.hasElements(getManyOptions());
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

	/**
	 * Verifica si el componente posee una respueta asociada y si la misma tiene
	 * una descripción.
	 * 
	 * @return
	 */
	public boolean isOneOptionResponse() {

		return getOneOption() != null
				&& getOneOption().getDescripcion() != null;
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

		if (isCheck()) {
			for (OpcionRespuesta option : getManyOptions()) {
				if (option.isCompletar()) {
					this.setVisibilityCheckText(true);
				}
			}
		} else {
			if (getOneOption() != null && getOneOption().isCompletar()) {
				this.setVisibilityCheckText(true);

			}
		}

	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	/**
	 * Verifica si el field es un SelectCheckManyOption.
	 * 
	 * @return
	 */
	public boolean isCheck() {

		return "CHECK".equals(getType());
	}

	/**
	 * Verifica si el field es un SelectOneRadio.
	 * 
	 * @return
	 */
	public boolean isRadio() {

		return "RADIO".equals(getType());
	}

	/**
	 * Verifica si el field es un SelectOneMenu.
	 * 
	 * @return
	 */
	public boolean isCombo() {

		return "COMBO".equals(getType());
	}

}
