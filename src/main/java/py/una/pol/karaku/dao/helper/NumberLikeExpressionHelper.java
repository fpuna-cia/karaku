package py.una.med.base.dao.helper;

import java.util.Map;
import javax.annotation.Nonnull;
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
	 * Esta implementación retorna una copia del {@link NumberLike} pasado con
	 * el alias configurado correctamente.
	 * 
	 * <p>
	 * Se retorna una copia del {@link NumberLike} pues si se modifica el
	 * {@link NumberLike} original para que contenga el alias correcto, las
	 * siguientes invocaciones a este método retornarían una version corrupta de
	 * la claúsula. Este método debe ser idempotente.
	 * </p>
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public Criterion getCriterion(@Nonnull Criteria criteria,
			@Nonnull NumberLike clause, @Nonnull Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria,
				clause.getPropiedad(), aliases);
		return new NumberLike(aliasWithProperty, clause.getValor(),
				clause.getMatchMode());

	}

}
