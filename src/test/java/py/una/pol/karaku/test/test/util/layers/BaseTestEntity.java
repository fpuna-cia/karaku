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
package py.una.pol.karaku.test.test.util.layers;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import py.una.pol.karaku.domain.BaseEntity;

/**
 * 
 * @author arturo
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
@MappedSuperclass
// @TypeDefs({ @TypeDef(defaultForType = Date.class, typeClass = DateType.class)
// })
public class BaseTestEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 528988319740601664L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 50)
	private String description;

	/**
	 * Nombre en espaÃ±ol para que no tenga problemas de tipo de datos al usar
	 * como columna
	 */
	private Date fecha;

	private BigDecimal bigDecimal;

	/**
	 * @return description
	 */
	public String getDescription() {

		return this.description;
	}

	/**
	 * @param description
	 *            description para setear
	 */
	public void setDescription(String description) {

		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.domain.BaseEntity#getId()
	 */
	@Override
	public Long getId() {

		return this.id;
	}

	/**
	 * @param id
	 *            id para setear
	 */
	@Override
	public void setId(Long id) {

		this.id = id;
	}

	/**
	 * @return bigDecimal
	 */
	public BigDecimal getBigDecimal() {

		return this.bigDecimal;
	}

	/**
	 * @param bigDecimal
	 *            bigDecimal para setear
	 */
	public void setBigDecimal(BigDecimal bigDecimal) {

		this.bigDecimal = bigDecimal;
	}

	/**
	 * @return fecha
	 */
	public Date getFecha() {

		return this.fecha;
	}

	/**
	 * @param fecha
	 *            fecha para setear
	 */
	public void setFecha(Date fecha) {

		this.fecha = fecha;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		BaseTestEntity other = (BaseTestEntity) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return this.getId().toString();
	}
}
