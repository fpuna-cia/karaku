/*
 * @TestTimeInterceptor.java 1.0 Oct 1, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.dao.interceptors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.dao.entity.annotations.Time;
import py.una.med.base.dao.entity.annotations.Time.Type;
import py.una.med.base.dao.entity.interceptors.InterceptorHandler;
import py.una.med.base.dao.entity.interceptors.TimeInterceptor;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

/**
 * The Class TimeInterceptorTest.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class TimeInterceptorTest extends BaseTest {

	/**
	 * The Class ContextConfiguration.
	 */
	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		/**
		 * Interceptor handler.
		 * 
		 * @return the interceptor handler
		 */
		@Bean
		InterceptorHandler interceptorHandler() {

			return new InterceptorHandler();
		}

		/**
		 * Time interceptor.
		 * 
		 * @return the time interceptor
		 */
		@Bean
		TimeInterceptor timeInterceptor() {

			return new TimeInterceptor();
		}
	}

	/** The interceptor handler. */
	@Autowired
	private transient InterceptorHandler interceptorHandler;

	/**
	 * Injection.
	 */
	@Test
	public void injection() {

		assertNotNull(this.interceptorHandler);
		assertThat(this.interceptorHandler.getInterceptorsCount(), is(1));
	}

	/**
	 * Test time.
	 */
	@Test
	public void testTime() {

		Date d = new Date();
		Date d2 = new Date(d.getTime());

		TimeTest tt = new TimeTest(d, d, d);

		this.interceptorHandler.intercept(tt);

		Calendar c = Calendar.getInstance();
		c.setTime(tt.getTime());

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		assertThat(c.get(Calendar.YEAR), is(1970));
		assertThat(c.get(Calendar.MONTH), is(0));
		assertThat(c.get(Calendar.DAY_OF_MONTH), is(1));

		assertThat(c.get(Calendar.HOUR_OF_DAY),
				is(c2.get(Calendar.HOUR_OF_DAY)));
		assertThat(c.get(Calendar.MINUTE), is(c2.get(Calendar.MINUTE)));

		assertThat(c.get(Calendar.SECOND), is(0));
		assertThat(c.get(Calendar.MILLISECOND), is(0));

	}

	/**
	 * Test date time.
	 */
	@Test
	public void testDateTime() {

		Date d = new Date();
		Date d2 = new Date(d.getTime());

		TimeTest tt = new TimeTest(d, d, d);

		this.interceptorHandler.intercept(tt);

		Calendar c = Calendar.getInstance();
		c.setTime(tt.getDateTime());

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		assertThat(c.get(Calendar.YEAR), is(c2.get(Calendar.YEAR)));
		assertThat(c.get(Calendar.MONTH), is(c2.get(Calendar.MONTH)));
		assertThat(c.get(Calendar.DAY_OF_MONTH),
				is(c2.get(Calendar.DAY_OF_MONTH)));
		assertThat(c.get(Calendar.HOUR_OF_DAY),
				is(c2.get(Calendar.HOUR_OF_DAY)));
		assertThat(c.get(Calendar.MINUTE), is(c2.get(Calendar.MINUTE)));

		assertThat(c.get(Calendar.SECOND), is(0));
		assertThat(c.get(Calendar.MILLISECOND), is(0));

	}

	/**
	 * Test date.
	 */
	@Test
	public void testDate() {

		Date d = new Date();
		Date d2 = new Date(d.getTime());

		TimeTest tt = new TimeTest(d, d, d);

		this.interceptorHandler.intercept(tt);

		Calendar c = Calendar.getInstance();
		c.setTime(tt.getDate());

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		assertThat(c.get(Calendar.YEAR), is(c2.get(Calendar.YEAR)));
		assertThat(c.get(Calendar.MONTH), is(c2.get(Calendar.MONTH)));
		assertThat(c.get(Calendar.DAY_OF_MONTH),
				is(c2.get(Calendar.DAY_OF_MONTH)));
		assertThat(c.get(Calendar.HOUR_OF_DAY), is(0));
		assertThat(c.get(Calendar.MINUTE), is(0));

		assertThat(c.get(Calendar.SECOND), is(0));
		assertThat(c.get(Calendar.MILLISECOND), is(0));

	}

	/**
	 * The Class TimeTest.
	 */
	static class TimeTest {

		/** The time. */
		@Time(type = Type.TIME)
		Date time;

		/** The date time. */
		@Time(type = Type.DATETIME)
		Date dateTime;

		/** The date. */
		@Time
		Date date;

		/**
		 * Instantiates a new time test.
		 * 
		 * @param time
		 *            the time
		 * @param dateTime
		 *            the date time
		 * @param date
		 *            the date
		 */
		public TimeTest(Date time, Date dateTime, Date date) {

			super();
			this.time = (Date) time.clone();
			this.dateTime = (Date) dateTime.clone();
			this.date = (Date) date.clone();
		}

		/**
		 * Gets the time.
		 * 
		 * @return time
		 */
		public Date getTime() {

			return this.time;
		}

		/**
		 * Gets the date.
		 * 
		 * @return date
		 */
		public Date getDate() {

			return this.date;
		}

		/**
		 * Gets the date time.
		 * 
		 * @return dateTime
		 */
		public Date getDateTime() {

			return this.dateTime;
		}

	}
}
