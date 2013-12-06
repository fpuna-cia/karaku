/*
 * @Converter.java 1.0 Nov 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services;

import py.una.med.base.replication.DTO;
import py.una.med.base.replication.EntityNotFoundException;
import py.una.med.base.replication.Shareable;

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
	 * @return objeto listo para ser enviado por la red.
	 */
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
	 * @return
	 */
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
}
