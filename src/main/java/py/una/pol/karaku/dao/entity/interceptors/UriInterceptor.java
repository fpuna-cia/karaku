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
import java.lang.reflect.Field;
import java.math.BigInteger;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.annotations.URI;
import py.una.pol.karaku.util.StringUtils;

/**
 * Intercepta los atributos con la anotación {@link URI} y genera
 * automaticamente las mismas.
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 * 
 */
@Component
public class UriInterceptor extends AbstractInterceptor {

	@Autowired
	private SessionFactory factory;

	@Override
	public Class<?>[] getObservedTypes() {

		return new Class<?>[] { String.class };
	}

	@Override
	public Class<?>[] getObservedAnnotations() {

		return new Class<?>[] { URI.class };
	}

	@Override
	public boolean interceptable(Operation op, Field field, Object bean) {

		return op == Operation.CREATE;
	}

	@Override
	public void intercept(Operation op, Field field, Object bean) {

		URI uri = notNull(field.getAnnotation(URI.class),
				"Intercept a field without uri in UriInterceptor");

		String baseUri = Validate.notNull(uri.baseUri(),
				"Base uri in a @URI is null or empty");

		String finalUri = null;
		switch (uri.type()) {
			case FIELD:
				finalUri = byUniqueField(field, bean, uri);
				break;
			case SEQUENCE:
				finalUri = bySequence(field, uri);
				break;
			default:
				throw new IllegalArgumentException();
		}

		ReflectionUtils.setField(field, bean, baseUri + finalUri);

	}

	private String bySequence(Field f, URI uri) {

		Validate.validState(StringUtils.isValid(uri.sequenceName()),
				"URI with type Sequence without sequence name %s", f.getName());

		return getNextSequence(uri.sequenceName());
	}

	private String byUniqueField(Field f, @Nonnull Object bean, URI uri) {

		String field = notNull(uri.field(),
				"URI with type Field without atribute name %s", f.getName());

		Object fieldValue = notNull(getFieldValueOfBean(bean, field),
				"The unique field %s, is null or empty", f.getName());

		Validate.validState(StringUtils.isValid(fieldValue.toString()),
				"The unique field %s is invalid (%s)", f.getName(), fieldValue);

		return fieldValue.toString().trim().replace(' ', '_');
	}

	protected String getNextSequence(String sequenceName) {

		BigInteger nextVal = (BigInteger) getSession().createSQLQuery(
				String.format("select nextval('%s');", sequenceName))
				.uniqueResult();
		return "" + nextVal.longValue();
	}

	protected Session getSession() {

		return factory.getCurrentSession();
	}
}
