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
package py.una.pol.karaku.dynamic.data;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link PagingDataSource} implementado utilizando una lista de objetos, la
 * paginación se realiza en memoria.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Jun 24, 2013
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ListPagingDataSource extends AbstractPagingDataSource {

	private final List totalItems;
	private List currentItems;

	/**
	 * 
	 */
	public ListPagingDataSource(List<?> items) {

		this.totalItems = items;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#getTotalCount()
	 */
	@Override
	public Long getTotalCount() {

		return (long) totalItems.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#getItems()
	 */
	@Override
	public List<?> getItems() {

		return currentItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#refresh()
	 */

	@Override
	public void refresh() {

		Long inicio = (getCurrentPage() - 1) * getRegistersPerPage();
		Long fin = getCurrentPage() * getRegistersPerPage();
		if (fin > getTotalCount()) {
			fin = getTotalCount();
		}
		if (currentItems == null) {
			currentItems = new ArrayList(getRegistersPerPage().intValue());
		}
		currentItems.clear();
		for (Long i = inicio; i < fin; i++) {
			currentItems.add(totalItems.get(i.intValue()));
		}

	}
}
