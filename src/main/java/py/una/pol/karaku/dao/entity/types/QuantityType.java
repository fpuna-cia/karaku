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
