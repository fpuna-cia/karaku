/*
 * OrExpressionHelper.java 1.0 12/03/2013
 */
package py.una.med.base.dao.helper;

import static py.una.med.base.util.Checker.notNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Or;

/**
 * 
 * 
 * @author Diego Acuña
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 * 
 */
@Component
public final class OrExpressionHelper extends BaseClauseHelper<Or> {

	@Autowired
	private RestrictionHelper helper;

	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull Or clause, @Nonnull Map<String, String> aliases) {

		List<Criterion> criterions = helper.getCriterions(
				notNull(Arrays.asList(clause.getClauses())), criteria, aliases);
		if (criterions.isEmpty()) {
			return null;
		}
		if (criterions.size() == 1) {
			return criterions.get(0);
		}

		Criterion toRet = Restrictions.or(criterions.get(0), criterions.get(1));
		for (int i = 2; i < criterions.size(); i++) {
			toRet = Restrictions.or(toRet, criterions.get(i));
		}
		return toRet;
	}
}
