/*
 * @I18nHelperTest.java 1.0 Apr 30, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.i18n;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.lang.annotation.Annotation;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.model.DisplayName;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.test.util.TestI18nHelper;
import py.una.med.base.util.I18nHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Apr 30, 2014
 * 
 */
@Ignore
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class I18nHelperTest extends BaseTest {

	private static final String PM1 = "param 1";
	private static final String PM2 = "param 2";
	private static final String SM2 = "simple 2";
	private static final String SM1 = "simple 1";

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	@Autowired
	private TestI18nHelper h;

	@Before
	public void before() {

		h.addString(SM1, "key 1");
		h.addString(SM2, "key 2");
		h.addString(PM1, "key {}");
		h.addString(PM2, "key {} is {}");
	}

	@Test
	public void test() throws Exception {

		assertEquals("key 1", h.getString(SM1));
		assertEquals("key 2", h.getString(SM2));
		// omite parametros innecsarios
		assertEquals("key 2", h.getString(SM2, "AA"));

		// ambos internacionalizados
		assertEquals("key 2", h.getString(PM1, 2));

		// solo el primero se internacionaliza
		assertEquals("key se traduce como llave",
				h.getString(PM1, "se traduce como llave"));

		assertEquals("key 2 is 123", h.getString(PM2, 2, 123));
		assertEquals("key clave is valor", h.getString(PM2, "clave", "valor"));

	}

	@Test
	public void testNotFound() throws Exception {

		assertNotEquals(h.getString("NOO"), "NOO");
	}

	@Test
	public void testConvertStrings() throws Exception {

		List<String> result = h.convertStrings(SM1, SM2);
		assertEquals("key 1", result.get(0));
		assertEquals("key 2", result.get(1));

		assertEquals("key 1", h.convertStrings(SM1).get(0));
	}

	@Test
	public void testWeakReference() throws Exception {

		assertEquals(I18nHelper.getMessage(SM1), "key 1");
	}

	@Test
	public void testGetName() throws Exception {

		assertEquals("key 1", I18nHelper.getName(getDN("{simple 1}")));
		assertEquals("key 1", I18nHelper.getName(getDN("simple 1")));
		assertNotEquals("key 1", I18nHelper.getName(getDN("simple 1}")));
		assertNotEquals("key 1", I18nHelper.getName(getDN("{simple 1")));
		assertEquals("", I18nHelper.getName(getDN("")));
		assertEquals("", I18nHelper.getName(null));
	}

	@Test
	public void testComprae() throws Exception {

		assertTrue(h.compare("simple 1", "key 1"));
		assertFalse(h.compare("simple 1", "key 2"));
	}

	/**
	 * @return
	 */
	private DisplayName getDN(final String key) {

		return new DisplayName() {

			@Override
			public Class<? extends Annotation> annotationType() {

				return DisplayName.class;
			}

			@Override
			public String path() {

				return "test";
			}

			@Override
			public String key() {

				return key;
			}
		};
	}

}
