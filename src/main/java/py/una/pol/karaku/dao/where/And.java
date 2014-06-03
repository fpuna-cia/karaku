/*
 * @Where Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dao.where;

import org.hibernate.criterion.Criterion;

/**
 * Clase que implementa la cláusula And (sql = 'and').
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 12/03/2013
 *
 */
public class And implements Clause {

	private Clause[] clauses;

	/**
	 * Retorna un {@link Clause} que compara las cláusulas pasadas como una
	 * conjunción (y). Significa que los objetos retornados deberán cumplir
	 * <b>con todas</b> las {@link Clause} pasadas.
	 * <p>
	 * Si se desea que cumplan con al menos una de todas las cláusulas, se debe
	 * formar una lista con ellas y utilizar un {@link Or}.
	 * </p>
	 * <p>
	 * La conjunción es la forma de unir expresiones por defecto. Es decir, si
	 * no se desea agregar condiciones anidadas, no es necesario utlizar esta
	 * clase.
	 * </p>
	 * <p>
	 *
	 * <pre>
	 * 	where <i>clause1</i> <b>and</b> <i>clause2</i> <b>and</b> <i>clause3</i> ...
	 * </pre>
	 *
	 * </p>
	 *
	 * @param clauses
	 *            una o mas {@link Clause}.
	 * @see py.una.pol.karaku.dao.helper.AndExpressionHelper
	 */
	public And(Clause ... clauses) {

		super();
		this.clauses = clauses.clone();
	}

	/**
	 * Retorna la lista de {@link Clause} que actualmente une esta cláusula.
	 *
	 * @return
	 */
	public Clause[] getClauses() {

		return clauses.clone();
	}

	/**
	 * Depende exclusivamente de AndExpressiónHelper.
	 */
	@Override
	public Criterion getCriterion() {

		return null;
	}
}
