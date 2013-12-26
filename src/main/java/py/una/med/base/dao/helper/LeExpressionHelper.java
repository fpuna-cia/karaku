/*
 * @GeExpressionHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Le;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 20, 2013
 * 
 */
@Component
public class LeExpressionHelper extends BaseClauseHelper<Le> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria, @Nonnull Le le,
			@Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, le.getPath(),
				aliases);
		return Restrictions.le(aliasWithProperty, le.getValue());

	}

}
