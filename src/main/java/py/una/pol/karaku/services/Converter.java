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

import javax.annotation.Nonnull;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.EntityNotFoundException;
import py.una.pol.karaku.replication.Shareable;

/**
 * Interfaz que define un objeto que se encarga de convertir Entidades a DTO.
 * 
 * <p>
 * En este contexto, un <b>DTO</b> es un objeto que esta preparado para ser
 * enviado por la red, idealmente no tiene datos internos ni relaciones
 * indeseadas.
 * </p>
 * 
 * <p>
 * Por ejemplo, un DTO, no debería tener referencias a entidades a las que esta
 * asociada (sí a las que compone), más bien debería tener una cadena con la URI
 * de la entidad, facilitando así el acceso a la misma y no sobrecargando el
 * mensaje.
 * </p>
 * 
 * <p>
 * Un converter debe ser un componente de Spring, y si se desea acceder a otros
 * converters, se debe usar el componente {@link ConverterProvider}, el cual es
 * capaz de proveer otros converters que estan en el contexto.
 * </p>
 * 
 * <p>
 * Se provee una implementación por defecto denominada
 * {@link ReflectionConverter}, el cual hace una copia atributo por atributo, si
 * bien es una implementación válida para los casos más básicos, no se
 * recomienda su uso.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * @see ConverterProvider
 * @see AbstractConverter
 * @see ReflectionConverter
 * 
 */
/**
 * 
 * @author Victor Ughelli
 * @since 1.0
 * @version 1.0 17/08/2015
 * 
 */
public interface Converter<E extends Shareable, T extends DTO> {

    /**
     * Convierte una entidad a un DTO.
     * 
     * <p>
     * La conversión no necesariamente debe ser atributo por atributo, y esta
     * limitada a un <i>depth</i> o profundidad, que define cuantos niveles
     * deben ser convertidos.
     * </p>
     * 
     * <p>
     * <b>Todos los atributos que retorna este método deben ser
     * serializables.</b>
     * </p>
     * 
     * @param entity
     *            entidad a ser convertida
     * @param depth
     *            máximo nivel de anidación de relaciones a convertir
     * @return objeto listo para ser enviado por la red, nunca <code>null</code>
     */
    @Nonnull
    T toDTO(E entity, int depth);

    /**
     * Convierte un DTO a una entidad válida para la base de datos actual.
     * 
     * <p>
     * Esta conversión puede utilizar otras llamadas a servicios para completar
     * un objeto más grande.
     * </p>
     * 
     * <p>
     * No se debe completar el identificador del mismo, es decir el ID del
     * objeto debe ser nulo, el {@link Shareable#getUri()} es el mecanismo
     * elegido para poder apuntar a la entidad de la base de datos a la que
     * apunta.
     * </p>
     * 
     * @param dto
     * @return entidad del sistema, nunca <code>null</code>
     */
    @Nonnull
    E toEntity(T dto) throws EntityNotFoundException;

    /**
     * Retorna la clase del DTO.
     * 
     * <p>
     * Este método es un método que facilita el trabajo del
     * {@link ConverterProvider}, no debería ser necesario reimplementarlo.
     * </p>
     * 
     * @return clase del DTO, nunca <code>null</code>
     */
    Class<T> getDtoType();

    /**
     * Retorna el tipo de la entidad.
     * 
     * <p>
     * Este método es un método que facilita el trabajo del
     * {@link ConverterProvider}, no debería ser necesario reimplementarlo.
     * </p>
     * 
     * @return tipo de la clase, nunca <code>null</code>
     */
    Class<E> getEntityType();

    /**
     * 
     * Refresca la entidad a replicar
     * 
     * Este método trae de forma completa todos los atributos de la entidad a
     * replicar. De manera a que la replicación de entidades que referencien a
     * otras entidades se realice de forma correcta.
     * 
     * @param entity
     * @param depth
     * @return
     */
    T beforeToDTO(E entity, int depth);
}
