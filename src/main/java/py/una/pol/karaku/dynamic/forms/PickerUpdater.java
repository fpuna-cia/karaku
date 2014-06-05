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
package py.una.pol.karaku.dynamic.forms;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * {@link FacesComponent} que se encarga de realizar los controles necesarios
 * para el {@link PickerField}. Mediante los métodos
 * {@link #processUpdates(FacesContext)} y
 * {@link #processValidators(FacesContext)}
 * <p>
 * Utiliza un componente auxiliar para realizar la verificación de que si es o
 * no requerido. Este componente tiene como id <code>required</code> y si esta
 * presente, entonces el picker es necesario.
 * </p>
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 4, 2013
 * 
 */
@FacesComponent(value = "pickerUpdater")
@SuppressWarnings("rawtypes")
public class PickerUpdater extends UINamingContainer {

	private PickerField getPicker() {

		return (PickerField) getAttributes().get("pickerField");
	}

	/**
	 * Determina si el picker al que corresponde es o no requerido.
	 * 
	 * @return <code>true</code> si no admite valores nulos, <code>false</code>
	 *         si los permite.
	 */
	public Boolean isRequired() {

		UIComponent ui = findComponent("required");
		return ui != null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Relega el trabajo de actualizar a {@link PickerField#postUpdate()}.
	 * </p>
	 */
	@Override
	public void processUpdates(FacesContext context) {

		pushComponentToEL(context, this.getParent());
		getPicker().postUpdate();
		popComponentFromEL(context);
	}

	@Override
	public void processValidators(FacesContext context) {

		if (getPicker() == null) {
			return;
		}
		if (!getPicker().isNullable() && getPicker().getValue() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"", getPicker().getMessage("COMPONENT_PICKER_NOT_SELECTED"));
			String clientId = getPicker().findComponent(getPicker().getId())
					.getClientId(context);
			context.addMessage(clientId, msg);
			context.validationFailed();
			context.renderResponse();
		}
	}

}
