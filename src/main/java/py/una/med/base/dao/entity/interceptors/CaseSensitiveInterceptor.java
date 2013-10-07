/*
 * @TimeInterceptor.java 1.0 Oct 1, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.reflect.Field;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.dao.annotations.CaseSensitive;

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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * py.una.med.base.dao.entity.interceptors.BaseInterceptor#filter(java.lang
	 * .reflect.Field, java.lang.Object)
	 */
	@Override
	public boolean interceptable(Field f, Object bean) {

		return f.getAnnotation(CaseSensitive.class) == null;
	}

	@Override
	public void intercept(Field f, Object bean) {

		Object o = ReflectionUtils.getField(f, bean);
		String s = (String) o;
		if (s != null) {
			s = s.toUpperCase();
			ReflectionUtils.setField(f, bean, s);
		}
	}

}
