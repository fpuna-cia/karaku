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
package py.una.pol.karaku.replication.client;

import static py.una.pol.karaku.util.Checker.notNull;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.util.KarakuReflectionUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@Component
public class ReplicationResponseHandler {

	private static final String[] ID_FIELDS = { "id", "lastId" };
	private static final String[] CHANGE_FIELDS = { "entities", "data" };

	/**
	 * Retorna un par inmutable que consta del ultimo identificador como llave,
	 * y la lista de cambios como valor.
	 * 
	 * @param t1
	 *            objeto retornado
	 * @return pair, nunca <code>null</code>, que consta de la llave la ultima
	 *         clave, y el valor la lista de cambios (no nula);
	 */
	public Pair<String, Collection<?>> getChanges(Object t1) {

		return new ImmutablePair<String, Collection<?>>(getLastId(t1),
				getItems(t1));
	}

	/**
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Collection getItems(Object response) {

		Object respuesta = notNull(response,
				"Cant get changes from null response");
		Class<?> clazz = notNull(respuesta.getClass());
		Field f = KarakuReflectionUtils.findField(clazz, CHANGE_FIELDS);

		if (f == null) {
			f = ReflectionUtils
					.findField(response.getClass(), null, List.class);
		}

		notNull(f, "Cant get the id field, "
				+ "use the @ReplicationData annotation or create "
				+ "a field with name %s, please see %s",
				Arrays.toString(CHANGE_FIELDS), response.getClass().getName());
		f.setAccessible(true);
		Collection c = (Collection) ReflectionUtils.getField(f, response);

		if (c == null) {
			return Collections.EMPTY_LIST;
		}
		return c;
	}

	/**
	 * @param response
	 * @return
	 */
	private String getLastId(Object response) {

		notNull(response, "Cant get id from null response");
		Class<?> clazz = notNull(response.getClass());

		Field f = KarakuReflectionUtils.findField(clazz, ID_FIELDS);

		notNull(f, "Cant get the id field, please use the @ReplicationId "
				+ "annotation, or create a field with name %s, see %s",
				Arrays.toString(ID_FIELDS), response.getClass());

		f.setAccessible(true);

		Object id = ReflectionUtils.getField(f, response);
		notNull(id, "Id null in response is not allowed");

		return id.toString();
	}
}
