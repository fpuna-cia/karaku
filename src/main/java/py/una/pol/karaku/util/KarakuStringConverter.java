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
package py.una.pol.karaku.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 15, 2013
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KarakuStringConverter extends KarakuConverter {

	private LabelProvider provider;

	/**
	 * 
	 */
	public KarakuStringConverter() {

		this(null);
	}

	/**
	 * @param provider
	 */
	public KarakuStringConverter(final LabelProvider<?> provider) {

		super();
		this.provider = provider;
	}

	@Override
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String value) {

		if (provider == null) {
			return super.getAsObject(context, component, value);
		} else {
			return provider.getAsString(super.getAsObject(context, component,
					value));
		}
	}
}
