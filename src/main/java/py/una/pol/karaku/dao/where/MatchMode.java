/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
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
