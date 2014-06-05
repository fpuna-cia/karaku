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
package py.una.pol.karaku.domain;

import java.io.Serializable;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 24, 2013
 * 
 */
public abstract class BaseEntity implements Serializable {

	protected static final long serialVersionUID = 1L;

	public abstract void setId(Long id);

	/**
	 * @return id
	 */
	public abstract Long getId();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		result += getClass().hashCode();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == null) {
			return false;
		}

		/**
		 * Si es la misma referencia necesariamente es igual.
		 */
		if (this == obj) {
			return true;
		}

		/**
		 * Nos aseguramos que sea asignable desde la clase actual, esto
		 * significa que es una subclase o de la misma clase.
		 */
		if (obj.getClass().isAssignableFrom(getClass())) {
			BaseEntity other = (BaseEntity) obj;
			if (this.getId() == null) {
				return other.getId() == null;
			} else {
				return getId().equals(other.getId());
			}

		}

		return false;
	}

}
