/*
 * @TimeInterceptor.java 1.0 Oct 1, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.dao.annotations.CaseSensitive;
import py.una.med.base.dao.entity.Operation;

/**
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 *
 */
@Component
public class CaseSensitiveInterceptor extends AbstractInterceptor {

	@Override
	public Class<?>[] getObservedTypes() {

		return new Class<?>[] { String.class };
	}

	@Override
	public Class<?>[] getObservedAnnotations() {

		return new Class<?>[] { CaseSensitive.class, void.class };

	}

	@Override
	public boolean interceptable(Operation op, Field f, Object bean) {

		boolean interceptable = (op != Operation.DELETE)
				&& (f.getAnnotation(CaseSensitive.class) == null);
		for (Annotation a : f.getAnnotations()) {
			if (a.annotationType().isAnnotationPresent(CaseSensitive.class)) {
				interceptable = false;
			}
		}

		return interceptable;
	}

	@Override
	public void intercept(Operation op, Field f, Object bean) {

		Object o = ReflectionUtils.getField(f, bean);
		String s = (String) o;
		if (s != null) {
			s = s.toUpperCase();
			ReflectionUtils.setField(f, bean, s);
		}
	}

}
