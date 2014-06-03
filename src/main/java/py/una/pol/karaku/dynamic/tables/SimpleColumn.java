/*
 * @SimpleColumn.java 1.0 Mar 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dynamic.tables;

import javax.el.ValueExpression;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import py.una.pol.karaku.dynamic.forms.SIGHComponentFactory;

/**
 * Columna que tiene como cabecera y cuerpo un {@link HtmlInputText}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
public class SimpleColumn extends SimpleHeaderColumn {

	private final HtmlOutputText body;

	public SimpleColumn() {

		super();
		body = SIGHComponentFactory.getHtmlOutputText();

	}

	/**
	 * Asigna a esta columna la expresión pasada como valor para el atributo
	 * <code>value</code>. Si el cuerpo no es un {@link HtmlOutputText} lanza
	 * una excepción
	 * 
	 * @param valueExpression
	 */
	public void bindExpressionValue(final ValueExpression valueExpression) {

		getBody().setValueExpression("value", valueExpression);
	}

	public void bindAttribute(final String attribute, final Class<?> returnType) {

		bindExpressionValue(getHelper().makeValueExpression(
				getAttributeStringExpression(attribute), returnType));
	}

	@Override
	public HtmlOutputText getBody() {

		return body;
	}

}
