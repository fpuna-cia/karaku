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

package py.una.pol.karaku.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
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
	@Nonnull
	public static <T> List<T> getAsList(@NotNull T ... items) {

		List<T> aRet = new ArrayList<T>(items.length);
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

		return !(collection == null || collection.isEmpty());
	}

	/**
	 * Verifica si un array tiene un determinado elemento.
	 * 
	 * <p>
	 * Para ello realiza un recorrido lineal sobre el array y compara cada
	 * elemento utilizadno {@link Object#equals(Object)}
	 * </p>
	 * 
	 * @param array
	 *            vector de elementos
	 * @param v
	 *            elemento a verificar
	 * @return <code>true</code> si lo contiene, <code>false</code> en caso
	 *         contrario.
	 */
	public static <T> boolean contains(@Nonnull final T[] array, final T v) {

		for (final T e : array) {
			if (e == v || v != null && v.equals(e)) {
				return true;
			}
		}

		return false;
	}
}
