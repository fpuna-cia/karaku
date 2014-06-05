/*
 * @ConverterString.java 1.0 13/06/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.survey.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import py.una.pol.karaku.util.StringUtils;

/**
 * Define un converter el cual dado un objeto lo convierte a String y viceversa.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 13/06/2013
 * 
 */
@Deprecated
public class KarakuConverterString implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (StringUtils.isInvalid(value)) {
			return null;
		}
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		if (!StringUtils.isValid(value)) {
			return "";
		}

		return value.toString();
	}

}
