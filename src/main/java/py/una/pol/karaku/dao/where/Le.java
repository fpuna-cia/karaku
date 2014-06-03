package py.una.pol.karaku.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Implementación de la cláusula SQL "<="
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0
 * 
 */
public class Le implements Clause {

	@Nonnull
	private Object value;
	@Nonnull
	private String path;

	@Override
	public Criterion getCriterion() {

		return Restrictions.le(this.path, this.value);
	}

	public Le(@Nonnull String path, @Nonnull Object value) {

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
