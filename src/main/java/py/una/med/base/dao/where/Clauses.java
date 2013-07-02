package py.una.med.base.dao.where;

import py.una.med.base.dao.restrictions.NumberLike;

/**
 * Clase que sirve de punto de acceso comun para todas las clauses que son
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

	public static Clause iLike(final String path, final Object object) {

		return new ILike(path, object);
	}

	public static Clause iLike(final String path, final Object object,
			final MatchMode matchMode) {

		return new ILike(path, object, matchMode);
	}

	public static Clause numberLike(final String path, final String number) {

		return new NumberLike(path, number);
	}

	public static Clause numberLike(final String path, final String number,
			final MatchMode matchMode) {

		return new NumberLike(path, number, matchMode.getMatchMode());
	}

	public static Clause or(final Clause ... clauses) {

		return new Or(clauses);
	}

	public static Clause not(final Clause clause) {

		return new Not(clause);
	}
}
