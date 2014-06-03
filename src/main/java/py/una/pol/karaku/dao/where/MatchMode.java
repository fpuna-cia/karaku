package py.una.pol.karaku.dao.where;

import javax.annotation.Nonnull;

/**
 * Define un modo de <i>match</i> entre cadenas.
 * 
 * @see org.hibernate.criterion.MatchMode
 * @author Arturo Volpe
 * @since 2.3.0
 * @version 1.0 Dec 19, 2013
 * 
 */
public enum MatchMode {

	/**
	 * La cadena debe ser igual al valor, genera un SQL del tipo:
	 * 
	 * <pre>
	 * 		like '<span style="color:green">value</span>'
	 * </pre>
	 */
	@Nonnull
	EQUAL {

		public String toString(String value) {

			return value;
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {

			return org.hibernate.criterion.MatchMode.EXACT;
		}
	},

	/**
	 * La cadena debe empezar con el valor, genera un SQL del tipo:
	 * 
	 * <pre>
	 * 		like '%<span style="color:green">value</span>'
	 * </pre>
	 */
	@Nonnull
	BEGIN {

		public String toString(String value) {

			return value + '%';
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {

			return org.hibernate.criterion.MatchMode.START;
		}
	},

	/**
	 * La cadena debe terminar con el valor, genera un SQL del tipo:
	 * 
	 * <pre>
	 * 		like '<span style="color:green">value</span>%'
	 * </pre>
	 */
	@Nonnull
	END {

		public String toString(String value) {

			return '%' + value;
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {

			return org.hibernate.criterion.MatchMode.END;
		}
	},

	/**
	 * El valor debe estar contenido en la cadena, genera un SQL del tipo:
	 * 
	 * <pre>
	 * 		like '%<span style="color:green">value</span>%'
	 * </pre>
	 */
	@Nonnull
	CONTAIN {

		public String toString(String value) {

			return '%' + value + '%';
		}

		@Override
		public org.hibernate.criterion.MatchMode getMatchMode() {

			return org.hibernate.criterion.MatchMode.ANYWHERE;
		}
	};

	/**
	 * Retorna la cadena formateada.
	 * 
	 * @param pattern
	 *            cadena a formatear
	 * @return cadena formateada
	 */
	@Nonnull
	public abstract String toString(@Nonnull String pattern);

	/**
	 * Retorna el match mode según para hibernate.
	 * 
	 * @return hibernate MatchMode que cumple la misma función.
	 */
	public abstract org.hibernate.criterion.MatchMode getMatchMode();

}
