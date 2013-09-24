/*
 * Equal.java 1.0 08/07/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

/**
 * Implementación de la cláusula SQL <code>=</code>. La implementación es un
 * wrapper de la clase {@link SimpleExpression} obtenida mediante
 * {@link Restrictions#eq(String, Object)}.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jul 8, 2013
 * 
 */
public class Equal implements Clause {

	private Object value;
	private String path;

	/**
	 * Retorna un {@link Criterion} configurado para implementar la
	 * funcionalidad de esta clase.
	 */
	@Override
	public Criterion getCriterion() {

		return Restrictions.eq(path, value);

	}

	/**
	 * Crea una nueva intancia de esta clase utilizando la variable path como
	 * una tributo y value el objeto de referencia para ser comparado, el
	 * {@link Object#toString()} de este valor sera utilizado para construir la
	 * sentencia. <br />
	 * La sentencia generada por esta clase será similar a: <br />
	 * 
	 * <pre>
	 * path = value.toString()
	 * </pre>
	 * 
	 * @param path
	 * @param value
	 */
	public Equal(String path, Object value) {

		super();
		this.value = value;
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public Object getValue() {
		return value;
	}
}
