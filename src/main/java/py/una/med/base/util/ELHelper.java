/**
 * @ELHelper.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.ajax4jsf.component.behavior.MethodExpressionAjaxBehaviorListener;
import org.springframework.stereotype.Component;

/**
 * Clase que provee funcionalidades para el uso de expresiones
 * 
 * @author Arturo Volpe Torres
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.1 May 24, 2013
 * 
 */
@Component
public class ELHelper {

	public static final ELHelper INSTANCE = new ELHelper();
	private static final String ACTION_METHOD = "#{CONTROLLER.METHOD}";

	/**
	 * Crea una expression que se utiliza para representar el tipo
	 * 
	 * @param expression
	 * @param type
	 * @return value expression con la expression pasada que retorna el type
	 *         especificado
	 */
	public ValueExpression makeValueExpression(final String expression,
			final Class<?> type) {

		ValueExpression ve = getExpressionFactory().createValueExpression(
				getElContext(), expression, type);
		return ve;
	}

	public MethodExpression makeMethodExpression(final String expression,
			final Class<?> expectedReturnType,
			final Class<?> ... expectedParamTypes) {

		MethodExpression methodExpression = null;
		try {
			FacesContext fc = getContext();
			ExpressionFactory factory = getExpressionFactory();
			methodExpression = factory.createMethodExpression(
					fc.getELContext(), expression, expectedReturnType,
					expectedParamTypes);

			return methodExpression;
		} catch (Exception e) {
			throw new FacesException("Method expression '" + expression
					+ "' no se puede crear.");
		}
	}

	/**
	 * Este método debe retornar un String que se usara para el mapa de
	 * navegación, retornar null o "" para refrescar la página
	 * 
	 * @param controller
	 * @param action
	 * @return
	 */
	public MethodExpression makeActionMethodExpression(final String controller,
			final String action) {

		String expression = formatExpression(controller, action);
		return makeMethodExpression(expression, String.class);
	}

	/**
	 * Crea un método con el formato #{CONTROLLER.ACTION}
	 * 
	 * @param controller
	 * @param action
	 * @return
	 */
	private String formatExpression(final String controller, final String action) {

		return ACTION_METHOD.replaceFirst("CONTROLLER", controller)
				.replaceFirst("METHOD", action);
	}

	/**
	 * Este método genera la expresion para invocar a un metodo utilizando ajax.
	 * 
	 * @param controller
	 * @param action
	 * @return
	 */
	public MethodExpressionAjaxBehaviorListener createAjaxBehaviorListener(
			final String controller, final String action) {

		String expression = formatExpression(controller, action);
		return new MethodExpressionAjaxBehaviorListener(
				makeMethodExpression(expression, Void.class,
						new Class[] { AjaxBehaviorEvent.class }));
	}

	private ExpressionFactory getExpressionFactory() {

		return getApplication().getExpressionFactory();
	}

	/**
	 * @return context
	 */
	public FacesContext getContext() {

		return FacesContext.getCurrentInstance();
	}

	/**
	 * @return application
	 */
	public Application getApplication() {

		return getContext().getApplication();
	}

	/**
	 * @return elContext
	 */
	public ELContext getElContext() {

		return getContext().getELContext();
	}

}
