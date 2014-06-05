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

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.springframework.stereotype.Component;

/**
 * Utility class for converting between XMLGregorianCalendar and java.util.Date
 * 
 * @author Héctor Martínez
 * @since 1.0
 * @version 1.0 Jun 19, 2013
 * 
 */
@Component
public class ServiceUtil {

	private DatatypeFactory df = null;

	/**
	 * 
	 */
	public ServiceUtil() {

		// Creamos un DataTypeFactory para las conversiones a
		// XMLGregorianCalendar
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException dce) {
			throw new IllegalStateException(
					"Exception while obtaining DatatypeFactory instance", dce);
		}
	}

	/**
	 * Converts a {@link Date} into an instance of XMLGregorianCalendar
	 * 
	 * @param date
	 *            Instance of {@link Date} or a null reference
	 * @return {@link XMLGregorianCalendar} instance whose value is based upon
	 *         the value in the date parameter. If the date parameter is null
	 *         then this method will simply return null.
	 */
	public XMLGregorianCalendar asXMLGregorianCalendar(Date date) {

		if (date == null) {
			return null;
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(date.getTime());
		return df.newXMLGregorianCalendar(gc);
	}

	/**
	 * Converts an XMLGregorianCalendar to an instance of java.util.Date
	 * 
	 * @param xgc
	 *            Instance of {@link XMLGregorianCalendar} or a null reference
	 * @return {@link Date} instance whose value is based upon the value in the
	 *         xgc parameter. If the xgc parameter is null then this method will
	 *         simply return null.
	 */
	public Date asDate(XMLGregorianCalendar xgc) {

		if (xgc == null) {
			return null;
		}
		return xgc.toGregorianCalendar().getTime();
	}
}
