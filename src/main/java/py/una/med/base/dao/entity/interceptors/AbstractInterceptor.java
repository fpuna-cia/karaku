/*
 * @BaseInterceptor.java 1.0 Oct 1, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.reflect.Field;

/**
 * Clase base para los EntityInterceptors en karaku.
 * 
 * <p>
 * Provee implementación por defecto de aquellos métodos que no se necesitan
 * implementar
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 * 
 */
public abstract class AbstractInterceptor implements Interceptor {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Class<?>[] getObservedTypes();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Class<?>[] getObservedAnnotations();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void intercept(final Field field, final Object bean);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean interceptable(final Field field, final Object bean) {

		return true;
	}
}
