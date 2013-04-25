package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class Not implements Clause {

	private Clause clause;

	public Not(Clause clause) {

		super();
		this.clause = clause;
	}

	public Clause getClause() {

		return clause;
	}

	@Override
	public Criterion getCriterion() {

		if (clause == null) {
			return null;
		} else {
			// Criterion aRet = clause.getCriterion();
			Criterion aRet = Restrictions.not(clause.getCriterion());
			System.out.println("----------que paso aca ----------");
			return aRet;

		}
	}
}
