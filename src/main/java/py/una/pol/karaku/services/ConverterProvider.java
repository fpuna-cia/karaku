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

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.Shareable;

/**
 * Proveedor de {@link Converter}.
 * 
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * 
 */
@Component
public class ConverterProvider {

    @Autowired(required = false)
    @SuppressWarnings("rawtypes")
    private List<Converter> converters;

    /**
     * Provee un converter.
     * 
     * <p>
     * Busca en el contexto de Spring por algún converter que se pueda aplicar a
     * los tipos pedidos, en el caso de que ninún converter sea encontrado,
     * retorna un {@link ReflectionConverter}
     * </p>
     * 
     * @param entityClass
     *            clase de la entidad
     * @param dtoClass
     *            clase del dto objetivo
     * @return converter, nunca <code>null</code>
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <E extends Shareable, T extends DTO> Converter<E, T> getConverter(
            Class<E> entityClass, Class<T> dtoClass) {

        if (converters != null) {
            for (Converter c : converters) {
                if (c.getDtoType().equals(dtoClass)
                        && c.getEntityType().equals(entityClass)) {
                    return c;
                }
            }
        }
        return new ContextReflexionConverter<E, T>(dtoClass, entityClass);
    }

    private class ContextReflexionConverter<E extends Shareable, T extends DTO>
            extends ReflectionConverter<E, T> {

        /**
         * @param dtoClass
         * @param entityClass
         */
        public ContextReflexionConverter(Class<T> dtoClass, Class<E> entityClass) {

            super(dtoClass, entityClass);
        }

        @Override
        public <Y extends Shareable, O extends DTO> Converter<Y, O> getConverter(
                Class<Y> entityClass, Class<O> dtoClass) {

            return ConverterProvider.this.getConverter(entityClass, dtoClass);
        }

        @Override
        public T beforeToDTO(E entity, int depth) {

            return toDTO(entity, depth);
        }
    }
}
