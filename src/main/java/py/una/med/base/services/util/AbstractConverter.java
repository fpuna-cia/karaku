/*
 * @AbstractConverter.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.services.util;

import org.apache.commons.lang.NotImplementedException;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.EntityNotFoundException;
import py.una.med.base.replication.Shareable;
import py.una.med.base.services.Converter;
import py.una.med.base.util.KarakuReflectionUtils;

/**
 * {@link Converter} base.
 * 
 * <p>
 * Es una implementación que simplemente no obliga a la clase no abstracta a
 * implementar todos los métodos, solo los que usará.
 * </p>
 * 
 * <p>
 * Además implementa una forma genérica de obtener el tipo de la entidad y del
 * dto.
 * </p>
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * 
 */
public abstract class AbstractConverter<E extends Shareable, T extends DTO>
		implements Converter<E, T> {

	/**
	 * Clase del DTO.
	 */
	protected Class<T> dtoClass;

	/**
	 * Clase de la entidad.
	 */
	protected Class<E> entityClass;

	@Override
	public Class<T> getDtoType() {

		if (dtoClass == null) {
			dtoClass = KarakuReflectionUtils.getParameterizedClass(this, 1);
		}
		return dtoClass;
	}

	@Override
	public Class<E> getEntityType() {

		if (entityClass == null) {
			entityClass = KarakuReflectionUtils.getParameterizedClass(this, 0);
		}
		return entityClass;
	}

	/**
	 * Throw {@link NotImplementedException} exception, su intención es que si
	 * se usa este método sea sobreescrito.
	 * 
	 * <br>
	 * 
	 * {@inheritDoc}
	 */

	@Override
	public T toDTO(E entity, int depth) {

		throw new NotImplementedException();
	}

	/**
	 * Throw {@link NotImplementedException} exception, su intención es que si
	 * se usa este método sea sobreescrito.
	 * 
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	public E toEntity(T dto) throws EntityNotFoundException {

		throw new NotImplementedException();
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((dtoClass == null) ? 0 : dtoClass.getName().hashCode());
		result = (prime * result)
				+ ((entityClass == null) ? 0 : entityClass.getName().hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractConverter other = (AbstractConverter) obj;
		if (dtoClass == null) {
			if (other.dtoClass != null)
				return false;
		} else if (!dtoClass.equals(other.dtoClass))
			return false;
		if (entityClass == null) {
			if (other.entityClass != null)
				return false;
		} else if (!entityClass.equals(other.entityClass))
			return false;
		return true;
	}

}
