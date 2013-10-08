/**
 * @SearchParam 1.0 23/10/2012
 */
package py.una.med.base.dao.search;

import java.util.List;
import py.una.med.base.util.ListHelper;

public class SearchParam implements ISearchParam {

	private Integer limit;
	private Integer offset;
	private List<OrderParam> orders;

	@Override
	public ISearchParam addOrder(OrderParam orderParam) {

		if (getOrders() == null) {
			setOrders(ListHelper.getAsList(orderParam));
		} else {
			getOrders().add(orderParam);
		}
		return this;
	}

	@Override
	public ISearchParam addOrder(String columnName, boolean asc) {

		return addOrder(new OrderParam(asc, columnName));
	}

	@Override
	public ISearchParam addOrder(String columnName) {

		return addOrder(new OrderParam(true, columnName));
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
