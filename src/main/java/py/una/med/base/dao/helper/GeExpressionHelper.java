/*
 * @GeExpressionHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.where.Ge;

/**
 *
 * @author Arturo Volpe
 * @author Uriel Gonzalez
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 *
 */
@Component
public class GeExpressionHelper extends BaseClauseHelper<Ge> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Criterion getCriterion(Criteria criteria, Ge le,
			Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, le.getPath(),
				aliases);
		if (aliasWithProperty == null) {
			return le.getCriterion();
		}
		return Restrictions.ge(aliasWithProperty, le.getValue());

	}

}
