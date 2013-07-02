/*
 * @Where Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Clase que implementa la clausula OR.
 * 
 * @author Diego Acuña, Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 * 
 */
public class Or implements Clause {

	private Clause[] clauses;

	public Or(Clause[] clauses) {

		super();
		this.clauses = clauses.clone();
	}

	public Clause[] getClauses() {

		return clauses;
	}

	@Override
	public Criterion getCriterion() {

		if (clauses == null || clauses.length == 0) {
			return null;
		}

		if (clauses.length == 1) {
			return clauses[0].getCriterion();
		}

		Criterion previus = Restrictions.or(clauses[0].getCriterion(),
				clauses[1].getCriterion());
		for (int i = 2; i < clauses.length; i++) {
			previus = Restrictions.or(previus, clauses[i].getCriterion());
		}
		return previus;
	}
}
