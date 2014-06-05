/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.jsf.converters;

import java.text.ParseException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.util.FormatProvider;
import py.una.pol.karaku.util.StringUtils;
import py.una.pol.karaku.util.Util;

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
