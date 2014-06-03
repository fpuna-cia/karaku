/*
 * @QuantityConverter.java 1.0 Oct 16, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.jsf.converters;

import java.text.ParseException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOG = LoggerFactory
			.getLogger(QuantityConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		if (StringUtils.isInvalid(value)) {
			return null;
		}
		try {
			return getFormatProvider(context).parseQuantity(value);
		} catch (ParseException e) {
			LOG.trace("Can't parse quantity: ", value, e);
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object object) {

		return getFormatProvider(context).asNumber((Quantity) object);
	}

	protected FormatProvider getFormatProvider(FacesContext context) {

		return Util.getSpringBeanByJSFContext(context, FormatProvider.class);
	}

}
