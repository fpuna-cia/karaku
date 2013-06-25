/*
 * @Button.java 1.0 Mar 13, 2013 Sistema Integral de Gestión Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.event.ActionListener;
import javax.faces.event.MethodExpressionActionListener;
import org.richfaces.component.UICommandButton;
import py.una.med.base.dynamic.util.SIGHSetPropertyActionListener;
import py.una.med.base.util.ELHelper;
import py.una.med.base.util.StringUtils;

/**
 * Clase que representa un botón, es un Facade que facilita su creación a través
 * de código JAVA
 * 
 * @author Arturo Volpe Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
public class Button extends Field {

	/**
	 * Interfaz que define un callBack que será invocado cuando se presiones el
	 * botón, véase {@link Button#setClickCallBack(OnClickCallBack)}
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Jun 17, 2013
	 * 
	 */
	public interface OnClickCallBack {

		void onClick();
	}

	/**
	 * @return
	 * @see javax.faces.component.UICommand#isImmediate()
	 */
	public boolean isImmediate() {

		return bind.isImmediate();
	}

	/**
	 * @param immediate
	 * @see javax.faces.component.UICommand#setImmediate(boolean)
	 */
	public void setImmediate(boolean immediate) {

		bind.setImmediate(immediate);
	}

	private UICommandButton bind;
	private OnClickCallBack currentCallBack;

	/**
	 * Crea una nueva instancia con un botón sin acciones
	 */
	public Button() {

		bind = SIGHComponentFactory.getAjaxCommandButton();
		bind.setStyleClass("commandButton");

	}

	/**
	 * Convierte este boton en un boton, ajax, para esto le agrega
	 * comportamiento del lado del cliente.
	 */
	public void addAjaxBehavior(ButtonAction action, ClientBehavior behaviour) {

		getBind().addClientBehavior(action.getActionName(), behaviour);
	}

	public void setRender(String ... toRender) {

		getBind().setRender(StringUtils.join(" ", toRender));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#disable()
	 */
	@Override
	public boolean disable() {

		getBind().setDisabled(true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#enable()
	 */
	@Override
	public boolean enable() {

		getBind().setDisabled(false);
		return true;
	}

	public void setDisableValue(String valueExpression) {

		ValueExpression expression = ELHelper.INSTANCE.makeValueExpression(
				valueExpression, java.lang.Boolean.class);
		getBind().setValueExpression("disabled", expression);
	}

	/**
	 * Cambia el valor enabled del componente.
	 * 
	 * @param nuevo
	 *            estado del componente
	 */
	public void setEnabled(boolean enabled) {

		if (enabled) {
			enable();
		} else {
			disable();
		}
	}

	/**
	 * Retorna el componente que esta clase encapsula.
	 * 
	 * @return {@link UICommandButton} encapsulado
	 */
	public UICommandButton getBind() {

		return bind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.dynamic.forms.Field#getType()
	 */
	@Override
	public String getType() {

		return "py.una.med.base.dynamic.forms.Button";
	}

	/**
	 * Asigna el texto del boton.
	 * 
	 * @param key
	 *            del archivo de internacionalizacion
	 */
	public void setText(final String key) {

		getBind().setValue(getMessage(key));
	}

	/**
	 * Asigna al componente para que actué de acuerdo a la acción pasada como
	 * Parámetro, corresponde al atributo <code>action</code> del
	 * {@link #getBind()}
	 * 
	 * 
	 * @param expression
	 *            MethodExpression representing the application action to invoke
	 *            when this component is activated by the user. The expression
	 *            must evaluate to a public method that takes no parameters, and
	 *            returns an Object (the toString() of which is called to derive
	 *            the logical outcome) which is passed to the NavigationHandler
	 *            for this application.
	 */
	public void setAction(final MethodExpression expression) {

		getBind().setActionExpression(expression);
	}

	/**
	 * Asigna al componente para que actué de acuerdo a la acción pasada como
	 * Parámetro, corresponde al atributo <code>action</code> del
	 * {@link #getBind()}, este método no recibe parámetros y debe retornar un
	 * objeto.
	 * 
	 * 
	 * @param expression
	 *            MethodExpression representing the application action to invoke
	 *            when this component is activated by the user. The expression
	 *            must evaluate to a public method that takes no parameters, and
	 *            returns an Object (the toString() of which is called to derive
	 *            the logical outcome) which is passed to the NavigationHandler
	 *            for this application.
	 */
	public void setAction(final String expression) {

		MethodExpression methodExpression = ELHelper.INSTANCE
				.makeMethodExpression(expression, Object.class, (Class<?>) null);
		getBind().setActionExpression(methodExpression);
	}

	/**
	 * Agrega un action listener del tipo SET, el cual copia lo que hay en
	 * <code>source</code> a <code>target</code>, para esto, el target debe ser
	 * Asignable con el source, tienen que ser expresiones validas que retornen
	 * un objeto
	 * 
	 * @param source
	 *            EL expression
	 * @param target
	 *            EL expression
	 */
	public void addPropertyActionListener(final String source,
			final String target) {

		getBind().addActionListener(
				new SIGHSetPropertyActionListener(source, target));
	}

	/**
	 * Modifica el elemento que es encapsulado por esta clase
	 * 
	 * @param UICommandButton
	 *            que sera encapsulado por esta clase
	 * 
	 */
	public void setBind(final UICommandButton bind) {

		this.bind = bind;
	}

	/**
	 * @param listener
	 * @see javax.faces.component.UICommand#addActionListener(javax.faces.event.ActionListener)
	 */
	public void addActionListener(ActionListener listener) {

		bind.addActionListener(listener);
	}

	/**
	 * Agrega un {@link OnClickCallBack} que sera invocado cuando se presione
	 * click sobre el botón
	 * 
	 * @param callBack
	 */
	public void setClickCallBack(final OnClickCallBack callBack) {

		this.currentCallBack = callBack;
		MethodExpression meCallBack = getElHelper().makeMethodExpression(
				"#{cc.attrs.field.onClick()}", void.class, void.class);
		addActionListener(new MethodExpressionActionListener(meCallBack));
	}

	/**
	 * Método que es invocado cuando se presiona click sobre el botón, véase
	 * {@link #setClickCallBack(OnClickCallBack)}.
	 */
	public void onClick() {

		if (currentCallBack == null) {
			return;
		}
		currentCallBack.onClick();
	}

	/**
	 * @param style
	 * @see org.richfaces.component.UICommandButton#setStyle(java.lang.String)
	 */
	public void setStyle(String style) {

		bind.setStyle(style);
	}

}
