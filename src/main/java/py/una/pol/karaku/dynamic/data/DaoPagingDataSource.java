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

import java.util.List;
import py.una.pol.karaku.dao.BaseDAO;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.domain.BaseEntity;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Jun 24, 2013
 * 
 */
public class DaoPagingDataSource<T extends BaseEntity> extends
		AbstractPagingDataSource {

	private final BaseDAO<T, Long> dao;
	private List<T> items;

	/**
	 * 
	 */
	public DaoPagingDataSource(BaseDAO<T, Long> dao) {

		this.dao = dao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#getTotalCount()
	 */
	@Override
	public Long getTotalCount() {

		return dao.getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#getItems()
	 */
	@Override
	public List<?> getItems() {

		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.dynamic.data.PagingDataSource#refresh()
	 */
	@Override
	public void refresh() {

		ISearchParam sp = new SearchParam();
		sp.setLimit(getRegistersPerPage().intValue());
		sp.setOffset(getCurrentPage().intValue()
				* getRegistersPerPage().intValue());
		items = dao.getAll(sp);

	}

}
