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
package py.una.pol.karaku.test.test.dao.interceptors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import py.una.pol.karaku.dao.annotations.CaseSensitive;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.interceptors.CaseSensitiveInterceptor;
import py.una.pol.karaku.test.util.TestPropertiesUtil;
import py.una.pol.karaku.test.util.TestUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 * 
 */
public class CaseSensitiveInterceptorTest {

	private CaseSensitiveInterceptor interceptor;

	private TestPropertiesUtil propertiesUtil;

	@Before
	public void before() {

		propertiesUtil = new TestPropertiesUtil();

		interceptor = new CaseSensitiveInterceptor();
		interceptor.setProperties(propertiesUtil);
	}

	@Test
	public void testEnabled() {

		changeProperty("true");
		interceptor.postConstruct();
		assertArrayEquals(TestUtils.getAsClassArray(String.class),
				interceptor.getObservedTypes());

		assertArrayEquals(
				TestUtils.getAsClassArray(CaseSensitive.class, void.class),
				interceptor.getObservedAnnotations());

	}

	@Test
	public void testDisabled() throws Exception {

		changeProperty("false");
		interceptor.postConstruct();
		assertArrayEquals(TestUtils.getAsClassArray(),
				interceptor.getObservedTypes());

		assertArrayEquals(TestUtils.getAsClassArray(),
				interceptor.getObservedAnnotations());
	}

	@Test
	public void testCaseSensitive() throws Exception {

		changeProperty("true");

		String noUp = "nOuP";
		String up = "up";
		String upper = "UP";
		CaseSensitiveTest cst = new CaseSensitiveTest(noUp, up);
		Class<?> c = CaseSensitiveTest.class;

		Field sensible = getField(c, "sensible");
		Field insensible = getField(c, "insensible");
		Field insensibleNula = getField(c, "insensibleNula");
		Field sensibleHerencia = getField(c, "sensibleHerencia");
		Field insensibleHerencia = getField(c, "insensibleHerencia");

		assertFalse(interceptor.interceptable(Operation.CREATE, sensible, cst));
		assertTrue(interceptor.interceptable(Operation.CREATE, insensible, cst));
		assertFalse(interceptor
				.interceptable(Operation.DELETE, insensible, cst));
		assertTrue(interceptor.interceptable(Operation.CREATE,
				insensibleHerencia, cst));
		assertFalse(interceptor.interceptable(Operation.CREATE,
				sensibleHerencia, cst));

		interceptor.intercept(Operation.CREATE, insensible, cst);
		interceptor.intercept(Operation.CREATE, insensibleNula, cst);

		assertEquals(noUp, cst.getSensible());
		assertNotEquals(up, cst.getInsensible());
		assertEquals(upper, cst.getInsensible());

	}

	/**
	 * @param c
	 * @return
	 * @throws NoSuchFieldException
	 */
	private Field getField(Class<?> c, String fieldName)
			throws NoSuchFieldException {

		Field f = c.getDeclaredField(fieldName);
		f.setAccessible(true);
		return f;
	}

	class CaseSensitiveTest {

		@CaseSensitive
		private String sensible;

		private String insensible;

		private String insensibleNula;

		@SiSensitive
		private String sensibleHerencia;
		@NoSensistive
		private String insensibleHerencia;

		/**
		 * @param noup
		 * @param up
		 */
		CaseSensitiveTest(String noup, String up) {

			super();
			this.sensible = noup;
			this.insensible = up;
			this.sensibleHerencia = noup;
			this.insensibleHerencia = up;
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

		/**
		 * @return insensibleNula
		 */
		public String getInsensibleNula() {

			return insensibleNula;
		}

		/**
		 * @param insensibleNula
		 *            insensibleNula para setear
		 */
		public void setInsensibleNula(String insensibleNula) {

			this.insensibleNula = insensibleNula;
		}

	}

	@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@CaseSensitive
	@interface SiSensitive {

	}

	@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@interface NoSensistive {

	}

	/**
	 * 
	 */
	private void changeProperty(String property) {

		propertiesUtil.put("karaku.interceptor.caseSensitive", property);
	}
}
