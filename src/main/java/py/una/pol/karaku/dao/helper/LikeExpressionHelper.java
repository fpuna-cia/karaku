/*
 * @LikeExpressionHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
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

	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull ILike clause, @Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, clause.getPath(),
				aliases);
		return Restrictions.ilike(aliasWithProperty, clause.getValue()
				.toString(), clause.getMode().getMatchMode());
	}
}
