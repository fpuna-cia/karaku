/*
 * @DateXMLAdapter.java 1.0 Jan 10, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.services.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jan 10, 2014
 * 
 */
public class DateXMLAdapter extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date date) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return DatatypeConverter.printDate(cal);
	}

	@Override
	public Date unmarshal(String s) {

		return DatatypeConverter.parseDate(s).getTime();
	}

}
