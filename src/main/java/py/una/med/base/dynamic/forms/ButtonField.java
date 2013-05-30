/**
 * @ButtonField.java 1.1 08/05/2013. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.behavior.AjaxBehavior;
import org.richfaces.component.UICommandButton;
import py.una.med.base.util.ELHelper;
import py.una.med.base.util.I18nHelper;

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
public class ButtonField extends LabelField {

	private static final String TYPE = "py.una.med.base.dynamic.forms.ButtonField";
	private TextField textField;
	private UICommandButton bind;

	private final AjaxBehavior ajaxBehavior;

	private final List<String> toRender;

	public ButtonField() {

		super();
		bind = SIGHComponentFactory.getAjaxCommandButton();
		bind.setImmediate(true);
		toRender = new ArrayList<String>();
		ajaxBehavior = SIGHComponentFactory.getAjaxBehavior();
		setClientBehavior("click");
		ajaxBehavior.setRender(Arrays.asList(getId()));

	}

	/**
	 * Define el nombre del boton
	 * 
	 * @param key
	 *            key de internacionalizacion que sera visualizado en el
	 *            componente
	 * @return mismo componente con un titulo
	 */
	public ButtonField setText(final String key) {

		getBind().setValue(I18nHelper.getMessage(key));

		return this;
	}

	/**
	 * Agrega una accion ajax al boton
	 * 
	 * @param eventName
	 * @return el mismo componente con una accion ajax
	 */
	private ButtonField setClientBehavior(final String eventName) {

		bind.addClientBehavior(eventName, ajaxBehavior);
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

		return getBind().getActionExpression();
	}

	/**
	 * Define los atributos del textfiel asociado al boton
	 * 
	 * @param label
	 *            key de internacionalizacion ejemplo "PACIENTE_CODIGO" que sera
	 *            visualizado como label del textfield
	 * @param expression
	 *            expresion que indica donde se almacenara el valor seteado en
	 *            el inputText
	 * @return boton con textfield configurado
	 */
	public ButtonField textField(String label, final ValueExpression expression) {

		getTextField().setLabel(label);
		getTextField().setValue(expression);
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

		getBind().setDisabled(true);
		return true;
	}

	@Override
	public boolean enable() {

		getBind().setDisabled(false);
		return true;
	}

	public TextField getTextField() {

		if (textField == null) {
			textField = new TextField();
		}
		return textField;
	}

	public void setBind(UICommandButton bind) {

		this.bind = bind;
	}

	public UICommandButton getBind() {

		return bind;
	}

}
