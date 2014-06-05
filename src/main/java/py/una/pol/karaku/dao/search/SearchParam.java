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

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.List;
import javax.annotation.Nonnull;
import py.una.pol.karaku.util.ListHelper;

public class SearchParam implements ISearchParam {

	private Integer limit;
	private Integer offset;
	private List<OrderParam> orders;

	@Override
	@Nonnull
	public ISearchParam addOrder(@Nonnull OrderParam orderParam) {

		if (getOrders() == null) {
			setOrders(ListHelper.getAsList(orderParam));
		} else {
			getOrders().add(orderParam);
		}
		return this;
	}

	@Override
	public ISearchParam addOrder(@Nonnull String columnName, boolean asc) {

		return addOrder(new OrderParam(asc, columnName));
	}

	@Override
	public ISearchParam addOrder(String columnName) {

		return addOrder(new OrderParam(true, notNull(columnName)));
	}

	@Override
	public Integer getLimit() {

		return limit;
	}

	@Override
	public Integer getOffset() {

		return offset;
	}

	@Override
	public List<OrderParam> getOrders() {

		return orders;
	}

	@Override
	public ISearchParam setLimit(Integer limit) {

		this.limit = limit;
		return this;
	}

	@Override
	public ISearchParam setOffset(Integer offset) {

		this.offset = offset;
		return this;
	}

	@Override
	public void setOrders(List<OrderParam> orders) {

		this.orders = orders;
	}
}
