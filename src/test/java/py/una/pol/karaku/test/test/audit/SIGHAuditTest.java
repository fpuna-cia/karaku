/*
 * @SIGHAuditTest.java 1.0 04/04/2014 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.test.test.audit;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.jar.Manifest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.audit.Audit;
import py.una.pol.karaku.audit.SIGHAudit;
import py.una.pol.karaku.business.AuditLogic;
import py.una.pol.karaku.business.IAuditLogic;
import py.una.pol.karaku.domain.AuditTrail;
import py.una.pol.karaku.domain.AuditTrailDetail;
import py.una.pol.karaku.repo.AuditTrailDao;
import py.una.pol.karaku.repo.AuditTrailDetailDao;
import py.una.pol.karaku.repo.IAuditTrailDao;
import py.una.pol.karaku.repo.IAuditTrailDetailDao;
import py.una.pol.karaku.security.AuthorityController;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.test.util.transaction.SQLFiles;
import py.una.pol.karaku.util.Util;

/**
 * 
 * @author Romina Fernandez
 * @since 1.0
 * @version 1.0 04/04/2014
 * 
 */
@SQLFiles(SQLFiles.NONE)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SIGHAuditTest extends BaseTestWithDatabase {

	@Configuration
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(AuditTrail.class);
		}

		@Bean
		SIGHAudit audit() {

			return new SIGHAudit();
		}

		@Bean
		IAuditLogic auditLogic() {

			return new AuditLogic();
		}

		@Bean
		IAuditTrailDao auditDAO() {

			return new AuditTrailDao();
		}

		@Bean
		IAuditTrailDetailDao auditDDAO() {

			return new AuditTrailDetailDao();
		}

		@Bean
		Example tembolo() {

			return new Example();
		}

		@Bean
		Util util() {

			return new Util() {

				@Override
				public String getIpAdress() {

					return "IP";
				}
			};
		}

		@Bean
		AuthorityController authorityController() {

			return new AuthorityController() {

				@Override
				public String getUsername() {

					return "ROOT";
				}
			};
		}

		@Bean
		Manifest manifest() {

			return new Manifest();
		}
	}

	@Autowired
	Example example;

	@Autowired
	SIGHAudit audit;

	@Autowired
	AuditLogic auditLogic;

	@Autowired
	AuditTrailDetailDao detailDao;

	@Test
	public void testAudit() throws Exception {

		Attr attr = new Attr("algo");

		// DADO
		example.setAttr1(attr);
		Audit a = getAuditAnnotation(gS("attr1.param1"), gS("{0}"));

		// cuando
		audit.doAudit(a, "METODO", example, gS("a", "b"));

		// entonces

		AuditTrail at = auditLogic.getAll(null).get(0);

		assertEquals("METODO", at.getMethodSignature());
		AuditTrailDetail atd1 = detailDao.getAll(null).get(0);
		// AuditTrailDetail atd2 = detailDao.getAll(null).get(1);
		assertEquals(attr.getParam1(), atd1.getValue());
		// assertEquals("a", atd2.getValue());

		Attr pepito = new Attr("hola");
		Attr pepito2 = new Attr(new String("hola").intern());
		assertEquals(pepito, pepito2);

	}

	private String[] gS(String ... strings) {

		return strings;
	}

	private Audit getAuditAnnotation(final String[] toAudit,
			final String[] params) {

		return new Audit() {

			@Override
			public Class<? extends Annotation> annotationType() {

				return Audit.class;
			}

			@Override
			public String[] toAudit() {

				return toAudit;
			}

			@Override
			public String[] paramsToAudit() {

				return params;
			}
		};
	}

	public static class Attr implements Serializable {

		private static final long serialVersionUID = 1L;
		String param1;

		public Attr(String value) {

			param1 = value;
		}

		@Override
		public boolean equals(Object obj) {

			Attr attr = (Attr) obj;
			// return attr.param1.equals(this.param1);
			return attr.param1 == (this.param1);
		}

		public String getParam1() {

			return param1;
		}

		@Override
		public String toString() {

			return param1;
		}
	}

	public static class Example {

		Attr attr1;

		/**
		 * @return attr1
		 */
		public Attr getAttr1() {

			return attr1;
		}

		/**
		 * @param attr1
		 *            attr1 para setear
		 */
		public void setAttr1(Attr attr1) {

			this.attr1 = attr1;
		}

		String cadena;

		public String importantMethod(String param1, String param2) {

			return param1;
		}

		public String getCadena() {

			return cadena;
		}

		public void setCadena(String cadena) {

			this.cadena = cadena;
		}
	}

}
