/*
 * @JSFUtilTest.java 1.0 Sep 27, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.jsf;

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
import py.una.med.base.jsf.utils.JSFUtils;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

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
