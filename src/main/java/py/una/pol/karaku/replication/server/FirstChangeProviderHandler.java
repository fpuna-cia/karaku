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
package py.una.pol.karaku.replication.server;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.una.pol.karaku.util.StringUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 4, 2013
 * 
 */
@Service
public class FirstChangeProviderHandler {

	@Autowired
	private List<FirstChangeProvider<?>> providers;

	@PostConstruct
	@SuppressWarnings("rawtypes")
	void sort() {

		Collections.sort(providers, new Comparator<FirstChangeProvider>() {

			@Override
			public int compare(FirstChangeProvider arg0,
					FirstChangeProvider arg1) {

				return arg1.getPriority().compareTo(arg0.getPriority());
			}
		});
	}

	/**
	 * Retorna el proveedor de cambios para la clase definida.
	 * 
	 * @return {@link FirstChangeProvider} encargado de proveer las entidades
	 *         del cambio.
	 * @throws IllegalStateException
	 *             si no se encuentra un cambio
	 */
	public FirstChangeProvider<?> getChangeProvider(Class<?> clazz) {

		for (FirstChangeProvider<?> fcp : providers) {
			if (fcp.getSupportedClass().isAssignableFrom(clazz)) {
				return fcp;
			}
		}
		throw new IllegalStateException(
				"Can't find first change provider for class: "
						+ clazz.getName());
	}

	/**
	 * Retorna todas las entidades con un identificador {@link Bundle#ZERO_ID}.
	 * 
	 * @param clazz
	 *            clase a verificar
	 * @return cambios
	 */
	@Nonnull
	public <T> Bundle<T> getAll(@Nonnull Class<?> clazz) {

		return getAll(clazz, Bundle.ZERO_ID);
	}

	/**
	 * Retorna el grupo de cambios con ultima modificacion el parámetro pasado.
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Nonnull
	public <T> Bundle<T> getAll(@Nonnull Class<?> clazz, @Nonnull String id) {

		notNull(clazz, "Can't get bundle of null class");
		String currentId = id;
		if (StringUtils.isInvalid(id)) {
			currentId = Bundle.ZERO_ID;
		}
		FirstChangeProvider fcp = getChangeProvider(clazz);
		Collection s = fcp.getChanges(clazz);
		Bundle<T> bundle = new Bundle<T>();
		for (Object o : s) {
			if (o == null) {
				continue;
			}
			bundle.add((T) o, currentId);
		}
		return bundle;
	}
}
