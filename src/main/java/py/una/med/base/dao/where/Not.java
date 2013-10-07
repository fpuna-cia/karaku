package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;

public class Not implements Clause {

	private final Clause clause;

	public Not(Clause clause) {

		super();
		this.clause = clause;
	}

	public Clause getClause() {

		return this.clause;
	}

	@Override
	public Criterion getCriterion() {

		return null;
	}
}
