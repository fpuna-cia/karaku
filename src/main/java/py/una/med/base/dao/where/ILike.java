package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class ILike implements Clause {

	private String path;
	private Object value;
	private MatchMode mode = MatchMode.CONTAIN;

	public String getPath() {

		return path;
	}

	public void setPath(String path) {

		this.path = path;
	}

	public Object getValue() {

		return value;
	}

	public void setValue(Object value) {

		this.value = value;
	}

	@Override
	public Criterion getCriterion() {

		return Restrictions.ilike(path, value.toString(), getMode()
				.getMatchMode());
	}

	public void setMode(MatchMode mode) {

		this.mode = mode;
	}

	public MatchMode getMode() {

		return mode;
	}

	public ILike(String path, Object value) {

		super();
		this.path = path;
		this.value = value;
	}

	public ILike(String path, Object value, MatchMode mode) {

		super();
		this.path = path;
		this.value = value;
		setMode(mode);
	}
}
