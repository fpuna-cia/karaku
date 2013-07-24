/*
 * @KarakuConverter.java 1.0 Jun 6, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.converters;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import py.una.med.base.dao.annotations.CaseSensitive;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 * Es un {@link Converter} que sanitiza las entidades, para que la sanitización
 * sea completa, la expresión que la define debe ser similar a
 * {@link #EXTRACT_FIELD_FROM_EXPRESSION_REGEX}. <br />
 * 
 * Bajo este contexto, sanitizar significa:
 * <ol>
 * <li>Eliminar espacios adelante y atras con {@link String#trim()}</li>
 * <li>Convertir a mayúsculas con {@link String#toUpperCase()}, siempre y cuando
 * la anotación {@link CaseSensitive} no este presente</li>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 6, 2013
 * 
 */
@FacesConverter(forClass = String.class)
public class StringConverter implements Converter {

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
	public final String EXTRACT_FIELD_FROM_EXPRESSION_REGEX = "(#\\{.*)\\.(.*)(\\})";
	private Pattern pattern;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (value == null || "".equals(value)) {
			return null;
		}

		String toRet = value.trim();

		ValueExpression val = component.getValueExpression("value");
		String beanExpression = val.getExpressionString();
		if (!(beanExpression == null || "".equals(beanExpression))) {

			Matcher ma = getPattern().matcher(beanExpression);
			if (ma.matches()) {
				String withoutField = ma.replaceFirst("$1$3");
				String field = ma.replaceFirst("$2");
				Object bean = context
						.getApplication()
						.getExpressionFactory()
						.createValueExpression(context.getELContext(),
								withoutField, Object.class)
						.getValue(context.getELContext());
				if (bean != null) {
					Field f;
					try {
						f = bean.getClass().getDeclaredField(field);
					} catch (Exception e) {
						throw new KarakuRuntimeException(
								"Can not find the field of the expression: "
										+ beanExpression);
					}
					if (f.getAnnotation(CaseSensitive.class) == null) {
						toRet = toRet.toUpperCase();
					}
				}

			}
		}
		return toRet;
	}

	private Pattern getPattern() {

		if (pattern == null) {
			pattern = Pattern.compile(EXTRACT_FIELD_FROM_EXPRESSION_REGEX);
		}
		return pattern;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {

		if (object == null) {
			return null;
		}

		if (object instanceof String) {
			return ((String) object).trim();
		}
		throw new KarakuRuntimeException(
				"StringConverter is only for strings (not for"
						+ object.getClass() + ")");
	}

}
