/*
 * @BigDecimalInterceptor.java 1.0 Oct 3, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.dao.interceptors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.math.BigDecimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.interceptors.BigDecimalInterceptor;
import py.una.pol.karaku.dao.entity.interceptors.InterceptorHandler;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 3, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class BigDecimalInterceptorTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		InterceptorHandler interceptorHandler() {

			return new InterceptorHandler();
		}

		@Bean
		BigDecimalInterceptor caseSensitiveInterceptor() {

			return new BigDecimalInterceptor();
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
	public void testBigDecimal() {

		CaseSensitiveTest cst = new CaseSensitiveTest(BigDecimal.ONE);
		interceptorHandler.intercept(Operation.CREATE, cst);

		cst = new CaseSensitiveTest(new BigDecimal("12.01"));
		interceptorHandler.intercept(Operation.CREATE, cst);

		cst = new CaseSensitiveTest(new BigDecimal("0.01"));
		interceptorHandler.intercept(Operation.CREATE, cst);

		cst = new CaseSensitiveTest(new BigDecimal("0000000.01"));
		interceptorHandler.intercept(Operation.CREATE, cst);

		cst = new CaseSensitiveTest(new BigDecimal("100000000.01"));
		interceptorHandler.intercept(Operation.CREATE, cst);

		cst = new CaseSensitiveTest(new BigDecimal("-1.01"));
		interceptorHandler.intercept(Operation.CREATE, cst);
	}

	@Test(expected = KarakuRuntimeException.class)
	public void testExploit() {

		CaseSensitiveTest cst = new CaseSensitiveTest(new BigDecimal("0.001"));
		interceptorHandler.intercept(Operation.CREATE, cst);
	}

	class CaseSensitiveTest {

		private BigDecimal bd;

		/**
		 * @param noup
		 * @param up
		 */
		CaseSensitiveTest(BigDecimal decimal) {

			super();
			this.bd = decimal;
		}

		BigDecimal getDecimal() {

			return bd;
		}

	}
}
