/*
 * @InterceptorHandlerTest.java 1.0 Oct 14, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.dao.interceptors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.dao.entity.Operation;
import py.una.med.base.dao.entity.interceptors.AbstractInterceptor;
import py.una.med.base.dao.entity.interceptors.InterceptorHandler;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 14, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class InterceptorHandlerTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		InterceptorHandler interceptorHandler() {

			return new InterceptorHandler();
		}

		@Bean
		TestInterceptor testInterceptor() {

			return new TestInterceptor();
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
	public void testInterceptor() {

		String noUp = "nOuP";

		TestEntity cst = new TestEntity(noUp);
		cst.setTransientNoSerializable(noUp);
		cst.setTransientSerializable(noUp);
		cst.setInterceptable(noUp);

		cst.setTextChild("a");

		TestEntity.ESTATICA = noUp;

		this.interceptorHandler.intercept(Operation.CREATE, cst);

		assertEquals(cst.getTransientNoSerializable(), noUp);
		assertEquals(cst.getTransientSerializable(), noUp);
		assertEquals(cst.getEsFinal(), noUp);
		assertEquals(cst.getInterceptable(), "NOUP");
		assertEquals(cst.getTextChild(), "A");
		assertEquals(TestEntity.ESTATICA, noUp);

	}

	static class TestInterceptor extends AbstractInterceptor {

		@Override
		public Class<?>[] getObservedTypes() {

			return new Class<?>[] { String.class };
		}

		@Override
		public Class<?>[] getObservedAnnotations() {

			return new Class<?>[] { void.class };

		}

		@Override
		public void intercept(Operation op, Field f, Object bean) {

			assertEquals("interceptable", f.getName());

			ReflectionUtils.setField(f, bean,
					((String) ReflectionUtils.getField(f, bean)).toUpperCase());
		}

	}

	public static class TestEntity {

		private String interceptable;

		private transient String transientNoSerializable;

		@Transient
		private String transientSerializable;

		private final String esFinal;

		public static String ESTATICA;

		@OneToMany(cascade = CascadeType.ALL)
		private List<TestChild> childs = new ArrayList<InterceptorHandlerTest.TestChild>() {

			private static final long serialVersionUID = 1L;

			{
				add(new TestChild());
			}
		};

		/**
		 * @param noup
		 * @param up
		 */
		TestEntity(String esFinal) {

			super();
			this.esFinal = esFinal;
		}

		/**
		 * @return transientNoSerializable
		 */
		public String getTransientNoSerializable() {

			return transientNoSerializable;
		}

		/**
		 * @param transientNoSerializable
		 *            transientNoSerializable para setear
		 */
		public void setTransientNoSerializable(String transientNoSerializable) {

			this.transientNoSerializable = transientNoSerializable;
		}

		/**
		 * @return transientSerializable
		 */
		public String getTransientSerializable() {

			return transientSerializable;
		}

		/**
		 * @param transientSerializable
		 *            transientSerializable para setear
		 */
		public void setTransientSerializable(String transientSerializable) {

			this.transientSerializable = transientSerializable;
		}

		/**
		 * @return esFinal
		 */
		public String getEsFinal() {

			return esFinal;
		}

		/**
		 * @return interceptable
		 */
		public String getInterceptable() {

			return interceptable;
		}

		/**
		 * @param interceptable
		 *            interceptable para setear
		 */
		public void setInterceptable(String interceptable) {

			this.interceptable = interceptable;
		}

		public void setTextChild(String interceptable) {

			childs.get(0).setInterceptable(interceptable);
		}

		public String getTextChild() {

			return childs.get(0).getInterceptable();
		}

	}

	public static class TestChild {

		String interceptable;

		/**
		 * @param interceptable
		 *            interceptable para setear
		 */
		public void setInterceptable(String interceptable) {

			this.interceptable = interceptable;
		}

		/**
		 * @return interceptable
		 */
		public String getInterceptable() {

			return interceptable;
		}
	}
}
