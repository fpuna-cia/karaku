/*
 * @UriInterceptor.java 1.0 Nov 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.reflect.Field;
import java.math.BigInteger;
import org.apache.commons.lang3.Validate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.dao.entity.Operation;
import py.una.med.base.dao.entity.annotations.URI;
import py.una.med.base.util.StringUtils;

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
	SessionFactory factory;

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

		URI uri = field.getAnnotation(URI.class);

		Validate.validState(uri != null,
				"Intercept a field without uri in UriInterceptor");

		String baseUri = Validate.notNull(uri.baseUri(),
				"Base uri in a @URI is null or empty");

		String finalUri = null;
		switch (uri.type()) {
			case FIELD:
				finalUri = byUniqueField(field, bean, uri);
				break;
			case SEQUENCE:
				finalUri = bySequence(field, bean, uri);
				break;
		}

		ReflectionUtils.setField(field, bean, baseUri + finalUri);

	}

	private String bySequence(Field f, Object bean, URI uri) {

		Validate.validState(StringUtils.isValid(uri.sequenceName()),
				"URI with type Sequence without sequence name %s", f.getName());

		return getNextSequence(uri.sequenceName());
	}

	private String byUniqueField(Field f, Object bean, URI uri) {

		Validate.validState(StringUtils.isValid(uri.field()),
				"URI with type Field without atribute name %s", f.getName());

		Object fieldValue = getFieldValueOfBean(bean, uri.field());

		Validate.validState(fieldValue != null,
				"The unique field %s, is null or empty", f.getName());

		Validate.validState(StringUtils.isValid(fieldValue.toString()),
				"The unique field %s is invalid (%s)", f.getName(), fieldValue);

		return fieldValue.toString().trim().replace(' ', '_');
	}

	protected String getNextSequence(String sequenceName) {

		BigInteger nextVal = (BigInteger) factory
				.getCurrentSession()
				.createSQLQuery(
						String.format("select nextval('%s');", sequenceName))
				.uniqueResult();
		return "" + nextVal.longValue();
	}
}