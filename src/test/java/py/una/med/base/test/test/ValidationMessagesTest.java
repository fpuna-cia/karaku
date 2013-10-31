/*
 * @ValidationMessagesTest.java 1.0 Oct 17, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;
import py.una.med.base.util.ValidationMessages;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ValidationMessagesTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

	}

	static HashSet<String> keys;

	@BeforeClass
	public static void before() {

		Locale locale = new Locale("es", "PY");
		ResourceBundle toAdd = ResourceBundle.getBundle(
				"language.validation.base", locale);
		Enumeration<String> en = toAdd.getKeys();
		keys = new HashSet<String>();
		while (en.hasMoreElements()) {

			keys.add("{" + en.nextElement() + "}");

		}

	}

	@Test
	public void testConstants() {

		ReflectionUtils.doWithFields(ValidationMessages.class,
				new FieldCallback() {

					@Override
					public void doWith(Field field)
							throws IllegalArgumentException,
							IllegalAccessException {

						String value = (String) field.get(null);
						assertNotNull(value);
						assertTrue("Key: " + value + " not found",
								keys.contains(value));
					}
				}, new FieldFilter() {

					@Override
					public boolean matches(Field field) {

						int modifiers = field.getModifiers();
						return Modifier.isStatic(modifiers)
								&& !Modifier.isTransient(modifiers)
								&& Modifier.isPublic(modifiers);
					}
				});
	}
}
