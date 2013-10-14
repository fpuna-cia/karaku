/*
 * @LtExpressionHelper.java 1.0 Oct 14, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Lt;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 14/10/2013
 * 
 */
@Component
public class LtExpressionHelper extends BaseClauseHelper<Lt> {

	@Override
	public Criterion getCriterion(Criteria criteria, Lt lt,
			Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, lt.getPath(),
				aliases);
		return Restrictions.lt(aliasWithProperty, lt.getValue());

	}

}
