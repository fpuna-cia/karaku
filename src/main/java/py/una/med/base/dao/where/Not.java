package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class Not implements IClause {

	private IClause clause;

	public Not(IClause clause) {

		super();
		this.clause = clause;
	}

	public IClause getClause() {

		return clause;
	}

	@Override
	public Criterion getCriterion() {

		if (clause == null) {
			return null;
		} else {
			// Criterion aRet = clause.getCriterion();
			Criterion aRet = Restrictions.not(clause.getCriterion());
			return aRet;

		}
	}
}
