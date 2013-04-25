/*
 * @SimpleHeaderColumn.java 1.0 Mar 13, 2013 Sistema Integral de Gestión
 * Hospitalaria
 */
package py.una.med.base.dynamic.tables;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;

/**
 * Columna que tiene como cabecera un {@link HtmlInputText}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
public abstract class SimpleHeaderColumn extends Column {

	private HtmlOutputText header;

	public void setHeader(final HtmlOutputText header) {

		this.header = header;
	}

	@Override
	public HtmlOutputText getHeader() {

		return header;
	}

	public void setHeaderText(final String text) {

		getHeader().setValue(getI18nStringExpression(text));
	}

}
