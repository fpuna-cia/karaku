/*
 * @QuantityTypeDescriptor.java 1.0 Oct 9, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.entity.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import py.una.med.base.math.Quantity;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 9, 2013
 * 
 */
public class QuantityTypeDescriptor extends AbstractTypeDescriptor<Quantity> {

	private static final long serialVersionUID = 2547858898177423421L;

	public static final QuantityTypeDescriptor INSTANCE = new QuantityTypeDescriptor();

	public QuantityTypeDescriptor() {

		super(Quantity.class);
	}

	@Override
	public String toString(Quantity value) {

		return value.toString();
	}

	@Override
	public Quantity fromString(String string) {

		return new Quantity(string);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X unwrap(Quantity quantity, Class<X> type, WrapperOptions options) {

		if (quantity == null) {
			return null;
		}
		BigDecimal value = quantity.bigDecimalValue();
		if (BigDecimal.class.isAssignableFrom(type)) {
			return (X) value;
		}
		if (BigInteger.class.isAssignableFrom(type)) {
			return (X) value.toBigIntegerExact();
		}
		if (Byte.class.isAssignableFrom(type)) {
			return (X) Byte.valueOf(value.byteValue());
		}
		if (Short.class.isAssignableFrom(type)) {
			return (X) Short.valueOf(value.shortValue());
		}
		if (Integer.class.isAssignableFrom(type)) {
			return (X) Integer.valueOf(value.intValue());
		}
		if (Long.class.isAssignableFrom(type)) {
			return (X) Long.valueOf(value.longValue());
		}
		if (Double.class.isAssignableFrom(type)) {
			return (X) Double.valueOf(value.doubleValue());
		}
		if (Float.class.isAssignableFrom(type)) {
			return (X) Float.valueOf(value.floatValue());
		}
		throw unknownUnwrap(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X> Quantity wrap(X value, WrapperOptions options) {

		if (value == null) {
			return null;
		}
		if (Quantity.class.isInstance(value)) {
			return (Quantity) value;
		}
		if (BigDecimal.class.isInstance(value)) {
			return new Quantity((BigDecimal) value);
		}
		if (BigInteger.class.isInstance(value)) {
			return new Quantity(new BigDecimal((BigInteger) value));
		}
		if (Number.class.isInstance(value)) {
			return new Quantity(((Number) value).doubleValue());
		}
		throw unknownWrap(value.getClass());
	}
}
