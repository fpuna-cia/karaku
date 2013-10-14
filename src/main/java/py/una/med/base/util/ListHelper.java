/**
 * @ListHelper 1.0 05/03/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 
 * 
 * @author Arturo Volpe,Nathalia Ochoa
 * @since 1.0
 * @version 1.0 08/03/2013
 * 
 */
public final class ListHelper {

	private ListHelper() {

		// No-op
	}

	/**
	 * Retorna un ArrayList de items, es lo mismo que
	 * {@link Arrays#asList(Object...)} solo que retorna un ArrayList, al cual
	 * se le pueden agregar cosas, al contrario que asList que retorna un
	 * AbstractList que no puede ser modificado.
	 * 
	 * @param items
	 *            a ser agregados a la lista, no puede ser nulo
	 * @return {@link ArrayList} conteniendo los elementos no nulos pasados.
	 */
	public static <T> List<T> getAsList(T ... items) {

		ArrayList<T> aRet = new ArrayList<T>(items.length);
		for (T item : items) {
			if (item != null) {
				aRet.add(item);
			}
		}
		return aRet;
	}

	/**
	 * Este metodo convierte una colleccion de elementos en un vector.
	 * 
	 * @param collection
	 * @return vector de elementos
	 */

	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Class<T> clazz, Collection<T> collection) {

		if (collection.isEmpty()) {
			return null;
		}
		T[] result = (T[]) Array.newInstance(clazz, collection.size());
		return collection.toArray(result);

	}

	/**
	 * Este metodo convierte una colleccion de elementos en un vector. Ademas
	 * supone que todos los elementos son del tipo T.
	 * 
	 * @param collection
	 * @return vector de elementos
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Collection<T> collection) {

		if (collection.isEmpty()) {
			return null;
		}
		Class<T> clazz = (Class<T>) collection.iterator().next().getClass();
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
	public boolean hasElements(Collection<?> collection) {

		return !(collection == null || collection.isEmpty());
	}
}
