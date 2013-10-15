/*
 * @Where Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;

/**
 * Clase que implementa la cláusula OR.
 *
 * @author Diego Acuña
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 *
 */
public class Or implements Clause {

	private final Clause[] clauses;

	public Or(Clause[] clauses) {

		super();
		this.clauses = clauses.clone();
	}

	public Clause[] getClauses() {

		return this.clauses.clone();
	}

	@Override
	public Criterion getCriterion() {

		return null;
	}
}
