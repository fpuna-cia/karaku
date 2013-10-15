/*
 * @KarakuAliasToBeanTransformer.java 1.0 Oct 13, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.dao.select;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.DistinctResultTransformer;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.exception.KarakuRuntimeException;

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

	/**
	 *
	 */
	private static final String RAWTYPES = "rawtypes";
	private final Class<T> resultClass;
	private boolean isInitialized;
	private String[] aliases;
	private Setter[] setters;
	private final boolean distinct;

	/**
	 * @param resultClass
	 */
	public KarakuAliasToBeanTransformer(Class<T> resultClass, boolean distinct) {

		super(resultClass);
		this.resultClass = resultClass;
		this.distinct = distinct;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 8185588266275147013L;

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {

		T toRet;
		try {
			if (!isInitialized) {
				initialize(aliases);
			} else {
				check(aliases);
			}

			toRet = resultClass.newInstance();

			for (int i = 0; i < aliases.length; i++) {
				// Diferencia con AliasToBeanResultTransformer

				// -------------------------------------------
				if (setters[i] != null) {
					setters[i].set(toRet, tuple[i], null);
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName(), e);
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: "
					+ resultClass.getName(), e);
		}

		return toRet;
	}

	@SuppressWarnings(RAWTYPES)
	@Override
	public List transformList(List list) {

		if (distinct) {
			return DistinctResultTransformer.INSTANCE.transformList(list);
		}
		return list;
	}

	private void initialize(String[] aliases) {

		PropertyAccessor propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] {
						PropertyAccessorFactory.getPropertyAccessor(
								resultClass, null),
						PropertyAccessorFactory.getPropertyAccessor("field") });
		this.aliases = new String[aliases.length];
		setters = new Setter[aliases.length];
		for (int i = 0; i < aliases.length; i++) {
			String alias = aliases[i];
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

	private void check(String[] aliases) {

		if (!Arrays.equals(aliases, this.aliases)) {
			throw new IllegalStateException(
					"aliases are different from what is cached; aliases="
							+ Arrays.asList(aliases) + " cached="
							+ Arrays.asList(this.aliases));
		}
	}

	private static class NestedSetter implements Setter {

		/**
		 *
		 */
		private static final long serialVersionUID = -3494109024315529035L;
		private final String path;
		private final Class<?> root;

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
			path = property;
		}

		/**
		 * <p>
		 * TODO cachear todo!
		 * </p>
		 * {@inheritDoc}
		 */
		@SuppressWarnings(RAWTYPES)
		@Override
		public void set(Object target, Object value,
				SessionFactoryImplementor factory) {

			String[] properties = path.split("\\.");

			Object current = target;
			Class currentClass = root;
			Field currentField = null;

			/*
			 * Cuando termine este for, en current estara el objeto para setear
			 * la propiedad y en currentclass la clase del mismo, por eso es -2,
			 * no hace falta el ultimo por que ahi sucede el trabajo real.
			 */
			for (int i = 0; i < (properties.length - 1); i++) {

				String currentProperty = properties[i];
				// obtengo el field

				currentField = getField(currentClass, currentProperty);

				// con el field obtengo el tipo
				currentClass = currentField.getType();

				// si es una lista veo que hacer
				if (List.class.isAssignableFrom(currentClass)) {
					// si es una coleccion ver que hacer con current (setearle
					// una lista o algo así)
					current = handleList(currentField, current);
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

		@SuppressWarnings({ RAWTYPES, "unchecked" })
		private Object handleList(Field field, Object current) {

			try {
				field.setAccessible(true);
				Object newList = field.get(current);
				if (newList == null) {
					newList = new ArrayList();
					field.set(current, newList);
				}
				Class cClass = GenericCollectionTypeResolver
						.getCollectionFieldType(field);

				List list = (List) newList;
				Object toRet = cClass.newInstance();
				list.add(toRet);

				field.setAccessible(false);
				return toRet;
			} catch (Exception e) {
				throw new KarakuRuntimeException(e);
			}

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

			String[] properties = path.split("\\.");

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
