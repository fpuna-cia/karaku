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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import py.una.pol.karaku.math.Quantity;

/**
 * Hibernate Type para la clase {@link Quantity}.
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 9, 2013
 *
 */
public final class QuantityCustomType implements UserType {

	private QuantityType qt = QuantityType.INSTANCE;

	/**
	 * Singleton de esta tipo.
	 */
	public static final QuantityCustomType INSTANCE = new QuantityCustomType();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] sqlTypes() {

		return new int[] { qt.sqlType() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Class returnedClass() {

		return Quantity.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object x, final Object y) {

		if (x == null) {
			return y == null;
		}
		return x.equals(y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(Object x) {

		return x.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner) throws SQLException {

		return qt.nullSafeGet(rs, names, session, owner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws SQLException {

		qt.nullSafeSet(st, value, index, session);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object deepCopy(Object value) {

		if (value == null) {
			return null;
		}
		if (!(Number.class.isAssignableFrom(value.getClass()))) {
			throw new UnsupportedOperationException("Cant convert a "
					+ value.getClass() + " to a Quantity");
		}
		return new Quantity(value.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isMutable() {

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Serializable disassemble(Object value) {

		return qt.disassemble(value, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object assemble(Serializable cached, Object owner) {

		return qt.assemble(cached, null, owner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object replace(Object original, Object target, Object owner) {

		return original;
	}

}
