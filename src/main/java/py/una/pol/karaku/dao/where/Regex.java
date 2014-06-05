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
package py.una.pol.karaku.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;

/**
 * Implementacion del operador Expresion Regular "~"
 * 
 * @author Arsenio Ferreira
 * @since 1.0
 * @version 1.0 21/01/2014
 * 
 */
public class Regex implements Clause, Criterion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Nonnull
	private final Object value;
	@Nonnull
	private final String path;

	public Regex(@Nonnull String path, @Nonnull Object value) {

		this.path = path;
		this.value = value;
	}

	@Nonnull
	public Object getValue() {

		return value;
	}

	@Nonnull
	public String getPath() {

		return path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Criterion getCriterion() {

		return null;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {

		String[] columns = criteriaQuery.findColumns(getPath(), criteria);
		if (columns.length != 1) {
			throw new HibernateException(
					"Regex may only be used with single-column properties");
		}
		SessionFactoryImplementor factory = criteriaQuery.getFactory();

		if (factory.getDialect() instanceof H2Dialect) {
			return columns[0] + " regexp ?";
		} else {
			// XXX ver consistencia entre bases de datos
			return columns[0] + " ~ ?";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypedValue[] getTypedValues(Criteria criteria,
			CriteriaQuery criteriaQuery) {

		TypedValue tv = new TypedValue(new org.hibernate.type.StringType(),
				value, EntityMode.POJO);
		return new TypedValue[] { tv };
	}

}
