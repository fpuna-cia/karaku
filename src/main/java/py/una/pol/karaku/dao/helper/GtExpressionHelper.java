/*
 * @GtExpressionHelper.java 1.0 Oct 14, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.where.Gt;

/**
 * 
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 14/10/2013
 * 
 */
@Component
public class GtExpressionHelper extends BaseClauseHelper<Gt> {

	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria, @Nonnull Gt gt,
			@Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, gt.getPath(),
				aliases);
		return Restrictions.gt(aliasWithProperty, gt.getValue());

	}

}
