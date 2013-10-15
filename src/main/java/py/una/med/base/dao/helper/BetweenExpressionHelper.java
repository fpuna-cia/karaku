/*
 * BetweenExpressionHelper.java 1.0 12/03/2013
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Between;

/**
 * Helper para la clase {@link Between}.
 *
 * <p>
 * Simplemente crea un {@link Restrictions#between(String, Object, Object)}
 * generando antes un alias válido para la propiedad.
 * </p>
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 *
 */
@Component
public final class BetweenExpressionHelper extends BaseClauseHelper<Between> {

	@Override
	public Criterion getCriterion(Criteria criteria, Between clause,
			Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		return Restrictions.between(aliasWithProperty, clause.getBegin(),
				clause.getEnd());
	}
}
