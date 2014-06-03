/*
 * @Util.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.util;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.test.test.util.layers.TestEntity;

/**
 * Provee utilidades para los test.
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
	 * Retorna un vector de clases a partir de una lista de los mismos.
	 * 
	 * @param elements
	 *            del tipo Class
	 * @return Class<?>[]
	 */
	public static Class<?>[] getAsClassArray(Class<?> ... elements) {

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
		for (Class<?> c : result) {
			LOG.info("Found class {} ", c.getSimpleName());
		}
		LOG.info("Found '{}' classes with relations with '{}'", result.size(),
				base.getSimpleName());
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
	 *            descripciones, la entidad n debe tener la descripción
	 */
	public static void checkOrder(List<TestEntity> list,
			String ... descriptions) {

		for (int i = 0; i < descriptions.length; i++) {
			assertEquals("Fail at index " + i, descriptions[i], list.get(i)
					.getDescription());
		}
	}

	/**
	 * Determina si dos colecciones son iguales.
	 * 
	 * <p>
	 * Este método es similar a {@link java.util.AbstractList#equals(Object)}
	 * solamente que realiza una comparación de longitud para ser más rapido.
	 * 
	 * </p>
	 * 
	 * @param l1
	 *            colección ejemplo
	 * @param l2
	 *            colección con la que comparar
	 * @return <code>true</code> sí y solamente sí, tienen el mismo tamaño,
	 *         ambas son <code>null</code>, o todos sus elementos son iguales
	 *         (de acuerdo al método equals).
	 */
	public static <T> boolean comparareCollections(Collection<T> l1,
			Collection<T> l2) {

		if ((l1 == null) && (l2 == null)) {
			return true;
		}
		if ((l1 == null) || (l2 == null)) {
			return false;
		}
		if (l1.size() != l2.size()) {
			return false;
		}
		Iterator<T> e1 = l1.iterator();
		Iterator<T> e2 = l2.iterator();
		while (e1.hasNext() && e2.hasNext()) {
			T o1 = e1.next();
			T o2 = e2.next();
			if (!(o1 == null ? o2 == null : o1.equals(o2))) {
				return false;
			}
		}
		return !(e1.hasNext() || e2.hasNext());
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

	/**
	 * Retorna el path de un recurso que existe, es hermano de la clase pasada o
	 * es un path absoluto.
	 * 
	 * <p>
	 * Si no existe se lanza una excepción.
	 * </p>
	 * 
	 * @param source
	 *            Clase de dondese invoca
	 * @param fileName
	 *            nombre del archivo
	 * @return ClassPath válido
	 * @throws KarakuRuntimeException
	 *             si no se encuentra el archivo
	 */
	public static String getSiblingResourceName(Class<?> source,
			String resourceName) {

		return getSiblingResource(source, resourceName).getPath();
	}

	/**
	 * Retorna un recurso, que se supone esta en la misma ubicación que la
	 * clase.
	 * 
	 * @param source
	 *            Clase de dondese invoca
	 * @param fileName
	 *            nombre del archivo
	 * @return ClassPath válido
	 * @throws KarakuRuntimeException
	 *             si no se encuentra el archivo
	 */
	public static ClassPathResource getSiblingResource(Class<?> source,
			String resourceName) {

		ClassPathResource cpr = new ClassPathResource(resourceName);
		if (cpr.exists()) {
			return cpr;
		}

		String realPath = source.getPackage().getName().replaceAll("\\.", "/");
		realPath += "/" + resourceName;
		cpr = new ClassPathResource(realPath);

		if (!cpr.exists()) {
			throw new KarakuRuntimeException("File with name " + resourceName
					+ " can not be found. Paths tried: Absolute:"
					+ resourceName + "; Relative: " + realPath);
		}
		return cpr;
	}
}
