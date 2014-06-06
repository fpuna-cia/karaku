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
package py.una.pol.karaku.dao.entity.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import py.una.pol.karaku.math.Quantity;

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
		X toRet = null;
		if (BigDecimal.class.isAssignableFrom(type)) {
			toRet = (X) value;
		}
		if (BigInteger.class.isAssignableFrom(type)) {
			toRet = (X) value.toBigIntegerExact();
		}
		toRet = checkForPrimitives(type, value, toRet);
		if (toRet != null) {
			return toRet;
		}
		throw unknownUnwrap(type);
	}

	/**
	 * @param type
	 * @param value
	 * @param toRet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <X> X checkForPrimitives(Class<X> type, BigDecimal value, X x) {

		X toRet = x;
		if (Byte.class.isAssignableFrom(type)) {
			toRet = (X) Byte.valueOf(value.byteValue());
		}
		if (Short.class.isAssignableFrom(type)) {
			toRet = (X) Short.valueOf(value.shortValue());
		}
		if (Integer.class.isAssignableFrom(type)) {
			toRet = (X) Integer.valueOf(value.intValue());
		}
		if (Long.class.isAssignableFrom(type)) {
			toRet = (X) Long.valueOf(value.longValue());
		}
		if (Double.class.isAssignableFrom(type)) {
			toRet = (X) Double.valueOf(value.doubleValue());
		}
		if (Float.class.isAssignableFrom(type)) {
			toRet = (X) Float.valueOf(value.floatValue());
		}
		return toRet;
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
