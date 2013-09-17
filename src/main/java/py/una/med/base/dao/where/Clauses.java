/*
 * @Clauses.java 1.0 Sep 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import java.util.List;
import py.una.med.base.dao.helper.OrExpressionHelper;
import py.una.med.base.dao.restrictions.NumberLike;
import py.una.med.base.dao.restrictions.Where;

/**
 * Clase que sirve de punto de acceso común para todas las clauses que son
 * aceptadas por el sistema
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1 08/02/2013
 * 
 */
public final class Clauses {

	private Clauses() {

		// No-op
	}

	/**
	 * Retorna una {@link Clause} que se encarga de comparar fechas como si
	 * fueran cadenas.
	 * <p>
	 * El SQL que genera esta cláusula es similar a:
	 * 
	 * <pre>
	 * where <b>path</b> like "%<b>object.toString()</b>%"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo por el cual se desea comparar.
	 * @param object
	 *            objeto con el cual se comparará.
	 * @return {@link ILike}
	 * @see #numberLike(String, Number, MatchMode)
	 */
	public static Clause iLike(final String path, final Object object) {

		return Clauses.iLike(path, object, MatchMode.CONTAIN);
	}

	/**
	 * Retorna una {@link Clause} que se encarga de comparar fechas como si
	 * fueran cadenas.
	 * <p>
	 * El SQL que genera esta cláusula es similar a:
	 * 
	 * <pre>
	 * where <b>path</b> like "%<b>object.toString()</b>%"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo por el cual se desea comparar.
	 * @param object
	 *            objeto con el cual se comparará.
	 * @param matchMode
	 *            {@link MatchMode} para indicar banderas.
	 * @return {@link ILike}
	 * @see #numberLike(String, Number, MatchMode)
	 */
	public static Clause iLike(final String path, final Object object,
			final MatchMode matchMode) {

		return new ILike(path, object, matchMode);
	}

	/**
	 * Retorna una {@link Clause} que se encarga de comparar numeros como si
	 * fueran cadenas.
	 * <p>
	 * El SQL que genera esta cláusula es similar a:
	 * 
	 * <pre>
	 * where <b>path</b> like "%<b>number</b>%"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo por el cual se desea comparar.
	 * @param number
	 *            número por el cual se verificara.
	 * @return {@link NumberLike}
	 * @see #numberLike(String, Number, MatchMode)
	 */
	public static Clause numberLike(final String path, final String number) {

		return Clauses.numberLike(path, number, MatchMode.CONTAIN);
	}

	/**
	 * Retorna una {@link Clause} que se encarga de comparar numeros como si
	 * fueran cadenas.
	 * <p>
	 * El SQL que genera esta cláusula es similar a:
	 * 
	 * <pre>
	 * where <b>path</b> like "%<b>number</b>%"
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo por el cual se desea comparar.
	 * @param number
	 *            número por el cual se verificara.
	 * @return {@link NumberLike}
	 * @see #numberLike(String, Number, MatchMode)
	 */
	public static Clause numberLike(final String path, final Number number) {

		return Clauses.numberLike(path, number, MatchMode.CONTAIN);
	}

	/**
	 * Retorna una {@link Clause} que se encarga de comparar numeros como si
	 * fueran cadenas.
	 * <p>
	 * El SQL que genera esta cláusula es similar a:
	 * 
	 * <pre>
	 * where <b>path</b> like "%<b>number</b>%"
	 * </pre>
	 * 
	 * Donde las <code>%</code> dependen del atributo {@link MatchMode}.
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo por el cual se desea comparar.
	 * @param number
	 *            número por el cual se verificara.
	 * @param matchMode
	 *            {@link MatchMode} por el cual se verificara.
	 * @return {@link NumberLike}
	 * @see #numberLike(String, Number, MatchMode)
	 */
	public static Clause numberLike(final String path, final String number,
			final MatchMode matchMode) {

		return new NumberLike(path, number, matchMode);
	}

	/**
	 * Retorna una {@link Clause} que se encarga de comparar números como si
	 * fueran cadenas.
	 * <p>
	 * El SQL que genera esta cláusula es similar a:
	 * 
	 * <pre>
	 * where <b>path</b> like "%<b>number</b>%"
	 * </pre>
	 * 
	 * Donde las <code>%</code> dependen del atributo {@link MatchMode}.
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo por el cual se desea comparar.
	 * @param number
	 *            número por el cual se verificara.
	 * @param matchMode
	 *            {@link MatchMode} por el cual se verificara.
	 * @return {@link NumberLike}
	 * @see #numberLike(String, String, MatchMode)
	 */
	public static Clause numberLike(final String path, final Number number,
			final MatchMode matchMode) {

		if (number == null) {
			throw new IllegalArgumentException("Number can't be null");
		}
		return Clauses.numberLike(path, number.toString(), matchMode);
	}

	/**
	 * Retorna un {@link Clause} que compara las cláusulas pasadas como una
	 * disjunción (o). Significa que los objetos retornados deberán cumplir
	 * <b>con al menos</b> una de las {@link Clause} pasadas.
	 * <p>
	 * Si se desea que cumplan con todas las cláusulas, se debe formar una lista
	 * con ellas y pasarlas al {@link Where}. Es decir, la conjunción es la
	 * forma de unir expresiones por defecto.
	 * </p>
	 * <p>
	 * La cláusula retornada ({@link Or}) genera un SQL similar a :
	 * 
	 * <pre>
	 * 	where <i>clause1</i> <b>or</b> <i>clause2</i> <b>or</b> <i>clause3</i> ...
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @see OrExpressionHelper
	 * @param clauses
	 *            una o mas {@link Clause}.
	 * @return {@link Or} para comparar una disjunción, nunca <code>null</code>
	 *         .
	 */
	public static Clause or(final Clause ... clauses) {

		if (clauses == null) {
			throw new IllegalArgumentException("clauses can't be null");
		}

		return new Or(clauses);
	}

	/**
	 * Retorna un {@link Clause} que compara las cláusulas pasadas como una
	 * disjunción (o). Significa que los objetos retornados deberán cumplir
	 * <b>con al menos</b> una de las {@link Clause} pasadas.
	 * <p>
	 * Si se desea que cumplan con todas las cláusulas, se debe formar una lista
	 * con ellas y pasarlas al {@link Where}. Es decir, la conjunción es la
	 * forma de unir expresiones por defecto.
	 * </p>
	 * <p>
	 * La cláusula retornada ({@link Or}) genera un SQL similar a :
	 * 
	 * <pre>
	 * 	where <i>clause1</i> <b>or</b> <i>clause2</i> <b>or</b> <i>clause3</i> ...
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @see OrExpressionHelper
	 * @param clauses
	 *            una o mas {@link Clause}.
	 * @return {@link Or} para comparar una disjunción, nunca <code>null</code>
	 *         .
	 */
	public static Clause or(List<Clause> clauses) {

		if (clauses == null) {
			throw new IllegalArgumentException("clauses can't be null");
		}
		return Clauses.or(clauses.toArray(new Clause[clauses.size()]));
	}

	/**
	 * Retorna unas {@link Clause} que es la negación de la pasada como
	 * parámetro.
	 * <p>
	 * Si la {@link Clause} X retorna n de los N elementos de un conjunto ,
	 * entonces la {@link Clause} {@link Not} retornada por este mÃ©todo,
	 * retornará los N - n elementos no retornados por la consulta sin negar.
	 * </p>
	 * <p>
	 * Este mÃ©todo es idempotente, es decir, se lo puede invocar N veces,
	 * teniendo siempre el mismo resultado.
	 * </p>
	 * 
	 * 
	 * @param clause
	 *            Clause a negar, no puede ser <code>null</code>
	 * @return {@link Not}.
	 */
	public static Clause not(final Clause clause) {

		if (clause == null) {
			throw new IllegalArgumentException("Clause to negate can't be null");
		}
		if (clause instanceof Not) {
			return clause;
		}
		return new Not(clause);
	}

	/**
	 * {@link Clause} que se utiliza para igualar referencias, si se desea
	 * comparar números o cadenas ver {@link #iLike(String, Object)} y
	 * {@link #numberLike(String, Number)} respectivamente.
	 * 
	 * <p>
	 * Esta cláusula genera un sql similar a :
	 * 
	 * <pre>
	 * 	where <i>path</i>.id <b>=</b> <i>value</i>.id
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo en formato HQL.
	 * @param value
	 *            referencia con la cual se desea comparar.
	 * @return {@link Equal}.
	 */
	public static Clause eq(String path, Object value) {

		return new Equal(path, value);
	}

	public static Clause ge(String path, Object value) {

		return new Ge(path, value);
	}
}
