/*
 * AndExpressionHelper.java 1.0 12/03/2013
 */
package py.una.med.base.dao.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.And;

/**
 * 
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 * 
 */
@Component
public final class AndExpressionHelper extends BaseClauseHelper<And> {

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
	public Criterion getCriterion(Criteria criteria, And clause,
			Map<String, String> aliases) {

		List<Criterion> criterions = helper.getCriterions(
				Arrays.asList(clause.getClauses()), criteria, aliases);
		if (criterions.size() == 0) {
			return null;
		}
		if (criterions.size() == 1) {
			return criterions.get(0);
		}

		Criterion toRet = Restrictions.and(criterions.get(0), criterions.get(1));
		for (int i = 2; i < criterions.size(); i++) {
			toRet = Restrictions.and(toRet, criterions.get(i));
		}
		return toRet;
	}
}
