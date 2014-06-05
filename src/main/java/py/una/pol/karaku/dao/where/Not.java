/*
 * 
 */
package py.una.pol.karaku.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;

/**
 * Negación de otra cláusula.
 * 
 * @author Arturo Volpe
 * @since 2.3.0
 * @version 1.0 Dec 19, 2013
 * 
 */
public class Not implements Clause {

	@Nonnull
	private final Clause clause;

	public Not(@Nonnull Clause clause) {

		super();
		this.clause = clause;
	}

	@Nonnull
	public Clause getClause() {

		return this.clause;
	}

	@Override
	public Criterion getCriterion() {

		return null;
	}
}
