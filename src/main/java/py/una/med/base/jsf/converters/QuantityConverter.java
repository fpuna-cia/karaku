/*
 * @QuantityConverter.java 1.0 Oct 16, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import py.una.med.base.math.Quantity;
import py.una.med.base.util.FormatProvider;
import py.una.med.base.util.StringUtils;
import py.una.med.base.util.Util;

/**
 * Conversor JSF de cadenas a {@link Quantity}.
 *
 * @author Nathalia Ochoa
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 16/10/2013
 *
 */
@FacesConverter(forClass = Quantity.class)
public class QuantityConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (StringUtils.isInvalid(value)) {
			return null;
		}
		return new Quantity(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {

		return Util.getSpringBeanByJSFContext(context, FormatProvider.class)
				.asNumber((Quantity) object);
	}

}
