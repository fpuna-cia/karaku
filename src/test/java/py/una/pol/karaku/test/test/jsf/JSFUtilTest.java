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
package py.una.pol.karaku.test.test.jsf;

import static org.junit.Assert.assertEquals;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.jsf.utils.JSFUtils;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 27, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class JSFUtilTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		JSFUtils jsfUtils() {

			return new JSFUtils();
		}
	}

	@Autowired
	JSFUtils jsfUtils;

	@Test
	public void parseDate() throws ParseException {

		Date test = new SimpleDateFormat().parse("11/22/11 00:00 AM");
		Date date = new SimpleDateFormat().parse("11/22/11 00:00 AM");
		Date jsf = this.jsfUtils.asDate(new SimpleDateFormat(
				JSFUtils.CALENDAR_DATE_PATTERN).format(test));
		assertEquals(date, jsf);
	}

	@Test(expected = ParseException.class)
	public void parseWrongDate() throws ParseException {

		this.jsfUtils.asDate("Tue Nov 22 00:00:00 XXX 2011");
	}
}
