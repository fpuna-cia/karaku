/*
 * @KarakuConverter.java 1.0 Jun 6, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.converters;

import java.lang.reflect.Field;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import py.una.med.base.dao.annotations.CaseSensitive;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.util.ELHelper;

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
 * @since 1.3.2
 * @version 1.0 Jun 6, 2013
 * 
 */
@FacesConverter(forClass = String.class)
public class StringConverter implements Converter {

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
			Field f = ELHelper.getFieldByExpression(beanExpression);
			if (f == null || f.getAnnotation(CaseSensitive.class) == null) {
				toRet = toRet.toUpperCase();
			}
		}
		return toRet;
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
