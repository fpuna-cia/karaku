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
package py.una.pol.karaku.services.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import py.una.pol.karaku.math.Quantity;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 2.3.0
 * @version 1.0 Dec 27, 2013
 * 
 */
public class CurrencyAdapter extends XmlAdapter<String, Quantity> {

	/**
	 * Convierte un BigDecimal a un elemento base:Currency.
	 * 
	 * @param currency
	 *            currency a convertir a base:Currency.
	 * @return Cadena en base:Currency.
	 */
	public String marshal(final Quantity currency) {

		return currency.toString();

	}

	/**
	 * Convierte un elemento base:Currency a un Quantity.
	 * 
	 * @param currency
	 *            elemento base:currency.
	 * @return en caso de que base:Currency sea un número entero sin signo
	 *         retorna su representación en BigDecimal, en caso contrario
	 *         retorna null.
	 */
	public Quantity unmarshal(final String currency) {

		if (currency.matches("^[0-9]+$")) {
			return new Quantity(currency);
		}
		return null;
	}

}
