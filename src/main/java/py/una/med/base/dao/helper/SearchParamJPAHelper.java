package py.una.med.base.dao.helper;

import java.util.ArrayList;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import py.una.med.base.dao.search.OrderParam;
import py.una.med.base.dao.search.SearchParam;

public class SearchParamJPAHelper<T> {

	public TypedQuery<T> apply(SearchParam params, TypedQuery<T> query) {

		if (params == null)
			return query;

		if (params.getLimit() != null && params.getLimit() > 0L)
			query.setMaxResults(params.getLimit().intValue());
		if (params.getOffset() != null && params.getOffset() > 0L)
			query.setFirstResult(params.getOffset().intValue());
		return query;
	}

	public CriteriaQuery<T> apply(SearchParam params, CriteriaQuery<T> query,
			Root<T> root, CriteriaBuilder builder) {

		if (params == null)
			return query;
		ArrayList<Order> orders = new ArrayList<Order>();
		if (params.getOrders() != null)
			for (OrderParam orderParam : params.getOrders()) {
				if (orderParam.isAsc())
					orders.add(builder.asc(root.get(orderParam.getColumnName())));
				else
					orders.add(builder.desc(root.get(orderParam.getColumnName())));
			}
		query.orderBy(orders);
		return query;
	}

	public CriteriaQuery<T> aplly(CriteriaBuilder criteriaBuilder,
			CriteriaQuery<T> criteriaQuery, Root<T> root, T ejemplo) {

		// ParameterExpression<Integer> p = criteriaBuilder
		// .parameter(Integer.class);
		// criteriaQuery.w
		return criteriaQuery;

	}
}
