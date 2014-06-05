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

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.dao.entity.Operation;

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
	}

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
	protected Object getFieldValueOfBean(@Nonnull Object bean,
			@Nonnull String field) {

		Field unique = ReflectionUtils.findField(bean.getClass(), field);
		unique.setAccessible(true);
		Object uniqueColumn = ReflectionUtils.getField(unique, bean);
		unique.setAccessible(false);
		return uniqueColumn;
	}
}
