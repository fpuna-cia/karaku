/*
 * @Lt.java 1.0 14/10/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Implementación de la cláusula SQL "<"
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 14/10/2013
 *
 */
public class Lt implements Clause {

	@Nonnull private final Object value;
	@Nonnull private final String path;

	@Override
	public Criterion getCriterion() {

		return Restrictions.lt(this.path, this.value);
	}

	public Lt(@Nonnull String path, @Nonnull Object value) {

		this.path = path;
		this.value = value;
	}

	@Nonnull
	public Object getValue() {

		return this.value;
	}

	@Nonnull
	public String getPath() {

		return this.path;
	}
}
