/*
 * OrExpressionHelper.java 1.0 12/03/2013
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.Restrictions;
import py.una.med.base.dao.restrictions.NumberLike;
import py.una.med.base.dao.where.IClause;
import py.una.med.base.dao.where.ILike;
import py.una.med.base.dao.where.Or;

/**
 * 
 * 
 * @author Diego Acuña, Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 * 
 */
public final class OrExpressionHelper {

	private OrExpressionHelper() {

		// No-op
	}

	public static Map<String, String> applyNestedCriteria(Criteria criteria,
			Or or, Map<String, String> aliases) {

		if (or.getClauses() == null || or.getClauses().length == 0) {
			return aliases;
		}

		if (or.getClauses().length == 1) {
			criteria.add(configureClause(criteria, or.getClauses()[0], aliases));
			return aliases;
		}

		Criterion previus = Restrictions.or(
				configureClause(criteria, or.getClauses()[0], aliases),
				configureClause(criteria, or.getClauses()[1], aliases));
		for (int i = 2; i < or.getClauses().length; i++) {
			previus = Restrictions.or(previus,
					configureClause(criteria, or.getClauses()[i], aliases));
		}
		criteria.add(previus);
		return aliases;
	}

	public static Criterion configureClause(Criteria criteria, IClause c,
			Map<String, String> alias) {

		if (c instanceof ILike) {
			return LikeExpressionHelper.getCriterion(criteria,
					(LikeExpression) ((ILike) c).getCriterion(), alias);

		} else if (c instanceof NumberLike) {
			return NumberLikerExpressionHelper.getCriterion(criteria,
					((NumberLike) c), alias);
		}

		return null;
	}
}
