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
package py.una.pol.karaku.controller;

import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.replication.Shareable;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 26, 2014
 * 
 */
public abstract class KarakuBaseShareableController<T extends Shareable> extends
		KarakuAdvancedController<T, Long> {

	private static final String FIELD_ACTIVO = "activo";

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Agrega la restricción de que solo se muestren aquellas entidades que
	 * están activas.
	 * </p>
	 */
	@Override
	public Where<T> getBaseWhere() {

		return super.getBaseWhere().addClause(
				Clauses.eq(getActivoFieldName(), Shareable.YES));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Agrega la restricción de que solo se muestren aquellas entidades que
	 * están activas.
	 * </p>
	 */
	@Override
	public Where<T> getWhereReport() {

		return super.getWhereReport().addClause(
				Clauses.eq(getActivoFieldName(), Shareable.YES));
	}

	/**
	 * Retorna un Where que solo muestra entidades activas.
	 * 
	 * @return
	 */
	protected <Z> Where<Z> getOnlyActivosWhere() {

		Where<Z> where = Where.get();
		where.addClause(Clauses.eq(getActivoFieldName(), Shareable.YES));
		return where;

	}

	/**
	 * Devuelve el nombre del campo que indica si la entidad está activa.
	 * 
	 * @return nombre del campo activo
	 */
	protected String getActivoFieldName() {

		return FIELD_ACTIVO;
	}

}
