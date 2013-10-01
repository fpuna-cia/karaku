package py.una.med.base.dao.where;

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

	private Object value;
	private String path;

	@Override
	public Criterion getCriterion() {

		return Restrictions.le(this.path, this.value);
	}

	public Le(String path, Object value) {

		this.path = path;
		this.value = value;
	}

	public Object getValue() {

		return value;
	}

	public String getPath() {

		return path;
	}
}
