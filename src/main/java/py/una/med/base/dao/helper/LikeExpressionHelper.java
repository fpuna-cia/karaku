/*
 * @LikeExpressionHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.ILike;

/**
 * {@link BaseClauseHelper} que se encarga de configurar las consultas del tipo
 * {@link ILike}, no tiene limite de anidaciones.
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 *
 */
@Component
public class LikeExpressionHelper extends BaseClauseHelper<ILike> {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * py.una.med.base.dao.helper.BaseClauseHelper#getCriterion(org.hibernate
	 * .Criteria, py.una.med.base.dao.where.Clause, java.util.Map)
	 */
	@Override
	public Criterion getCriterion(Criteria criteria, ILike clause,
			Map<String, String> aliases) {

		LikeExpression likeExpression = (LikeExpression) clause.getCriterion();
		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		if (aliasWithProperty == null) {
			return likeExpression;
		}
		return Restrictions.ilike(aliasWithProperty, clause.getValue()
				.toString(), clause.getMode().getMatchMode());
	}
}
