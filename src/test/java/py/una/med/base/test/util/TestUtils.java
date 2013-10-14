/*
 * @Util.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.util;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.Entity;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import py.una.med.base.test.test.util.layers.TestEntity;

/**
 * Provee utilidades para los test
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
public class TestUtils {

	private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

	/**
	 * Retorna un vector de elementos a partir de una lista de los mismos.
	 * 
	 * @param elements
	 *            de clase T
	 * @return T[]
	 */
	public static <T> T[] getAsArray(T ... elements) {

		return elements;
	}

	/**
	 * Retorna la lista de entidades relacionadas a una base.
	 * 
	 * @param base
	 *            entidad base
	 * @return array con todas las entidades que se pueden acceder desde base.
	 */
	public static Class<?>[] getReferencedClasses(Class<?> base) {

		LOG.info("Scanning for classes with relations with '{}'",
				base.getName());
		Set<Class<?>> result = getReferencedClasses(base,
				new HashSet<Class<?>>(10));
		result.add(base);
		LOG.info("Found '{}' classes with relations with '{}'", result.size(),
				base.getName());
		return result.toArray(new Class<?>[result.size()]);
	}

	/**
	 * Verificación de orden de una lista.
	 * 
	 * Determinan si una lista tiene las descripciones pasadas en el orden
	 * adecuado.
	 * 
	 * @param list
	 *            lista de entidades
	 * @param descriptions
	 *            descripciones, la entidad n debe tener la descripción n
	 */
	public static void checkOrder(List<TestEntity> list, String ... descriptions) {

		for (int i = 0; i < descriptions.length; i++) {
			assertEquals("Fail at index " + i, descriptions[i], list.get(i).getDescription());
		}
	}

	/**
	 * Retorna la lista de entidades relacionadas a una base.
	 * 
	 * @param base
	 *            entidad base
	 * @return array con todas las entidades que se pueden acceder desde base.
	 */
	public static Class<?>[] getReferencedClasses(Class<?> ... base) {

		LOG.info("Scanning for classes with relations with '{}'", base);
		Set<Class<?>> result = new HashSet<Class<?>>(10);
		for (Class<?> clasz : base) {
			result.add(clasz);
			getReferencedClasses(clasz, result);
		}
		LOG.info("Found '{}' classes with relations with '{}'", result.size(),
				base);
		return result.toArray(new Class<?>[result.size()]);
	}

	private static Set<Class<?>> getReferencedClasses(Class<?> base,
			final Set<Class<?>> elements) {

		ReflectionUtils.doWithFields(base, new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {

				if (elements.add(getType(field))) {
					getReferencedClasses(getType(field), elements);
				}
			}
		}, new FieldFilter() {

			@Override
			public boolean matches(Field field) {

				return AnnotationUtils.findAnnotation(getType(field),
						Entity.class) != null;
			}
		});

		return elements;
	}

	private static Class<?> getType(Field field) {

		Class<?> toRet = field.getType();
		// Si tenemos una lista, buscar el tipo de la lista.
		if (Collection.class.isAssignableFrom(toRet)) {
			toRet = GenericCollectionTypeResolver.getCollectionFieldType(field);
		}
		return toRet;
	}
}
