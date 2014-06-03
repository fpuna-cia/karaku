/*
 * @ValidationMessagesTest.java 1.0 Oct 17, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test;

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
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import py.una.pol.karaku.util.ValidationMessages;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 * 
 */
public class ValidationMessagesTest {

	static HashSet<String> keys;

	@BeforeClass
	public static void before() {

		Locale locale = new Locale("es", "PY");
		ResourceBundle toAdd = ResourceBundle.getBundle(
				"language.validation.karaku", locale);
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
