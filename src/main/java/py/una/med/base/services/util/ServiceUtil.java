/*
 * @XMLGregorianCalendarConverter.java 1.0 Jun 19, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.services.util;

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
			throw new IllegalArgumentException(
					"The argument date cannot be null");
		} else {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTimeInMillis(date.getTime());
			return df.newXMLGregorianCalendar(gc);
		}
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
			throw new IllegalArgumentException("xgc");
		} else {
			return xgc.toGregorianCalendar().getTime();
		}
	}
}
