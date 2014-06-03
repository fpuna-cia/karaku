/*
 * @QuantityType.java 1.0 Oct 9, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.entity.types;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import py.una.med.base.math.Quantity;

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
