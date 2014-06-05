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
package py.una.pol.karaku.util;

import java.util.Collections;
import java.util.List;
import javax.faces.model.SelectItem;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.ISearchParam;

/**
 * Lista de objetos en memoria con paginación.
 * 
 * 
 * 
 * @author Nathalia Ochoa
 * @author Claudia Valenzuela
 * @since 1.0
 * @version 1.0 23/08/2013
 * 
 */
public class KarakuListHelperInMemory<T> implements KarakuListHelperProvider<T> {

	private List<T> listMemory;
	private SimpleFilter simpleFilter;
	private PagingHelper pagingHelper;

	public KarakuListHelperInMemory(List<T> list) {

		this(new SimpleFilter(), list);
	}

	public KarakuListHelperInMemory(SimpleFilter simpleFilter, List<T> list) {

		setListMemory(list);
		this.simpleFilter = simpleFilter;

	}

	@Override
	public List<T> getEntities() {

		List<T> toShow = doFilter(listMemory, getSimpleFilter().getOption(),
				getSimpleFilter().getValue());

		long size = toShow.size();
		getHelper().udpateCount(size);

		ISearchParam isp = getHelper().getISearchparam();
		int from = isp.getOffset();
		int to = isp.getOffset() + isp.getLimit();
		if (to > toShow.size()) {
			to = toShow.size();
		}

		return toShow.subList(from, to);
	}

	@Override
	public void reloadEntities() {

		// nada que hacer, la lista esta en memoria.
	}

	/**
	 * Realiza el filtro de elementos.
	 * 
	 * <p>
	 * Este método es utilizado antes de calcular la paginación y debe retornar
	 * una sublista de los elementos
	 * <p>
	 * 
	 * 
	 * @param toFilter
	 * @param option
	 * @param value
	 * @return
	 */
	public List<T> doFilter(List<T> toFilter, String option, String value) {

		return toFilter;
	}

	@Override
	public SimpleFilter getSimpleFilter() {

		return simpleFilter;
	}

	@Override
	public List<SelectItem> getFilterOptions() {

		return Collections.emptyList();
	}

	public List<T> getListMemory() {

		return listMemory;
	}

	public final void setListMemory(List<T> listMemory) {

		if (listMemory == null) {
			setListMemory(Collections.<T> emptyList());
			return;
		}
		this.listMemory = listMemory;
	}

	@Override
	public void setBaseWhere(Where<T> where) {

		// TODO Auto-generated method stub
	}

	@Override
	public PagingHelper getHelper() {

		if (pagingHelper == null) {
			pagingHelper = new PagingHelper(5);
		}

		return pagingHelper;
	}

}
