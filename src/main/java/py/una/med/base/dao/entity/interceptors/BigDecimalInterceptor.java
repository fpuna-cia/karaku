/*
 * @BigDecimalInterceptor.java 1.0 Oct 3, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.entity.interceptors;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.exception.KarakuRuntimeException;

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
	public void intercept(Field field, Object bean) {

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
}
