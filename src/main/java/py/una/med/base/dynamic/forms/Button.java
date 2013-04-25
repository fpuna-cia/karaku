/*
 * @Button.java 1.0 Mar 13, 2013 Sistema Integral de Gestión Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import javax.el.MethodExpression;
import javax.faces.component.html.HtmlCommandButton;
import py.una.med.base.dynamic.util.SIGHSetPropertyActionListener;
import py.una.med.base.util.ELHelper;

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

	private HtmlCommandButton bind;

	/**
	 * Crea una nueva instancia con un botón sin acciones
	 */
	public Button() {

		bind = SIGHComponentFactory.getCommandButton();
		bind.setStyleClass("commandButton");
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

	/**
	 * @return bind
	 */
	public HtmlCommandButton getBind() {

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

	public void setText(final String key) {

		getBind().setValue(getMessage(key));
	}

	/**
	 * Asigna al componente para que actué de acuerdo a la acción pasada como
	 * Parámetro, corresponde al atributo <code>action</code> del
	 * {@link HtmlCommandButton}
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
	 * {@link HtmlCommandButton}, este método no recibe parámetros y debe
	 * retornar un objeto.
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
	 * @param bind
	 *            bind para setear
	 */
	public void setBind(final HtmlCommandButton bind) {

		this.bind = bind;
	}
}
