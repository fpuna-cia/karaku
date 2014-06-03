/*
 * @UriTestInterceptor.java 1.0 Nov 5, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.dao.interceptors;

import static org.junit.Assert.assertEquals;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.dao.annotations.CaseSensitive;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.annotations.URI;
import py.una.pol.karaku.dao.entity.annotations.URI.Type;
import py.una.pol.karaku.dao.entity.interceptors.InterceptorHandler;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.test.util.transaction.SQLFiles;
import py.una.pol.karaku.test.util.transaction.Sequences;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Sequences("sq1")
@SQLFiles(SQLFiles.NONE)
public class UriTestInterceptor extends BaseTestWithDatabase {

	@Configuration
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(TestClass.class);
		}

	}

	@Autowired
	private InterceptorHandler interceptorHandler;

	@Test
	public void testIntercept() throws Exception {

		TestClass tc = new TestClass();
		tc.unique = 1L;
		tc.otherUnique = "guasa bi";

		interceptorHandler.intercept(Operation.CREATE, tc);

		assertEquals("tt/1", tc.uri1);
		assertEquals("tt/1", tc.uri2);
		assertEquals("tt/guasa_bi", tc.uri3);

		tc.unique = 100L;
		tc.otherUnique = "GUASA BI 234";

		interceptorHandler.intercept(Operation.CREATE, tc);
		assertEquals("tt/100", tc.uri1);
		assertEquals("tt/2", tc.uri2);
		assertEquals("tt/GUASA_BI_234", tc.uri3);

		interceptorHandler.intercept(Operation.UPDATE, tc);
		assertEquals("tt/2", tc.uri2);

		interceptorHandler.intercept(Operation.DELETE, tc);
		assertEquals("tt/2", tc.uri2);
	}

	@Entity
	static class TestClass {

		@Id
		Long unique;

		/**
		 * El orden de ejecución es el mismo en el que aparecen, por ello si
		 * otherUnique no esta con CaseSensitive, las URI se generarán con la
		 * versión en mayúsculas.
		 */
		@CaseSensitive
		String otherUnique;

		@URI(baseUri = "tt/", type = Type.FIELD, field = "unique")
		String uri1;

		@URI(baseUri = "tt/", type = Type.SEQUENCE, sequenceName = "sq1")
		String uri2;

		@URI(baseUri = "tt/", type = Type.FIELD, field = "otherUnique")
		String uri3;
	}

}
