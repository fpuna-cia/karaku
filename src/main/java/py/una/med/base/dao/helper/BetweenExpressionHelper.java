/*
 * BetweenExpressionHelper.java 1.0 12/03/2013
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Between;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 * 
 */
@Component
public final class BetweenExpressionHelper extends BaseClauseHelper<Between> {

	@Autowired
	private RestrictionHelper helper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * py.una.med.base.dao.helper.BaseClauseHelper#getCriterion(org.hibernate
	 * .Criteria, py.una.med.base.dao.where.Clause, java.util.Map)
	 */
	@Override
	public Criterion getCriterion(Criteria criteria, Between clause,
			Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		if (aliasWithProperty == null) {
			aliasWithProperty = clause.getPath();
		}
		return Restrictions.between(aliasWithProperty, clause.getBegin(),
				clause.getEnd());
	}
}
