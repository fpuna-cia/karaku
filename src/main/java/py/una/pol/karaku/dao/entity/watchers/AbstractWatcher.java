/*
 * @AbstractWatcher.java 1.0 Nov 6, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.dao.entity.watchers;

import java.lang.reflect.ParameterizedType;
import py.una.pol.karaku.dao.entity.Operation;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 6, 2013
 *
 */
public abstract class AbstractWatcher<T> implements Watcher<T> {

	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getTargetClass() {

		if (this.clazz == null) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			this.clazz = (Class<T>) type.getActualTypeArguments()[0];
		}

		return clazz;
	}

	@Override
	public Operation redirect(Operation operation, T bean) {

		return operation;
	}
}
