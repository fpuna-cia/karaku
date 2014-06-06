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
package py.una.pol.karaku.test.test.math.layers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import py.una.pol.karaku.math.Quantity;

/**
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 9, 2013
 * 
 */
@Entity(name = "entity")
public class EntityWithQuantity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(scale = 15, precision = 4, columnDefinition = "NUMERIC")
	Quantity q1;

	@Column(scale = 15, precision = 4, columnDefinition = "NUMERIC")
	Quantity q2;

	/**
	 * @return id
	 */
	public Long getId() {

		return id;
	}

	/**
	 * @param id
	 *            id para setear
	 */
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @return q1
	 */
	public Quantity getQ1() {

		return q1;
	}

	/**
	 * @param q1
	 *            q1 para setear
	 */
	public void setQ1(Quantity q1) {

		this.q1 = q1;
	}

	/**
	 * @return q2
	 */
	public Quantity getQ2() {

		return q2;
	}

	/**
	 * @param q2
	 *            q2 para setear
	 */
	public void setQ2(Quantity q2) {

		this.q2 = q2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((q1 == null) ? 0 : q1.hashCode());
		result = prime * result + ((q2 == null) ? 0 : q2.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityWithQuantity other = (EntityWithQuantity) obj;
		if (q1 == null) {
			if (other.q1 != null)
				return false;
		} else if (!q1.equals(other.q1))
			return false;
		if (q2 == null) {
			if (other.q2 != null)
				return false;
		} else if (!q2.equals(other.q2))
			return false;
		return true;
	}

}
