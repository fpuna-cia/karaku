package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.Restrictions;
import py.una.med.base.dao.restrictions.NumberLike;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.ILike;
import py.una.med.base.dao.where.Not;

public class NotExpressionHelper {

	private NotExpressionHelper() {

		// No op
	}

	public static Map<String, String> applyNestedCriteria(Criteria criteria,
			Not not, Map<String, String> aliases) {

		if (not.getClause() == null) {
			return aliases;
		} else {
			criteria.add(configureClause(criteria, not.getClause(), aliases));
			return aliases;
		}
	}

	public static Criterion configureClause(Criteria criteria, Clause c,
			Map<String, String> alias) {

		if (c instanceof ILike) {
			return LikeExpressionHelper.getCriterion(criteria,
					(LikeExpression) ((ILike) c).getCriterion(), alias);

		} else if (c instanceof NumberLike) {
			Criterion toRet = NumberLikerExpressionHelper.getCriterion(
					criteria, ((NumberLike) c), alias);

			return Restrictions.not(toRet);
		}

		return null;
	}

}
