/*
 * @AbstractConverter.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.services;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.EntityNotFoundException;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.util.KarakuReflectionUtils;

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
	private Class<T> dtoClass;

	/**
	 * Clase de la entidad.
	 */
	private Class<E> entityClass;

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

		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		AbstractConverter rhs = (AbstractConverter) obj;
		return new EqualsBuilder().append(dtoClass, rhs.getDtoType())
				.append(entityClass, rhs.getEntityType()).isEquals();
	}

}
