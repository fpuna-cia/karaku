/*
 * @TestTimeInterceptor.java 1.0 Oct 1, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.dao.interceptors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.dao.annotations.CaseSensitive;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.interceptors.CaseSensitiveInterceptor;
import py.una.pol.karaku.dao.entity.interceptors.InterceptorHandler;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class CaseSensitiveInterceptorTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		InterceptorHandler interceptorHandler() {

			return new InterceptorHandler();
		}

		@Bean
		CaseSensitiveInterceptor caseSensitiveInterceptor() {

			return new CaseSensitiveInterceptor();
		}
	}

	@Autowired
	private transient InterceptorHandler interceptorHandler;

	@Test
	public void injection() {

		assertNotNull(this.interceptorHandler);
		assertThat(this.interceptorHandler.getInterceptorsCount(), is(1));
	}

	@Test
	public void testCaseSensitive() {

		String noUp = "nOuP";
		String up = "up";
		String upper = "UP";
		CaseSensitiveTest cst = new CaseSensitiveTest(noUp, up);

		this.interceptorHandler.intercept(Operation.CREATE, cst);

		assertEquals(cst.getSensible(), noUp);
		assertNotEquals(cst.getInsensible(), up);
		assertEquals(cst.getInsensible(), upper);

	}

	class CaseSensitiveTest {

		@CaseSensitive
		private String sensible;

		private String insensible;

		/**
		 * @param noup
		 * @param up
		 */
		CaseSensitiveTest(String noup, String up) {

			super();
			this.sensible = noup;
			this.insensible = up;
		}

		/**
		 * @return sensible
		 */
		String getSensible() {

			return this.sensible;
		}

		/**
		 * @return insensible
		 */
		String getInsensible() {

			return this.insensible;
		}

		public void setSensible(String sensible) {
			this.sensible = sensible;
		}

		public void setInsensible(String insensible) {
			this.insensible = insensible;
		}

	}
}
