/*
 * @NotExpressionHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Equal;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@Component
public class EqualExpressionHelper extends BaseClauseHelper<Equal> {

	@Override
	public Criterion getCriterion(Criteria criteria, Equal clause,
			Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		return Restrictions.eq(aliasWithProperty, clause.getValue());

	}
}
