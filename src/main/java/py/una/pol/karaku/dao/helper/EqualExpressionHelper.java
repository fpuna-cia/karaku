/*
 * @NotExpressionHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.where.Equal;

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
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull Equal clause, @Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		if (clause.getValue() == null) {
			return Restrictions.isNull(aliasWithProperty);
		}
		return Restrictions.eq(aliasWithProperty, clause.getValue());

	}
}
