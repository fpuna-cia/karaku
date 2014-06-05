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
package py.una.pol.karaku.util;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.ajax4jsf.component.behavior.MethodExpressionAjaxBehaviorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Clase que provee funcionalidades para el uso de expresiones
 * 
 * @author Arturo Volpe Torres
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.3.2 25/07/2013
 * 
 */
@Component
public class ELHelper {

	/**
	 * 
	 */
	private static final String CAN_NOT_FIND_THE_FIELD_OF_THE_EXPRESSION = "Can not find the field of the expression: {}";
	public static final ELHelper INSTANCE = new ELHelper();
	private static final String ACTION_METHOD = "#{CONTROLLER.METHOD}";

	/**
	 * Dada una expresión del tipo:<br />
	 * 
	 * <pre>
	 * 		#{controller.bean.field}
	 * </pre>
	 * 
	 * Determina tres grupos, de la siguiente forma
	 * <table>
	 * <tr>
	 * <th>Expresión</th>
	 * <th>Significado</th>
	 * </tr>
	 * <tr>
	 * <td>'#{controller.bean':</td>
	 * <td>Parte que determina el bean, completar con <code>}</code> para
	 * obtener la expresión.</td>
	 * </tr>
	 * <tr>
	 * <td>'nombre1':</td>
	 * <td>Parte que determina el nombre del atributo.</td>
	 * </tr>
	 * <tr>
	 * <td>'}':</td>
	 * <td>Parte que completa la Expresión</td>
	 * </tr>
	 * </table>
	 * <br />
	 * <br />
	 * Reemplazando por ejemplo con $1$3 obtenemos la expresion:
	 * 
	 * <pre>
	 * #{controller.bean}
	 * </pre>
	 */
	public static final String EXTRACT_FIELD_FROM_EXPRESSION_REGEX = "(#\\{.*)\\.(.*)(\\})";
	private static Pattern pattern;

	private static final Logger LOG = LoggerFactory.getLogger(ELHelper.class);

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
					+ "' no se puede crear.", e);
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

	/**
	 * Dada una expresión del tipo
	 * 
	 * <pre>
	 * 		#{controller.bean.field}
	 * </pre>
	 * 
	 * Retorna el {@link Field} donde se almacenara el campo field, notar que es
	 * a nivel de {@link Field}, es decir, requiere que el getter y el setter,
	 * tengan el mismo nombre (getField, setField), no sirve para campos que no
	 * cumplan esta condicion.
	 * 
	 * @param beanExpression
	 *            expresión con el formato de
	 *            {@link #EXTRACT_FIELD_FROM_EXPRESSION_REGEX}
	 * @return {@link Field} del atributo
	 * @since 1.3.2
	 */
	public static Field getFieldByExpression(String beanExpression) {

		// abre y cierra un paréntesis, se utiliza para definir si utiliza algún
		// método.
		if (beanExpression.indexOf('(') != -1
				&& beanExpression.indexOf(')') != -1) {
			return null;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		Matcher ma = getPattern().matcher(beanExpression);
		if (ma.matches()) {
			String withoutField = ma.replaceFirst("$1$3");
			String field = ma.replaceFirst("$2");
			Object bean;
			try {
				bean = context
						.getApplication()
						.getExpressionFactory()
						.createValueExpression(context.getELContext(),
								withoutField, Object.class)
						.getValue(context.getELContext());
			} catch (ELException el) {
				// TODO mejorar para tener en cuenta cuando la expresión se
				// refiere a un Vector.
				// Utilizar la expresión regular
				// (#\{.*)\.([a-zA-Z]*|[a-zA-Z]*\[[a-zA-Z\.]*\])(\})
				// See http://www.regexplanet.com/advanced/java/index.html
				LOG.info(CAN_NOT_FIND_THE_FIELD_OF_THE_EXPRESSION,
						beanExpression, el);
				return null;
			}
			if (bean != null) {
				Field f;
				try {
					f = bean.getClass().getDeclaredField(field);
				} catch (Exception e) {
					try {
						LOG.trace(
								"Can't get field by reflection, trying using proxies",
								e);
						f = getFieldForce(field, bean.getClass());
					} catch (Exception e2) {
						LOG.debug(CAN_NOT_FIND_THE_FIELD_OF_THE_EXPRESSION,
								beanExpression, e2);
						return null;
					}
				}
				return f;
			}
		}
		return null;
	}

	private static Field getFieldForce(String name, Class<?> clazz) {

		if (clazz.getName().toUpperCase().contains("CGLIB")) {
			return getFieldFromCGEnhancedClass(name, clazz);
		}
		// TODO ver cuando se utiliza javassist
		return null;
	}

	private static Field getFieldFromCGEnhancedClass(String name, Class<?> clazz) {

		Class<?> real = clazz.getSuperclass();
		return ReflectionUtils.findField(real, name);
	}

	private static synchronized Pattern getPattern() {

		if (pattern == null) {
			pattern = Pattern
					.compile(ELHelper.EXTRACT_FIELD_FROM_EXPRESSION_REGEX);
		}
		return pattern;
	}
}
