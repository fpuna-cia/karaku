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

import java.util.ArrayList;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import org.richfaces.component.UICommandButton;
import py.una.pol.karaku.util.ELHelper;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.ListHelper;

/**
 * Esta clase representa un boton ajax, el cual posee un textfield el cual puede
 * utilizarse para visualizar un determinado valor relacionado a la operacion
 * del boton.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 08/05/2013
 * 
 */
public class ButtonField extends TextField {

	private static final String TYPE = "py.una.pol.karaku.dynamic.forms.ButtonField";
	private UICommandButton buttonBind;

	private final AjaxBehavior ajaxBehavior;

	private final List<String> toRender;

	private boolean required;

	private ValueExpression inputExpression;
	private boolean inputEditable = true;

	/**
	 * @return required
	 */
	@Override
	public boolean isRequired() {

		return required;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.forms.TextField#setRequired(boolean)
	 */
	@Override
	public void setRequired(boolean required) {

		super.setRequired(required);
		this.required = required;
	}

	public ButtonField() {

		super();
		buttonBind = KarakuComponentFactory.getAjaxCommandButton();
		buttonBind.setImmediate(true);
		toRender = new ArrayList<String>(3);
		toRender.add(getId("label"));
		toRender.add(getId("input"));
		toRender.add(getId("button"));
		ajaxBehavior = KarakuComponentFactory.getAjaxBehavior();
		setClientBehavior("click");
		ajaxBehavior.setRender(toRender);

		setValue(ELHelper.INSTANCE.makeValueExpression("#{item.object}",
				Object.class));

	}

	private String getId(String part) {

		return this.getId() + "_" + part;
	}

	/**
	 * Define el nombre del boton
	 * 
	 * @param key
	 *            key de internacionalizacion que sera visualizado en el
	 *            componente
	 * @return mismo componente con un titulo
	 */
	public ButtonField setButtonText(final String key) {

		getButtonBind().setValue(I18nHelper.getMessage(key));

		return this;
	}

	/**
	 * Agrega una accion ajax al boton
	 * 
	 * @param eventName
	 * @return el mismo componente con una accion ajax
	 */
	private ButtonField setClientBehavior(final String eventName) {

		getButtonBind().addClientBehavior(eventName, ajaxBehavior);
		return this;
	}

	/**
	 * Habilita el boton ajax cuando se presiona doble click en el mismo
	 * 
	 * @param click
	 *            debe ser true para que la accion ajax se aplique
	 * @return el mismo componente con una accion ajax al presionar doble click
	 */
	public ButtonField setActionInDoubleClick(boolean click) {

		if (click) {
			setClientBehavior("dblclick");
		}
		return this;
	}

	/**
	 * Define el metodo que sera invocado por el boton ajax.
	 * 
	 * @param controller
	 *            controlador donde se define la accion que se desea invocar
	 * @param action
	 *            accion que sera invocada de manera ajax
	 * @return mismo componente
	 */
	public ButtonField setAction(final String controller, final String action) {

		ajaxBehavior.addAjaxBehaviorListener(ELHelper.INSTANCE
				.createAjaxBehaviorListener(controller, action));
		return this;
	}

	/**
	 * Retorna la accion que ejecutara el boton
	 * 
	 * @return
	 */
	public MethodExpression getAction() {

		return getButtonBind().getActionExpression();
	}

	/**
	 * Define los atributos del textfield asociado al boton
	 * 
	 * @param label
	 *            key de internacionalizacion ejemplo "PACIENTE_CODIGO" que será
	 *            visualizado como label del textfield
	 * @param expression
	 *            expresion que indica donde se almacenara el valor seteado en
	 *            el inputText
	 * @return boton con textfield configurado
	 */
	public ButtonField textField(String label, final ValueExpression expression) {

		setLabel(label);
		setValue(ELHelper.INSTANCE.makeValueExpression("#{item.object}",
				Object.class));
		inputExpression = expression;
		return this;
	}

	/**
	 * Define los elementos que seran renderizados por el boton, por defecto el
	 * textfield asociado es renderizado
	 * 
	 * @param toRender
	 *            lista de id cuyos componentes se desean renderizar
	 * @return
	 */
	public ButtonField setRender(final String toRender) {

		this.toRender.add(toRender);
		if (ajaxBehavior != null) {
			ajaxBehavior.setRender(this.toRender);
			ajaxBehavior.setExecute(ListHelper.getAsList("@none"));
		}
		return this;
	}

	public List<String> getRender() {

		return toRender;
	}

	@Override
	public String getType() {

		return TYPE;
	}

	@Override
	public boolean disable() {

		getButtonBind().setDisabled(true);
		return super.disable();
	}

	@Override
	public boolean enable() {

		getButtonBind().setDisabled(false);
		return super.enable();
	}

	public UICommandButton getButtonBind() {

		return buttonBind;
	}

	/**
	 * @param buttonBind
	 *            buttonBind para setear
	 */
	public void setButtonBind(UICommandButton buttonBind) {

		this.buttonBind = buttonBind;
	}

	public Object getObject() {

		FacesContext fc = FacesContext.getCurrentInstance();
		return this.inputExpression.getValue(fc.getELContext());
	}

	public void setObject(Object ob) {

		FacesContext fc = FacesContext.getCurrentInstance();
		inputExpression.setValue(fc.getELContext(), ob);
		return;
	}

	/**
	 * @param b
	 */
	public void setInputEditable(boolean b) {

		inputEditable = b;

	}

	/**
	 * @return inputEditable
	 */
	public boolean isInputEditable() {

		return inputEditable;
	}
}
