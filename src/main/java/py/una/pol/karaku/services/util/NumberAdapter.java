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

import java.text.ParseException;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.util.FormatProvider;

/**
 *
 *
 * @author Arturo Volpe
 * @since 2.3.0
 * @version 1.0 Dec 27, 2013
 *
 */
public class NumberAdapter extends XmlAdapter<String, Quantity> {

	@Autowired
	private FormatProvider fp;

	public static final NumberAdapter INSTANCE = new NumberAdapter();

	/**
	 * Convierte un BigDecimal a un elemento base:Number.
	 *
	 * @param numero
	 *            BigDecimal a convertir a un base:Number.
	 * @return Cadena en base:Number
	 */
	public String marshal(final Quantity numero) {

		return INSTANCE.fp.asNumber(numero);
	}

	/**
	 * Convierte un elemento base:Number a un BigDecimal.
	 *
	 * @param decimal
	 *            Elemento base:Number.
	 * @return una nueva cantidad con el decimal especificado en un mismo
	 *         formato que {@link java.math.BigDecimal#BigDecimal(String)}.
	 */
	public Quantity unmarshal(final String decimal) {

		try {
			return INSTANCE.fp.parseLongQuantity(decimal);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Cant parse null BigIntegers", e);
		}

	}
}
