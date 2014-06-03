/*
 * @Watcher.java 1.0 Nov 6, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dao.entity.watchers;

import py.una.pol.karaku.dao.entity.Operation;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 6, 2013
 *
 */
public interface Watcher<T> {

	/**
	 * Verifica si se debe redirijir una operación.
	 *
	 * <p>
	 * Tener extremo cuidado con relaciones circulares
	 * </p>
	 *
	 * @param operation
	 * @param bean
	 * @return
	 */
	Operation redirect(Operation operation, T bean);

	T process(Operation origin, Operation redirected, T bean);

	/**
	 * Define las clases (o superclases) que serán observadas.
	 *
	 * @return
	 */
	Class<T> getTargetClass();
}
