/*
 * @BaseTestEntity.java 1.0 Sep 13, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.util.layers;

import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import py.una.med.base.domain.BaseEntity;

/**
 * 
 * @author arturo
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
@MappedSuperclass
public class BaseTestEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 50)
	private String description;

	/**
	 * Nombre en español para que no tenga problemas de tipo de datos al usar
	 * como columna
	 */
	private Date fecha;

	private BigDecimal bigDecimal;

	/**
	 * @return description
	 */
	public String getDescription() {

		return description;
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
	 * @see py.una.med.base.domain.BaseEntity#getId()
	 */
	@Override
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
	 * @return bigDecimal
	 */
	public BigDecimal getBigDecimal() {

		return bigDecimal;
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

		return fecha;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		BaseTestEntity other = (BaseTestEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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

		return getId().toString();
	}
}
