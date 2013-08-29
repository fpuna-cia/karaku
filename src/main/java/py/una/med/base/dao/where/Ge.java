package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Implementacion de la clausula SQL "mayor que o igual"
 * 
 * @author Uriel Gonzalez
 * 
 */
public class Ge implements Clause {

	private Object value;
	private String path;

	@Override
	public Criterion getCriterion() {

		return Restrictions.ge(this.path, this.value);
	}

	public Ge(String path, Object value) {

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
