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
package py.una.pol.karaku.dao.select;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.util.ListHelper;

/**
 * Simple Transformer que expande el {@link AliasToBeanResultTransformer} para
 * soportar relaciones.
 * 
 * <p>
 * La diferencia es que un alias del tipo: 'pais.id', no era soportado, entonces
 * lo que se hace es, si se encuentra un alias de ese tipo, se crea una
 * instancia del padre (pais) si la misma no existe, y se setea el id.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 13, 2013
 * 
 */
public class KarakuAliasToBeanTransformer<T> extends
		AliasToBeanResultTransformer {

	private static final long serialVersionUID = 8185588266275147013L;
	private static final String COULD_NOT_INSTANTIATE_RESULTCLASS = "Could not instantiate resultclass: ";
	private static final String UNCHECKED = "unchecked";
	private static final String RAWTYPES = "rawtypes";
	private final Class<T> resultClass;
	private boolean isInitialized;
	private String[] aliases;
	private Setter[] setters;

	/**
	 * @param resultClass
	 */
	public KarakuAliasToBeanTransformer(Class<T> resultClass) {

		super(resultClass);
		this.resultClass = resultClass;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] realAlias) {

		T toRet;
		try {
			if (!isInitialized) {
				initialize(realAlias);
			} else {
				check(realAlias);
			}
			resetNestedSetter();
			toRet = resultClass.newInstance();

			for (int i = 0; i < realAlias.length; i++) {
				// Diferencia con AliasToBeanResultTransformer

				// -------------------------------------------
				if (setters[i] != null) {
					setters[i].set(toRet, tuple[i], null);
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException(COULD_NOT_INSTANTIATE_RESULTCLASS
					+ resultClass.getName(), e);
		} catch (IllegalAccessException e) {
			throw new HibernateException(COULD_NOT_INSTANTIATE_RESULTCLASS
					+ resultClass.getName(), e);
		}

		return toRet;
	}

	/**
	 * Comunica que se ha cambiado de bean, para que los {@link NestedSetter},
	 * creen una nueva instancia en vez de reutilizarla actual.
	 */
	private void resetNestedSetter() {

		NestedObjectHolder noh = new NestedObjectHolder();
		for (Setter s : setters) {
			if (s instanceof NestedSetter) {
				NestedSetter nSetter = (NestedSetter) s;
				nSetter.setHolder(noh);
			}
		}
	}

	/**
	 * It's Always distinct.
	 */
	@SuppressWarnings({ RAWTYPES, UNCHECKED })
	@Override
	public List transformList(List list) {

		if (!ListHelper.hasElements(list)) {
			return list;
		}

		Map<Identity, Object> objects = new HashMap<Identity, Object>(
				list.size());
		List toRet = new ArrayList();
		for (Object o : list) {
			if (o == null) {
				continue;
			}
			Identity neI = new Identity(o);
			if (objects.containsKey(neI)) {
				handleDuplicate(objects.get(neI), o);
			} else {
				objects.put(neI, o);
				toRet.add(o);
			}
		}
		return toRet;
	}

	/**
	 * 
	 * @param primary
	 * @param duplicated
	 * @param currentAlias
	 */
	private void handleDuplicate(Object primary, Object duplicated) {

		for (Setter s : setters) {
			if (s instanceof NestedSetter) {
				NestedSetter ns = (NestedSetter) s;
				ns.join(primary, duplicated);
			}
		}
	}

	/**
	 * Helper class para distinguir elementos repetidos.
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Jan 10, 2014
	 * 
	 */
	private static class Identity {

		BaseEntity e;

		public Identity(@Nonnull Object o) {

			e = (BaseEntity) o;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj instanceof Identity) {
				Identity other = (Identity) obj;
				return e.getId().equals(other.e.getId());
			}
			return false;
		}

		@Override
		public int hashCode() {

			return e.getId().hashCode();
		}
	}

	private void initialize(String[] newAlias) {

		PropertyAccessor propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] {
						PropertyAccessorFactory.getPropertyAccessor(
								resultClass, null),
						PropertyAccessorFactory.getPropertyAccessor("field") });
		this.aliases = new String[newAlias.length];
		setters = new Setter[newAlias.length];
		for (int i = 0; i < newAlias.length; i++) {
			String alias = newAlias[i];
			if (alias != null) {
				this.aliases[i] = alias;
				// Diferencia con AliasToBeanResultTransformer

				if (alias.indexOf('.') > 0) {
					// found nested
					setters[i] = new NestedSetter(resultClass, alias);
				} else {

					// -------------------------------------------
					setters[i] = propertyAccessor.getSetter(resultClass, alias);
					// Diferencia con AliasToBeanResultTransformer
				}
				// -------------------------------------------
			}
		}
		isInitialized = true;
	}

	private void check(String[] aliasesToCheck) {

		if (!Arrays.equals(aliasesToCheck, this.aliases)) {
			throw new IllegalStateException(
					"aliases are different from what is cached; aliases="
							+ Arrays.asList(aliasesToCheck) + " cached="
							+ Arrays.asList(this.aliases));
		}
	}

	/**
	 * Almacena objetos creados entre NestedSetter de una misma consulta.
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Jan 9, 2014
	 * 
	 */
	private static class NestedObjectHolder implements Serializable {

		private static final long serialVersionUID = -8862245221089607348L;
		/**
		 * Par de path, currentObject
		 */
		private Map<String, Object> created = new HashMap<String, Object>();

		/**
		 * Define si tiene o no un objeto almacenado.
		 * 
		 * @param path
		 * @return
		 */
		public boolean has(String path) {

			return created.containsKey(path);
		}

		public Object get(String path) {

			return created.get(path);
		}

		public void set(String path, Object object) {

			created.put(path, object);
		}

	}

	private static class NestedSetter implements Setter {

		private static final long serialVersionUID = -3494109024315529035L;
		private final Class<?> root;
		private NestedObjectHolder nestedObjectHandler;
		private String[] properties;
		private boolean hasCollection = false;

		/**
		 * Setter para propiedades anidadas.
		 * 
		 * @param root
		 *            clase raíz (tipicamente la entidad)
		 * @param property
		 *            con al menos un punto (un nivel de anidacion).
		 */
		public NestedSetter(Class<?> root, String property) {

			this.root = root;
			properties = property.split("\\.");
		}

		/**
		 * @param noh
		 */
		public void setHolder(NestedObjectHolder noh) {

			this.nestedObjectHandler = noh;
		}

		/**
		 * <p>
		 * TODO cachear todo!
		 * <p>
		 * Los problemas que existen son:
		 * </p>
		 * <ol>
		 * <li>No se puede almacenar solamente el ultimo setter, pues hay que
		 * obtener el objeto al que seatear</li>
		 * <li>Entre el path puede existir una colección, y esa colección puede
		 * no existir</li>
		 * </ol>
		 * </p> <br>
		 * {@inheritDoc}
		 */
		@SuppressWarnings(RAWTYPES)
		@Override
		public void set(Object target, Object value,
				SessionFactoryImplementor factory) {

			if (value == null) {
				return;
			}

			Object current = target;
			Class currentClass = root;
			Field currentField = null;
			StringBuilder sb = new StringBuilder();

			/*
			 * Cuando termine este for, en current estara el objeto para setear
			 * la propiedad y en currentclass la clase del mismo, por eso es -1,
			 * no hace falta el ultimo por que ahi sucede el trabajo real.
			 */
			for (int i = 0; i < properties.length - 1; i++) {
				String currentProperty = properties[i];
				sb.append(currentProperty).append('.');
				// obtengo el field

				currentField = getField(currentClass, currentProperty);

				// con el field obtengo el tipo
				currentClass = currentField.getType();

				// si es una lista veo que hacer
				if (Collection.class.isAssignableFrom(currentClass)) {
					hasCollection = true;
					// si es una coleccion ver que hacer con current
					current = handleCollection(currentClass, currentField,
							current, sb.toString());
					// al ser una colección, el tipo no es el mismo que el field
					currentClass = current.getClass();
				} else {
					// con el field obtengo el objeto
					current = handleSingle(currentField, current);
				}

			}

			currentField = getField(currentClass,
					properties[properties.length - 1]);
			set(currentField, current, value);

		}

		/**
		 * Une las colecciones de dos objetos repetidos.
		 * 
		 * @param original
		 *            objeto cuyas listas se llenaran
		 * @param duplicate
		 *            objetos de donde se quita información.
		 */
		@SuppressWarnings({ UNCHECKED, RAWTYPES })
		public void join(Object original, Object duplicate) {

			if (!hasCollection) {
				return;
			}

			Class currentClass = root;
			Object currentOriginal = original;
			Object currentDuplicate = duplicate;
			for (int i = 0; i < properties.length - 1; i++) {
				String currentProperty = properties[i];
				// obtengo el field

				Field currentField = getField(currentClass, currentProperty);
				currentClass = currentField.getType();

				// si es una lista las uno
				if (Collection.class.isAssignableFrom(currentClass)) {
					// si es una coleccion ver que hacer con current
					currentField.setAccessible(true);
					try {
						Collection toAdd = (Collection) currentField
								.get(currentDuplicate);
						Collection originalCol = (Collection) currentField
								.get(currentOriginal);
						originalCol.addAll(toAdd);
						toAdd.clear();
					} catch (Exception e) {
						throw new KarakuRuntimeException(e);
					}
					// only works for one collection in the path.
					return;
				}
				currentOriginal = get(currentField, currentOriginal);
				currentDuplicate = get(currentField, currentDuplicate);
			}
		}

		/**
		 * @param currentField
		 * @param current
		 * @return
		 */
		private Object handleSingle(Field currentField, Object current) {

			Object next = get(currentField, current);
			set(currentField, current, next);
			return next;
		}

		/**
		 * @param current
		 * @param next
		 * @param currentField
		 */
		private void set(Field currentField, Object current, Object next) {

			try {
				currentField.setAccessible(true);
				currentField.set(current, next);
				currentField.setAccessible(false);
			} catch (Exception e) {
				throw new KarakuRuntimeException(e);
			}
		}

		@SuppressWarnings({ RAWTYPES, UNCHECKED })
		private Object handleCollection(Class currentClass, Field field,
				Object current, String currentPath) {

			try {
				field.setAccessible(true);
				Object newList = field.get(current);
				if (newList == null) {
					newList = getNew(currentClass);
					field.set(current, newList);
				}

				Object toRet;
				Collection list = (Collection) newList;
				if (!nestedObjectHandler.has(currentPath)) {
					Class cClass = GenericCollectionTypeResolver
							.getCollectionFieldType(field);
					toRet = cClass.newInstance();
					list.add(toRet);
					nestedObjectHandler.set(currentPath, toRet);
				} else {
					toRet = nestedObjectHandler.get(currentPath);
				}

				field.setAccessible(false);
				return toRet;
			} catch (Exception e) {
				throw new KarakuRuntimeException(e);
			}

		}

		/**
		 * @param currentClass
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		private Object getNew(Class currentClass) {

			if (List.class.isAssignableFrom(currentClass)) {
				return new ArrayList();
			}
			if (Set.class.isAssignableFrom(currentClass)) {
				return new HashSet();
			}
			throw new UnsupportedOperationException(
					"Can't handle collection of type:" + currentClass.getName());
		}

		/**
		 * @param currentField
		 * @param currentProperty
		 * @return
		 */
		private Object get(Field currentField, Object current) {

			try {
				currentField.setAccessible(true);
				Object toRet = currentField.get(current);
				if (toRet == null) {
					toRet = currentField.getType().newInstance();
				}
				currentField.setAccessible(false);
				return toRet;
			} catch (Exception e) {
				throw new KarakuRuntimeException(e);
			}
		}

		/**
		 * @param currentClass
		 * @param currentProperty
		 * @return
		 */
		@SuppressWarnings(RAWTYPES)
		private Field getField(Class currentClass, String currentProperty) {

			return ReflectionUtils.findField(currentClass, currentProperty);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getMethodName() {

			return "get" + properties[properties.length - 1];
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Method getMethod() {

			return null;
		}

	}

}
