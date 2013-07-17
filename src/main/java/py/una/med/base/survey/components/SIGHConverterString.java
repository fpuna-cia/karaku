/*
 * @ConverterString.java 1.0 13/06/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.survey.components;

import java.util.Date;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Define un converter el cual dado un objeto lo convierte a String y viceversa.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 13/06/2013
 * 
 */
public class SIGHConverterString implements Converter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		Date date = null;
		if (value == null || value.isEmpty()) {
			return date;
		} else {
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		return value.toString();
	}

}
