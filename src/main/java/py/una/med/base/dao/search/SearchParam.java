/**
 * @SearchParam 1.0 23/10/2012
 */
package py.una.med.base.dao.search;

import java.util.ArrayList;
import java.util.List;
import py.una.med.base.util.ListHelper;

public class SearchParam implements ISearchParam {

	private List<OrderParam> orders;
	private Integer offset;
	private Integer limit;

	@Override
	public ISearchParam addOrder(String columnName, boolean asc) {

		if (orders == null) {
			orders = new ArrayList<OrderParam>();
		}
		OrderParam newOrder = new OrderParam(asc, columnName);
		orders.add(newOrder);
		return this;
	}

	@Override
	public List<OrderParam> getOrders() {

		return orders;
	}

	@Override
	public void setOrders(List<OrderParam> orders) {

		this.orders = orders;
	}

	@Override
	public Integer getOffset() {

		return offset;
	}

	@Override
	public ISearchParam setOffset(Integer offset) {

		this.offset = offset;
		return this;
	}

	@Override
	public Integer getLimit() {

		return limit;
	}

	@Override
	public ISearchParam setLimit(Integer limit) {

		this.limit = limit;
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * py.una.med.base.dao.search.ISearchParam#addOrder(py.una.med.base.dao.
	 * search.OrderParam)
	 */
	@Override
	public ISearchParam addOrder(OrderParam orderParam) {

		if (getOrders() == null) {
			setOrders(ListHelper.getAsList(orderParam));
		} else {
			getOrders().add(orderParam);
		}
		return this;
	}
}
