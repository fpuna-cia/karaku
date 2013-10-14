/*
 * @Gt.java 1.0 14/10/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Implementación de la cláusula SQL ">"
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 14/10/2013
 * 
 */
public class Gt implements Clause {

	private final Object value;
	private final String path;

	@Override
	public Criterion getCriterion() {

		return Restrictions.gt(this.path, this.value);
	}

	public Gt(String path, Object value) {

		this.path = path;
		this.value = value;
	}

	public Object getValue() {

		return this.value;
	}

	public String getPath() {

		return this.path;
	}
}
