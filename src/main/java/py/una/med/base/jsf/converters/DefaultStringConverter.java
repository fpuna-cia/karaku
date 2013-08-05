/*
 * @KarakuConverter.java 1.0 Jun 6, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Es un {@link FacesConverter} que no realiza ninguna acción, es utilizado
 * cuando no se desea utilizar el {@link StringConverter}, su uso es asi:
 * 
 * <pre>
 * &lt;h:selectOneMenu "
 * converter = &quot;oldStringConverter&quot; /&gt;
 *     ...
 * &lt;/h:selectOneMenu&gt;
 * </pre>
 * 
 * @author Arturo Volpe
 * @since 1.3.2
 * @version 1.0 Jun 6, 2013
 * 
 */
@FacesConverter(value = "oldStringConverter")
public class DefaultStringConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {

		if (object == null) {
			return "";
		}
		return object.toString();
	}

}
