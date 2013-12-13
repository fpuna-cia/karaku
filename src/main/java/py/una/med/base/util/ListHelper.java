/**
 * @ListHelper 1.0 05/03/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Clase utilitaria para manipulación de Listas.
 * 
 * @author Arturo Volpe
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 08/03/2013
 * 
 */
public final class ListHelper {

	private ListHelper() {

		// No-op
	}

	/**
	 * Genera un {@link ArrayList} a partir de una serie de elementos.
	 * <p>
	 * Retorna un ArrayList de items, es lo mismo que
	 * {@link java.util.Arrays#asList(Object...)} solo que retorna un ArrayList,
	 * al cual se le pueden agregar cosas, al contrario que asList que retorna
	 * un AbstractList que no puede ser modificado.
	 * </p>
	 * 
	 * @param items
	 *            array no nulo de elementos
	 * @return {@link ArrayList} conteniendo los elementos no nulos pasados.
	 */
	public static <T> List<T> getAsList(@NotNull T ... items) {

		ArrayList<T> aRet = new ArrayList<T>(items.length);
		for (T item : items) {
			if (item != null) {
				aRet.add(item);
			}
		}
		return aRet;
	}

	/**
	 * Este método convierte una colección de elementos en un vector.
	 * 
	 * @param collection
	 *            colección a convertir
	 * @param clazz
	 *            clase de la colección
	 * @return vector de elementos
	 */

	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Class<T> clazz, Collection<T> collection) {

		if (!hasElements(collection)) {
			return (T[]) Array.newInstance(clazz, 0);
		}
		T[] result = (T[]) Array.newInstance(clazz, collection.size());
		return collection.toArray(result);

	}

	/**
	 * Este método convierte una colección de elementos en un vector.
	 * 
	 * <p>
	 * Se supone que todos los elementos son del tipo T, si no todos son del
	 * mismo tipo, entonces utilizar el método
	 * {@link #asArray(Class, Collection)}.
	 * </p>
	 * <p>
	 * Este método infiere el tipo según cual sea su primer elemento.
	 * </p>
	 * 
	 * @param collection
	 * @return vector de elementos
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Collection<T> collection) {

		Class<T> clazz = (Class<T>) collection.iterator().next().getClass();
		if (!hasElements(collection)) {
			return (T[]) Array.newInstance(clazz, 0);
		}
		T[] result = (T[]) Array.newInstance(clazz, collection.size());
		return collection.toArray(result);

	}

	/**
	 * Define si una colección tiene o no elementos.
	 * <p>
	 * Este método es null-safe, es decir hasElements(null) retorna
	 * <code>false</code>
	 * </p>
	 * 
	 * @param collection
	 *            lista de elementos, puede ser <code>null</code>
	 * @return <code>true</code> si la lista tiene elementos, <code>false</code>
	 *         en otro caso
	 */
	public static boolean hasElements(Collection<?> collection) {

		return !((collection == null) || collection.isEmpty());
	}
}
