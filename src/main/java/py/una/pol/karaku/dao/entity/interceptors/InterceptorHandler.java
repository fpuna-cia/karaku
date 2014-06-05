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
package py.una.pol.karaku.dao.entity.interceptors;

import static py.una.pol.karaku.util.Checker.notNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.log.Log;
import py.una.pol.karaku.util.ListHelper;

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
	private List<Interceptor> interceptors;

	private transient int interceptorsCount;

	private Map<Class<?>, Set<Interceptor>> byType;
	private Map<Class<?>, Set<Interceptor>> byAnnotation;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() {

		this.log.info("Building InterceptorHandler");
		this.interceptorsCount = interceptors.size();
		for (Interceptor bi : interceptors) {
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

	/**
	 * Intercepta un bean especifico.
	 * 
	 * <p>
	 * Intercepta todos los campos de un objeto, buscando aquellos que tengan
	 * algún interceptor definido.
	 * </p>
	 * <p>
	 * Reglas:
	 * <ol>
	 * <li>Si el item es un atributo normal, invocar a su respectivo
	 * interceptor.
	 * </p>
	 * <li>Si es una colección, y tiene tiene la anotación {@link OneToMany}, y
	 * su {@link CascadeType} es {@link CascadeType#ALL}, entonces se propaga la
	 * intercepción a los miembros de la colección. </p>
	 * 
	 * @param op
	 *            Operación actual.
	 * @param bean
	 *            objeto que esta siendo interceptado.
	 */
	public void intercept(@Nonnull final Operation op, final Object bean) {

		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalAccessException {

				InterceptorHandler.this.intercept(op, notNull(field), bean);
			}
		});
	}

	/**
	 * Intercepta un atributo de un bean especifico.
	 * 
	 * <p>
	 * Reglas:
	 * <ol>
	 * <li>Si el item es un atributo normal, invocar a su respectivo
	 * interceptor.
	 * </p>
	 * <li>Si es una colección, y tiene tiene la anotación {@link OneToMany}, y
	 * su {@link CascadeType} es {@link CascadeType#ALL}, entonces se propaga la
	 * intercepción a los miembros de la colección. </p>
	 * 
	 * @param op
	 *            Operación actual.
	 * @param field
	 *            campo sobre el cual se esta ejecutando.
	 * @param bean
	 *            objeto que esta siendo interceptado.
	 */
	private void intercept(@Nonnull Operation op, @Nonnull Field field,
			@Nonnull Object bean) {

		if (field.getAnnotation(OneToMany.class) != null) {
			OneToMany otm = field.getAnnotation(OneToMany.class);
			CascadeType[] cascade = otm.cascade();
			if (cascade != null
					&& ListHelper.contains(cascade, CascadeType.ALL)) {
				field.setAccessible(true);
				Collection<?> c = (Collection<?>) ReflectionUtils.getField(
						field, bean);
				if (Hibernate.isInitialized(c) && ListHelper.hasElements(c)) {
					for (Object o : c) {
						this.intercept(op, o);
					}
				}
			}
			return;
		}
		field.setAccessible(true);
		Class<?> type = field.getType();

		Set<Interceptor> typeInterceptors = addAll(byType.get(void.class),
				byType.get(type));

		Annotation[] annons = field.getAnnotations();

		Set<Interceptor> annonInterceptors = new HashSet<Interceptor>();
		if (byAnnotation.get(void.class) != null) {
			annonInterceptors.addAll(byAnnotation.get(void.class));
		}

		for (Annotation an : annons) {
			if (byAnnotation.get(an.annotationType()) != null) {
				annonInterceptors.addAll(byAnnotation.get(an.annotationType()));
			}
		}

		typeInterceptors.retainAll(annonInterceptors);

		for (Interceptor bi : typeInterceptors) {
			if (this.isAssignable(field) && bi.interceptable(op, field, bean)) {
				bi.intercept(op, field, bean);
			}
		}
	}

	private Set<Interceptor> addAll(Set<Interceptor> base,
			Set<Interceptor> toAdd) {

		Set<Interceptor> toRet;
		if (base == null) {
			toRet = new HashSet<Interceptor>();
		} else {
			toRet = new HashSet<Interceptor>(base);
		}
		if (toAdd != null) {
			toRet.addAll(toAdd);
		}
		return toRet;
	}

	private Set<Interceptor> getInterceptorsByType(Class<?> type) {

		Set<Interceptor> toRet = this.getByType().get(type);
		if (toRet == null) {
			toRet = new HashSet<Interceptor>();
			this.getByType().put(type, toRet);
		}

		return toRet;
	}

	private Set<Interceptor> getInterceptorsByAnnotation(Class<?> type) {

		Set<Interceptor> toRet = this.getByAnnotation().get(type);
		if (toRet == null) {
			toRet = new HashSet<Interceptor>();
			this.getByAnnotation().put(type, toRet);
		}

		return toRet;
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
