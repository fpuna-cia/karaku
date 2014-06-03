/*
 * @RegexExpressionHelper.java 1.0 21/01/2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.dao.where.Regex;

/**
 * 
 * @author Arsenio Ferreira
 * @since 1.0
 * @version 1.0 21/01/2014
 * 
 */
@Component
public class RegexExpressionHelper extends BaseClauseHelper<Regex> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull Regex re, @Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, re.getPath(),
				aliases);
		return new Regex(aliasWithProperty, re.getValue());

	}

}
