package py.una.med.base.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Implementacion de la clausula SQL "mayor que o igual"
 * 
 * @author Uriel Gonzalez
 * 
 */
public class Ge implements Clause {

	@Nonnull
	private Object value;
	@Nonnull
	private String path;

	@Override
	public Criterion getCriterion() {

		return Restrictions.ge(this.path, this.value);
	}

	public Ge(@Nonnull String path, @Nonnull Object value) {

		this.path = path;
		this.value = value;
	}

	@Nonnull
	public Object getValue() {

		return value;
	}

	@Nonnull
	public String getPath() {

		return path;
	}
}
