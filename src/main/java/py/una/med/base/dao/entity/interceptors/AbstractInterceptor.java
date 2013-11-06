/*
 * @BaseInterceptor.java 1.0 Oct 1, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.dao.entity.Operation;

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

	@Override
	public Class<?>[] getObservedAnnotations() {

		return new Class<?>[] { void.class };
	}

	@Override
	public java.lang.Class<?>[] getObservedTypes() {

		return new Class<?>[] { void.class };
	};

	@Override
	public boolean interceptable(Operation op, Field field, Object bean) {

		return true;
	}

	/**
	 * Retorna el valor de un field de un bean dado.
	 *
	 * <p>
	 * Es útil cuando se necesita obtener valores de otros fields del bean que
	 * se esta interceptando.
	 * </p>
	 *
	 * @param bean
	 *            del cual quitar el valor
	 * @param field
	 *            nombre del atributo
	 * @return valor extraído
	 */
	protected Object getFieldValueOfBean(Object bean, String field) {

		Field unique = ReflectionUtils.findField(bean.getClass(), field);
		unique.setAccessible(true);
		Object uniqueColumn = ReflectionUtils.getField(unique, bean);
		unique.setAccessible(false);
		return uniqueColumn;
	}
}
