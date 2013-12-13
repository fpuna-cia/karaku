/*
 * @WatcherHandler.java 1.0 Nov 6, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.entity.watchers;

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
import py.una.med.base.dao.entity.Operation;

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
	public <T> Operation redirect(@Nonnull Operation operation, Class<T> clazz,
			T dc) {

		Set<Watcher<?>> watchers = getWatchers(clazz);

		for (Watcher<?> watcher : watchers) {
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
