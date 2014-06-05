/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
