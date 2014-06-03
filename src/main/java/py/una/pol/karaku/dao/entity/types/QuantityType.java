/*
 * @QuantityType.java 1.0 Oct 9, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dao.entity.types;

import java.sql.Types;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.DecimalTypeDescriptor;
import py.una.pol.karaku.math.Quantity;

/**
 * Hibernate Type para la clase {@link Quantity}.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 9, 2013
 * 
 */
public class QuantityType extends
		AbstractSingleColumnStandardBasicType<Quantity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -930415011312550411L;
	public static final QuantityType INSTANCE = new QuantityType();

	public QuantityType() {

		super(new QuantitySQLTypeDescriptor(), QuantityTypeDescriptor.INSTANCE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {

		return "quantity";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getRegistrationKeys() {

		return new String[] { Quantity.class.getName() };
	}

	static class QuantitySQLTypeDescriptor extends DecimalTypeDescriptor {

		private static final long serialVersionUID = -1213448140308342676L;

		@Override
		public int getSqlType() {

			return Types.NUMERIC;
		}
	}
}
