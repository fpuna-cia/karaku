/**
 * @SearchParam 1.0 23/10/2012
 */
package py.una.med.base.dao.search;

import java.util.ArrayList;
import java.util.List;

public class SearchParam implements ISearchParam {

	private List<OrderParam> orders;
	private Integer offset;
	private Integer limit;

	public OrderParam addOrder(String columnName, boolean asc) {

		if (orders == null)
			orders = new ArrayList<OrderParam>();
		OrderParam newOrder = new OrderParam(asc, columnName);
		orders.add(newOrder);
		return newOrder;
	}

	public List<OrderParam> getOrders() {

		return orders;
	}

	public void setOrders(List<OrderParam> orders) {

		this.orders = orders;
	}

	public Integer getOffset() {

		return offset;
	}

	public void setOffset(Integer offset) {

		this.offset = offset;
	}

	public Integer getLimit() {

		return limit;
	}

	public void setLimit(Integer limit) {

		this.limit = limit;
	}

	public void addExcludedColumn(String string) {

	}

}
