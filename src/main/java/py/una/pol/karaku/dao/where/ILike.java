package py.una.med.base.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class ILike implements Clause {

	@Nonnull
	private String path;

	@Nonnull
	private Object value;

	@Nonnull
	private MatchMode mode;

	public ILike(@Nonnull String path, @Nonnull Object value) {

		this(path, value, MatchMode.CONTAIN);
	}

	public ILike(@Nonnull String path, @Nonnull Object value,
			@Nonnull MatchMode mode) {

		super();
		this.path = path;
		this.value = value;
		this.mode = mode;
	}

	@Nonnull
	public String getPath() {

		return path;
	}

	public void setPath(@Nonnull String path) {

		this.path = path;
	}

	public Object getValue() {

		return value;
	}

	public void setValue(@Nonnull Object value) {

		this.value = value;
	}

	@Override
	public Criterion getCriterion() {

		return Restrictions.ilike(path, value.toString(), getMode()
				.getMatchMode());
	}

	public void setMode(@Nonnull MatchMode mode) {

		this.mode = mode;
	}

	public MatchMode getMode() {

		return mode;
	}

}
