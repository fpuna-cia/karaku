/*
 * @Clauses.java 1.0 Sep 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import static py.una.med.base.util.Checker.notNull;
import java.util.List;
import javax.annotation.Nonnull;
import py.una.med.base.dao.helper.AndExpressionHelper;
import py.una.med.base.dao.helper.OrExpressionHelper;

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
	public static Clause iLike(@Nonnull final String path,
			@Nonnull final Object object) {

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
	public static Clause iLike(@Nonnull final String path,
			@Nonnull final Object object, @Nonnull final MatchMode matchMode) {

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
	public static Clause numberLike(@Nonnull final String path,
			@Nonnull final String number) {

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
	public static Clause numberLike(@Nonnull final String path,
			@Nonnull final Number number) {

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
	public static Clause numberLike(@Nonnull final String path,
			@Nonnull final String number, @Nonnull final MatchMode matchMode) {

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
	public static Clause numberLike(@Nonnull final String path,
			@Nonnull final Number number, @Nonnull final MatchMode matchMode) {

		return Clauses.numberLike(path, notNull(number.toString()), matchMode);
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
	 * entonces la {@link Clause} {@link Not} retornada por este método,
	 * retornará los N - n elementos no retornados por la consulta sin negar.
	 * </p>
	 * <p>
	 * Notar que la negación de un atributo anidado no siempre implica que el
	 * padre no vuelva a aparecer en la lista retornada, por ejemplo.
	 * 
	 * <pre>
	 * 	<b>id	|	   Nombre</b>		
	 * 	 1	|	 Paraguay
	 * 	 2	|	Argentina
	 * 
	 * 	<b>id	|	   Nombre 	| País</b>		
	 * 	 1	|	 San Lorenzo	|   1
	 * 	 2	|	Asunción	|   1
	 * 
	 * </pre>
	 * 
	 * La consulta:
	 * 
	 * <pre>
	 * select * from Pais p join p.ciudades c where c.nombre = "Asunción"
	 * </pre>
	 * 
	 * </p>
	 * Retornará un registro (Paraguay), pero si negamos el Where, y utilizamos:
	 * 
	 * <pre>
	 * select * from Pais p join p.ciudades c where not(c.nombre = "Asunción")
	 * </pre>
	 * 
	 * </p> <b>Retornará exactamente lo mismo</b>, pues existe una ciudad que no
	 * se llama Asunción y que pertenece a Paraguay.
	 * <p>
	 * Este método es idempotente, es decir, se lo puede invocar N veces,
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
	public static Clause eq(@Nonnull String path, @Nonnull Object value) {

		return new Equal(path, value);
	}

	/**
	 * {@link Clause} que se utiliza comparar objetos, se pueden comparar tanto
	 * números como fechas.
	 * 
	 * <p>
	 * Esta cláusula genera un sql similar a :
	 * 
	 * <pre>
	 * 	where <i>path</i>.date 	<b>&gt;=</b> '12-02-2013'
	 * 	where <i>path</i>.costo	<b>&gt;=</b> 666.25
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo en formato HQL.
	 * @param value
	 *            objeto con la cual se desea comparar.
	 * @return {@link Ge}.
	 */
	public static Clause ge(@Nonnull String path, @Nonnull Object value) {

		return new Ge(path, value);
	}

	/**
	 * {@link Clause} que se utiliza comparar objetos, se pueden comparar tanto
	 * números como fechas.
	 * 
	 * <p>
	 * Esta cláusula genera un sql similar a :
	 * 
	 * <pre>
	 * 	where <i>path</i>.date 	<b>&gt;</b> '12-02-2013'
	 * 	where <i>path</i>.costo	<b>&gt;</b> 666.25
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo en formato HQL.
	 * @param value
	 *            objeto con la cual se desea comparar.
	 * @return {@link Gt}.
	 */
	public static Clause gt(@Nonnull String path, @Nonnull Object value) {

		return new Gt(path, value);
	}

	/**
	 * {@link Clause} que se utiliza comparar objetos, se pueden comparar tanto
	 * números como fechas.
	 * 
	 * <p>
	 * Esta cláusula genera un sql similar a :
	 * 
	 * <pre>
	 * 	where <i>path</i>.date 	<b>&lt;=</b> '12-02-2013'
	 * 	where <i>path</i>.costo	<b>&lt;=</b> 666.25
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo en formato HQL.
	 * @param value
	 *            objeto con la cual se desea comparar.
	 * @return {@link Le}.
	 */
	public static Clause le(@Nonnull String path, @Nonnull Object value) {

		return new Le(path, value);
	}

	/**
	 * {@link Clause} que se utiliza comparar objetos, se pueden comparar tanto
	 * números como fechas.
	 * 
	 * <p>
	 * Esta cláusula genera un sql similar a :
	 * 
	 * <pre>
	 * 	where <i>path</i>.date 	<b>&lt;</b> '12-02-2013'
	 * 	where <i>path</i>.costo	<b>&lt;</b> 666.25
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo en formato HQL.
	 * @param value
	 *            objeto con la cual se desea comparar.
	 * @return {@link Lt}.
	 */
	public static Clause lt(@Nonnull String path, @Nonnull Object value) {

		return new Lt(path, value);
	}

	/**
	 * {@link Clause} que se utiliza para comparar rangos.
	 * 
	 * <p>
	 * Es similar al atributo <code>between</code>, y como este, solo es
	 * aplicable a ciertos tipos de datos como números y fechas.
	 * <p>
	 * 
	 * <p>
	 * Notar que esta comparación es inclusiva, es decir siempre incluirá a los
	 * elementos de los extremos.
	 * </p>
	 * 
	 * @param path
	 *            atributo
	 * @param first
	 *            desde
	 * @param last
	 *            hasta
	 * @return Clause para comparar fechas
	 */
	public static Clause between(@Nonnull String path, @Nonnull Object first,
			@Nonnull Object last) {

		return new Between(path, first, last);
	}

	/**
	 * Retorna un {@link Clause} que compara las cláusulas pasadas como una
	 * conjunción (y). Significa que los objetos retornados deberán cumplir
	 * <b>con todas</b> las {@link Clause} pasadas.
	 * <p>
	 * Si se desea que cumplan con solo alguna de las cláusulas, se deberá
	 * utilizar {@link #or(Clause...)}.
	 * </p>
	 * <p>
	 * La cláusula retornada ({@link And}) genera un SQL similar a :
	 * 
	 * <pre>
	 * 	where <i>clause1</i> <b>and</b> <i>clause2</i> <b>and</b> <i>clause3</i> ...
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @see AndExpressionHelper
	 * @param clauses
	 *            una o mas {@link Clause}.
	 * @return {@link And} para comparar una disjunción, nunca <code>null</code>
	 *         .
	 */
	public static Clause and(final Clause ... clauses) {

		if (clauses == null) {
			throw new IllegalArgumentException("clauses can't be null");
		}

		return new And(clauses);
	}

	/**
	 * {@link Clause} que se utiliza para aplicar una expresion regular a un
	 * atributo de una entidad.
	 * 
	 * <p>
	 * Esta cláusula genera un sql similar a :
	 * 
	 * <pre>
	 * 	where <i>path</i>.costo	<b>~</b> (expresion regular)
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param path
	 *            ubicación del atributo en formato HQL.
	 * @param value
	 *            definicion de la expresion regular.
	 * @return {@link Regex}.
	 */
	public static Clause regex(@Nonnull String path, @Nonnull Object value) {

		return new Regex(path, value);
	}
}
