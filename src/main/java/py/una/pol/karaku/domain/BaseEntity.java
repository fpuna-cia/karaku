/*
 * @BaseEntity.java 1.0 May 24, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.domain;

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
