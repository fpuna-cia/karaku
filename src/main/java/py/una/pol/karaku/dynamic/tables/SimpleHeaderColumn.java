/*
 * @SimpleHeaderColumn.java 1.0 Mar 13, 2013 Sistema Integral de Gestión
 * Hospitalaria
 */
package py.una.pol.karaku.dynamic.tables;

import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;

import py.una.pol.karaku.dynamic.forms.KarakuComponentFactory;
import py.una.pol.karaku.util.I18nHelper;

/**
 * Columna que tiene como cabecera un {@link HtmlInputText}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 13, 2013
 * 
 */
public abstract class SimpleHeaderColumn extends AbstractColumn {

	private HtmlOutputText header;

	public void setHeader(final HtmlOutputText header) {

		this.header = header;
	}

	@Override
	public HtmlOutputText getHeader() {

		return header;
	}

	public void setHeaderText(final String key) {

		if (getHeader() == null) {
			setHeader(KarakuComponentFactory.getHtmlOutputText());
		}
		getHeader().setValue(I18nHelper.getMessage(key));
	}

}
