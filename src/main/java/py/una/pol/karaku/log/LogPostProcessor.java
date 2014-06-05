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
package py.una.pol.karaku.log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.ReflectionUtils.MethodFilter;
import py.una.pol.karaku.exception.KarakuRuntimeException;

/**
 * {@link BeanPostProcessor} encargado de inyectar los {@link Logger} a través
 * de la anotación {@link Log}.
 * <p>
 * Se utiliza el método {@link #postProcessBeforeInitialization(Object, String)}
 * por que si se utiliza el método
 * {@link #postProcessBeforeInitialization(Object, String)} el objeto recibido
 * ya es un proxy de CGLib
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * @see Log
 */
@Component
public class LogPostProcessor implements BeanPostProcessor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LogPostProcessor.class);

	/**
	 * Revisa todos los métodos o campos que tengan la anotación {@link Log} y
	 * le asigna el valor el Log del bean en cuestión.
	 * 
	 * <br />
	 * 
	 * {@inheritDoc}
	 * 
	 * @param bean
	 *            bean a procesar
	 * @param beanName
	 *            nombre del bean
	 * @return el mismo bean
	 * @throws BeansException
	 *             nunca
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object bean,
			final String beanName) {

		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalAccessException {

				if (!Modifier.isFinal(field.getModifiers())) {
					field.setAccessible(true);
					Log log = field.getAnnotation(Log.class);
					field.set(bean, getLogger(bean, log));
				}
			}

		}, new FieldFilter() {

			@Override
			public boolean matches(Field field) {

				return field.getAnnotation(Log.class) != null;
			}
		});

		ReflectionUtils.doWithMethods(bean.getClass(), new MethodCallback() {

			@Override
			public void doWith(Method method) throws IllegalAccessException {

				Log log = method.getAnnotation(Log.class);
				try {
					method.invoke(bean, getLogger(bean, log));
				} catch (InvocationTargetException e) {
					LOGGER.warn("Error extracting proxy object",
							new KarakuRuntimeException(
									"Can not set logger for: "
											+ bean.getClass().getName(), e));
				}
			}
		}, new MethodFilter() {

			@Override
			public boolean matches(Method method) {

				return method.getAnnotation(Log.class) != null;

			}
		});

		return bean;

	}

	/**
	 * Esta implementación no hace ninguna acción.
	 * 
	 * @param bean
	 *            bean a procesar
	 * @param beanName
	 *            nombre del bean
	 * @return el mismo bean
	 * @throws BeansException
	 *             nunca
	 */
	@Override
	public Object postProcessAfterInitialization(final Object bean,
			final String beanName) {

		return bean;
	}

	/**
	 * Recupera el {@link Logger} del {@link LoggerFactory} a través del nombre
	 * del log definido en la anotación o por el nombre de la clase.
	 * 
	 * @param bean
	 * @param log
	 * @return {@link Logger}, nunca <code>null</code>.
	 */
	private Logger getLogger(final Object bean, Log log) {

		Logger logger;
		if (log.name() == null || "".equals(log.name().trim())) {
			logger = LoggerFactory.getLogger(bean.getClass());
		} else {
			logger = LoggerFactory.getLogger(log.name().trim());
		}
		return logger;
	}
}
