package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.NumberLike;

/**
 * Helper que se encarga de crear los alias necesarios para que se pueda navegar
 * en una relación entre joins
 * <p>
 * Este es un helper particular pues {@link NumberLike} es a la vez
 * {@link Clause} y {@link Criterion}
 * </p>
 *
 * @see BaseClauseHelper
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1 08/02/2013
 *
 */
@Component
public final class NumberLikeExpressionHelper extends
		BaseClauseHelper<NumberLike> {

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public Criterion getCriterion(Criteria criteria, NumberLike clause,
			Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria,
				clause.getPropiedad(), aliases);
		clause.setPropiedad(aliasWithProperty);
		return clause;

	}

}
