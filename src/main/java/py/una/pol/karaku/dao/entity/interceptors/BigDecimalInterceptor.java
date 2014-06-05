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
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.exception.KarakuRuntimeException;

/**
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 3, 2013
 *
 */
@Component
public class BigDecimalInterceptor extends AbstractInterceptor {

	/**
	 *
	 */
	private static final int MAXIMUM_PRECISION = 2;

	@Override
	public Class<?>[] getObservedTypes() {

		return new Class<?>[] { BigDecimal.class };
	}

	@Override
	public Class<?>[] getObservedAnnotations() {

		return new Class<?>[] { void.class };
	}

	@Override
	public void intercept(Operation o, Field field, Object bean) {

		BigDecimal value = (BigDecimal) ReflectionUtils.getField(field, bean);
		if (value == null) {
			return;
		}
		if (value.longValue() < 1) {
			value = value.add(BigDecimal.ONE);
		}
		BigDecimal trailed = value.stripTrailingZeros();
		int precision = trailed.scale();
		if (precision > MAXIMUM_PRECISION) {
			throw new KarakuRuntimeException(
					String.format(
							"Attribute '%s' of bean '%s' has a precision of {%d}, maximum allowed is {%d}",
							field.getName(), bean.getClass().getSimpleName(),
							precision, MAXIMUM_PRECISION));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean interceptable(Operation op, Field field, Object bean) {

		return op != Operation.DELETE;
	}
}
