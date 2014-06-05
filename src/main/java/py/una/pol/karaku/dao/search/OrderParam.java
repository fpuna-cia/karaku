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
package py.una.pol.karaku.dao.search;

import javax.annotation.Nonnull;

/**
 * Clase que representa una ordenacion de una consulta
 * 
 * @author Arturo Volpe
 * @version 1.0, 23/10/2012
 * @since 1.0
 */
public class OrderParam {

	private boolean asc;

	@Nonnull
	private String columnName;

	/**
	 * Crea una nueva instancia, para el nombre de la columna (tipicamente el
	 * nombre del atributo de la entidad), y un orden definido por el boolean
	 * asc
	 * 
	 * @param asc
	 * @param columnName
	 */
	public OrderParam(boolean asc, @Nonnull String columnName) {

		super();
		this.asc = asc;
		this.columnName = columnName;
	}

	/**
	 * Consulta si es ascendente
	 * 
	 * @return
	 */
	public boolean isAsc() {

		return asc;
	}

	/**
	 * Indica que la columna se ordenara ascendentemnete, false para ordenar
	 * descendentemente
	 * 
	 * @param asc
	 */
	public void setAsc(boolean asc) {

		this.asc = asc;
	}

	/**
	 * Nombre de la columna a ser ordenada, tipicamente el nombre del atributo
	 * de la entidad
	 * 
	 * @return
	 */
	@Nonnull
	public String getColumnName() {

		return columnName;
	}

	/**
	 * Indica que columna sera ordenada por este ordenParam
	 * 
	 * @param columnName
	 */
	public void setColumnName(@Nonnull String columnName) {

		this.columnName = columnName;
	}
}
