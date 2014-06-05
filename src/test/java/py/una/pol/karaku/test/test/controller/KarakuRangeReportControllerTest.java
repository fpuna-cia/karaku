/*
 * @KarakuRangeReportControllerTest.java 1.0 Feb 11, 2014 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.pol.karaku.test.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Calendar;
import java.util.Date;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.component.UICalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.controller.reports.KarakuRangeReportController;
import py.una.pol.karaku.test.base.BaseControllerTest;
import py.una.pol.karaku.test.configuration.ControllerTestConfiguration;
import py.una.pol.karaku.test.test.util.layers.TestEntity;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 11, 2014
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class KarakuRangeReportControllerTest extends BaseControllerTest {

	@Configuration
	static class ContextConfiguration extends ControllerTestConfiguration {

		@Bean
		public TestKarakuRangeReportController testRangeReportController() {

			return new TestKarakuRangeReportController();
		}

	}

	@Autowired
	private TestKarakuRangeReportController controller;

	@Before
	public void setUpMessages() {

		getI18nHelper().addString("REPORT_MESSAGE_AFTER",
				"La fecha debe ser posterior a FECHA");
		getI18nHelper().addString("REPORT_MESSAGE_BEFORE",
				"La fecha debe ser anterior a FECHA");
	}

	@Test
	public void testOnChangeDateAfter() {

		controller.getFilterOptions().put(
				KarakuRangeReportController.DATE_BEFORE, getDate(1, 3, 2014));

		controller.onChangeDateAfter(getEvent(1, 2, 2014));
		assertEquals("La fecha debe ser posterior a 01-03-2014", getHelper()
				.getLastMessage().getDetail());

		getHelper().clearMessages();
		controller.onChangeDateAfter(getEvent(1, 4, 2014));
		assertNull(null, getHelper().getLastMessage());

	}

	@Test
	public void testOnChangeDateBefore() {

		controller.getFilterOptions().put(
				KarakuRangeReportController.DATE_AFTER, getDate(1, 3, 2014));

		controller.onChangeDateBefore(getEvent(1, 5, 2014));
		assertEquals("La fecha debe ser anterior a 01-03-2014", getHelper()
				.getLastMessage().getDetail());
		getHelper().clearMessages();

		controller.onChangeDateBefore(getEvent(1, 2, 2014));
		assertNull(null, getHelper().getLastMessage());

	}

	private Date getDate(int day, int month, int year) {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, day);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.YEAR, year);
		return c.getTime();
	}

	/**
	 * @return
	 */
	private AjaxBehaviorEvent getEvent(int day, int month, int year) {

		UICalendar calendar = new UICalendar();
		calendar.setValue(getDate(day, month, year));
		return new AjaxBehaviorEvent(calendar, new AjaxBehavior());
	}

	static class TestKarakuRangeReportController extends
			KarakuRangeReportController<TestEntity, Long> {

		@Override
		public void generateReport() {

			throw new NotImplementedException();
		}
	}

}
