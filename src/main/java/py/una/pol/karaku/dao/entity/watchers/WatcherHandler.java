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
package py.una.pol.karaku.dao.entity.watchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.entity.Operation;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 6, 2013
 * 
 */
@Component
public class WatcherHandler {

	@Autowired(required = false)
	private List<Watcher<?>> watchers;

	private Map<Class<?>, List<Watcher<?>>> maps;

	@PostConstruct
	void init() {

		maps = new HashMap<Class<?>, List<Watcher<?>>>();

		if (watchers == null) {
			return;
		}
		for (Watcher<?> w : watchers) {
			addOrCreateAndAdd(w, w.getTargetClass());
		}
	}

	private void addOrCreateAndAdd(Watcher<?> w, Class<?> c) {

		if (maps.get(c) == null) {
			maps.put(c, new ArrayList<Watcher<?>>());
		}
		maps.get(c).add(w);
	}

	/**
	 * @return
	 */
	public List<Watcher<?>> getWatchers() {

		return watchers;
	}

	/**
	 * @param operation
	 * @param dc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public <T> Operation redirect(@Nonnull Operation operation,
			@Nonnull Class<T> clazz, @Nonnull T dc) {

		Set<Watcher<?>> clazzwatchers = getWatchers(clazz);

		for (Watcher<?> watcher : clazzwatchers) {
			Watcher<T> wath = (Watcher<T>) watcher;
			Operation nOp = wath.redirect(operation, dc);
			if (!nOp.equals(operation)) {
				return redirect(nOp, clazz, dc);
			}
		}
		return operation;
	}

	/**
	 * @param operation
	 * @param dc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T process(Operation origin, Operation operation, Class<T> clazz,
			T dc) {

		Set<Watcher<?>> currentWatchers = getWatchers(clazz);

		T bean = dc;
		for (Watcher<?> watcher : currentWatchers) {
			Watcher<T> wath = (Watcher<T>) watcher;
			bean = wath.process(origin, operation, bean);
		}
		return bean;
	}

	/**
	 * @param clazz
	 * @return
	 */
	private <T> Set<Watcher<?>> getWatchers(Class<T> clazz) {

		Set<Watcher<?>> currentWatchers = new HashSet<Watcher<?>>();
		for (Entry<Class<?>, List<Watcher<?>>> entry : maps.entrySet()) {

			Class<?> oClazz = entry.getKey();
			if (oClazz.isAssignableFrom(clazz)) {
				currentWatchers.addAll(entry.getValue());
			}
		}
		return currentWatchers;
	}
}
