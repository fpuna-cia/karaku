/*
 * @InterceptorHandler.java 1.0 Oct 1, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import py.una.med.base.log.Log;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 1, 2013
 * 
 */
@Component
public class InterceptorHandler implements InitializingBean {

	@Log
	private transient Logger log;

	@Autowired
	List<Interceptor> interceptors;

	private transient int interceptorsCount;

	Map<Class<?>, Set<Interceptor>> byType;
	Map<Class<?>, Set<Interceptor>> byAnnotation;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		this.log.info("Building InterceptorHandler");
		// Map<String, AbstractInterceptor> interceptors = this.ac
		// .getBeansOfType(AbstractInterceptor.class);
		this.interceptorsCount = interceptors.size();
		for (Interceptor bi : interceptors) {// .values()) {
			this.log.info("Add: {}", bi.getClass().getSimpleName());
			for (Class<?> type : bi.getObservedTypes()) {
				// Cuando type == null, entonces se agregara al grupo de
				// interceptors asociados con void.class!.
				this.getInterceptorsByType(type).add(bi);
			}
			for (Class<?> annon : bi.getObservedAnnotations()) {
				// Cuando annon == null, entonces se agregara al grupo de
				// interceptors asociados con la anotación void.class (osea
				// todas).
				this.getInterceptorsByAnnotation(annon).add(bi);
			}
		}

	}

	public void intercept(final Object bean) {

		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {

				InterceptorHandler.this.intercept(field, bean);
			}
		});
	}

	public void intercept(Field field, Object bean) {

		field.setAccessible(true);
		Class<?> type = field.getType();

		Set<Interceptor> typeInterceptors = this.getInterceptorsByType(type);
		typeInterceptors.addAll(this.getInterceptorsByType(void.class));

		Annotation[] annons = field.getAnnotations();
		Set<Interceptor> annonInterceptors = this
				.getInterceptorsByAnnotation(void.class);
		for (Annotation an : annons) {
			annonInterceptors.addAll(this.getInterceptorsByAnnotation(an
					.annotationType()));
		}
		typeInterceptors.retainAll(annonInterceptors);

		for (Interceptor bi : typeInterceptors) {
			if (this.isAssignable(field) && bi.interceptable(field, bean)) {
				bi.intercept(field, bean);
			}
		}
	}

	private Set<Interceptor> getInterceptorsByType(Class<?> type) {

		Set<Interceptor> interceptors = this.getByType().get(type);
		if (interceptors == null) {
			interceptors = new HashSet<Interceptor>();
			this.getByType().put(type, interceptors);
		}

		return interceptors;
	}

	private Set<Interceptor> getInterceptorsByAnnotation(Class<?> type) {

		Set<Interceptor> interceptors = this.getByAnnotation().get(type);
		if (interceptors == null) {
			interceptors = new HashSet<Interceptor>();
			this.getByAnnotation().put(type, interceptors);
		}

		return interceptors;
	}

	/**
	 * @return byType
	 */
	private Map<Class<?>, Set<Interceptor>> getByType() {

		if (this.byType == null) {
			this.byType = new HashMap<Class<?>, Set<Interceptor>>();

		}

		return this.byType;
	}

	/**
	 * @return byAnnotation
	 */
	private Map<Class<?>, Set<Interceptor>> getByAnnotation() {

		if (this.byAnnotation == null) {
			this.byAnnotation = new HashMap<Class<?>, Set<Interceptor>>();
		}

		return this.byAnnotation;
	}

	/**
	 * Retorna el número de {@link Interceptor} registrados por este componente.
	 * 
	 * @return interceptorsCount
	 */
	public int getInterceptorsCount() {

		return this.interceptorsCount;
	}

	/**
	 * Verifica si un field puede ser asignable. Se define un fiel asignable
	 * como aquel que no es estatico, final.
	 * 
	 * Como nota general tener en cuenta que un campo que no es String es
	 * inmediatamente no asignable
	 * 
	 * @param field
	 *            a ser evaluado
	 * @return <code>true</code> si es asignable, <code>false</code> en caso
	 *         contrario
	 */
	private boolean isAssignable(Field field) {

		int modifier = field.getModifiers();
		if (Modifier.isFinal(modifier)) {
			return false;
		}
		if (Modifier.isStatic(modifier)) {
			return false;
		}
		if (Modifier.isTransient(modifier)) {
			return false;
		}
		return field.getAnnotation(Transient.class) == null;
	}
}
