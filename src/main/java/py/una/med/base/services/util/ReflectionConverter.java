/*
 * @ReflexionConverter.java 1.0 Nov 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.services.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.replication.Shareable;
import py.una.med.base.services.Converter;

/**
 * {@link Converter} que utiliza reflexión.
 *
 * <p>
 * Este converter es el converter por defecto de cualquier entidad, y su uso no
 * es muy recomendado ya que no implementa una lógica compleja y absolutamente
 * todo es compartido.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 12, 2013
 *
 */
public abstract class ReflectionConverter<E extends Shareable, T extends Shareable>
		implements Converter<E, T> {

	Class<T> dtoClass;
	Class<E> entityClass;

	public ReflectionConverter(Class<T> dtoClass, Class<E> entityClass) {

		this.dtoClass = dtoClass;
		this.entityClass = entityClass;
	}

	@Override
	public Class<T> getDtoType() {

		return dtoClass;
	}

	@Override
	public Class<E> getEntityType() {

		return entityClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T toDTO(E entity, int depth) {

		return (T) map(entity, getDtoType(), depth, false);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E toEntity(T dto) {

		return (E) map(dto, getEntityType(), Integer.MAX_VALUE, true);
	}

	/**
	 *
	 * @param source
	 * @param targetClass
	 * @param depth
	 * @param dtoToEntity
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object map(final Object source, final Class<?> targetClass,
			final int depth, final boolean dtoToEntity) {

		if (source == null) {
			return null;
		}
		try {
			final Object toRet = targetClass.newInstance();
			ReflectionUtils.doWithFields(source.getClass(),
					new FieldCallback() {

						@Override
						public void doWith(Field field)
								throws IllegalAccessException {

							Field toSet = ReflectionUtils.findField(
									targetClass, field.getName());
							if (toSet == null) {
								return;
							}
							field.setAccessible(true);
							toSet.setAccessible(true);
							Class<?> s = field.getType();
							Class<?> t = toSet.getType();

							if (Collection.class.isAssignableFrom(s)
									&& Collection.class.isAssignableFrom(t)) {
								if (depth > 0) {
									Collection colTarget = getNewCollection(t);
									Collection colSource = (Collection) field
											.get(source);
									if (colSource == null) {
										return;
									}
									Class targetRowType = GenericCollectionTypeResolver
											.getCollectionFieldType(toSet);
									for (Object row : colSource) {
										if (row instanceof Shareable) {
											colTarget.add(convert(
													row.getClass(),
													targetRowType,
													(Shareable) row, depth - 1,
													dtoToEntity));
										} else {
											colTarget.add(map(row,
													targetRowType, depth - 1,
													dtoToEntity));
										}
									}
									toSet.set(toRet, colTarget);
								}
							} else if (s.isAssignableFrom(t)) {
								Object o = field.get(source);
								toSet.set(toRet, o);
							} else if (Shareable.class.isAssignableFrom(s)
									&& Shareable.class.isAssignableFrom(t)) {
								Object o = convert(s, t,
										(Shareable) field.get(source),
										depth - 1, dtoToEntity);
								toSet.set(toRet, o);
							} else {

								throw new KarakuRuntimeException(String.format(
										"Cant copy from field %s to field %s",
										field, toSet));
							}
						}
					}, ReflectionUtils.COPYABLE_FIELDS);
			return toRet;
		} catch (Exception e) {
			throw new KarakuRuntimeException(
					String.format("Can't copy from %s to %s",
							source.getClass(), targetClass), e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object convert(Class fromClass, Class toClass, Shareable object,
			int depth, boolean dtoToEntity) {

		if (dtoToEntity) {
			return getConverter(toClass, fromClass).toEntity(object);
		} else {
			return getConverter(fromClass, toClass).toDTO(object, depth);
		}
	}

	@SuppressWarnings("rawtypes")
	private Collection getNewCollection(Class collectionClass) {

		if (List.class.isAssignableFrom(collectionClass)) {
			return new ArrayList();
		}
		if (Set.class.isAssignableFrom(collectionClass)) {
			return new HashSet();
		}
		throw new KarakuRuntimeException("Unsupported type of collection "
				+ collectionClass);
	}

	public abstract <Y extends Shareable, O extends Shareable> Converter<Y, O> getConverter(
			Class<Y> entityClass, Class<O> dtoClass);
}
