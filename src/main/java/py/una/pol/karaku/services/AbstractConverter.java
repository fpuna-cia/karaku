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
package py.una.pol.karaku.services;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.log.Log;
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
/**
 * 
 * @author Victor Ughelli
 * @since 1.0
 * @version 1.0 17/08/2015
 * 
 */
@Transactional
public abstract class AbstractConverter<E extends Shareable, T extends DTO>
        implements Converter<E, T> {

    @Log
    private Logger log;

    @Autowired
    private SessionFactory factory;

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

    @Override
    public T beforeToDTO(E entity, int depth) {

        Session session = factory.getCurrentSession();
        try {
            E fromdb = (E) session.get(entity.getClass(), (Long) entity
                    .getClass().getMethod("getId").invoke(entity));
            return toDTO(fromdb, depth);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            log.error(
                    "Replication terminated with errors, could not refresh entity",
                    e);

        }
        return null;
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
