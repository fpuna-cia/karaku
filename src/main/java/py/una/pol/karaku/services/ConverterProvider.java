/*
 * @ConverterProvider.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.Shareable;

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
	}
}
