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
package py.una.pol.karaku.dao.search;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericCollectionTypeResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clause;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.dao.where.DateClauses;
import py.una.pol.karaku.dao.where.MatchMode;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.exception.NotDisplayNameException;
import py.una.pol.karaku.log.Log;

/**
 * Componente que sirve de ayuda al realizar búsquedas dinámicas sobre objetos.
 * Esta clase es útil cuando la utilizamos en ambientes genéricos, donde la
 * información de los objetos, atributos y sus relaciones no esta bien definida.
 * <p>
 * Utiliza reflexión y una nomenclatura de atributos separados por
 * <code>.</code> (similar al ELExpression) para navegar a través de relaciones
 * y identificar los atributos específicos.
 * </p>
 * <p>
 * Utiliza la información del atributo para determinar cual es el mejor filtro a
 * aplicar en las distintas situaciones, los tipos soportados actualmente son:
 * <ul>
 * <li> {@link String}: utiliza una búsqueda por <i>like</i></li>
 * <li> {@link Number}: utiliza una búsqueda por <i>like</i>, todas las clases
 * que heredan de number se buscan bajo los mismos criterios.</li>
 * <li> {@link Date}: utiliza una búsqueda por rango, donde la primera fecha, es
 * la fecha pasada en formato
 * {@link py.una.pol.karaku.util.FormatProvider#DATE_FORMAT} o
 * {@link py.una.pol.karaku.util.FormatProvider#DATETIME_SHORT_FORMAT}, utilizando
 * el componente auxiliar {@link DateClauses}.</li>
 * <li>Si se detecta una {@link Collection} en el path a un atributo, se utiliza
 * un {@link py.una.pol.karaku.dao.where.ILike} como técnica de mejor esfuerzo, no
 * se garantiza que el tipo de búsqueda es el correcto. Si la {@link Collection}
 * que se encuentra es una interfaz, entones <b>SI</b> se puede obtener su tipo
 * genérico.</li>
 * </ul>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 19, 2013
 * @see #getClause(Class, String, String)
 */
@Component
public class SearchHelper {

	@Log
	private Logger log;

	@Autowired
	private DateClauses dc;

	/**
	 * Método auxiliar a {@link #getClause(Class, String, String)} que crea un
	 * nuevo {@link Where} y lo retorna con la {@link Clause} agregada.
	 * 
	 * @param root
	 *            raíz de la búsqueda el atributo, no debe ser <code>null</code>
	 * @param property
	 *            cadena separada por <code>.</code> (puntos) que sirve para
	 *            recorrer a través del objeto y obtener la propiedad deseada.
	 * @param value
	 *            valor con el cual se desea comparar, dependiendo de la
	 *            propiedad, cada valor es tratado por separado.
	 * @return {@link Where} nuevo, con la cláusula correspondiente agregada.
	 * @see #getClause(Class, String, String)
	 */
	public <T> Where<T> buildWhere(@NotNull Class<T> root,
			@NotNull String property, String value) {

		return new Where<T>().addClause(this.getClause(root, property, value));
	}

	/**
	 * Retorna una cláusula para realizar una búsqueda por el atributo pasado.
	 * <p>
	 * Notar que la intención de este método es ser utilizados en ámbitos
	 * dinámicos, donde la información no se dispone, por ejemplo al momento de
	 * crear filtros automáticos.
	 * </p>
	 * <p>
	 * Ver la descripción de esta clase ( {@link SearchHelper} )
	 * </p>
	 * 
	 * <h3>Ejemplos</h3> Estos ejemplos utilizan las entidades de Test, y son
	 * extraidos de los Test, para verlos en funcionamiento, ir a los Test.
	 * 
	 * <pre>
	 * getClause(TestEntity.class, &quot;testChild.description&quot;, &quot;COSTO&quot;)
	 * </pre>
	 * 
	 * Es lo mismo que utilizar:
	 * 
	 * <pre>
	 * Clauses.ilike("testChild.description", "COSTO", {@link MatchMode#CONTAIN})
	 * </pre>
	 * 
	 * Cambiando solamente el path, el {@link Clause} generado cambia
	 * drásticamente:
	 * 
	 * <pre>
	 * getClause(TestEntity.class, &quot;testChild.fecha&quot;, &quot;13-11-2013&quot;)
	 * </pre>
	 * 
	 * Es lo mismo que utilizar:
	 * 
	 * <pre>
	 * dateClauses.between("testChild.fecha", "13-11-2013", "13-11-2013", <code>true</code>);
	 * </pre>
	 * 
	 * @param root
	 *            raíz de la búsqueda el atributo, no debe ser <code>null</code>
	 * @param property
	 *            cadena separada por <code>.</code> (punto) que sirve para
	 *            recorrer a través del objeto y obtener la propiedad deseada.
	 * @param value
	 *            valor con el cual se desea comparar, dependiendo de la
	 *            propiedad, cada valor es tratado por separado.
	 * @return {@link Clause} correspondiente a la propiedad.
	 */
	public <T> Clause getClause(@Nonnull Class<T> root,
			@Nonnull String property, String value) {

		if ("".equals(property)) {
			throw new IllegalArgumentException("Property can't be empty");
		}

		FieldInfo fi = getFieldInfo(root, property);
		if (fi.isCollection()) {
			// Si es una colección, se utiliza una técnica de mejor
			// esfuerzo.
			return Clauses.iLike(property, value, MatchMode.CONTAIN);
		}
		return this.getFilter(fi, property, value);
	}

	/**
	 * Retorna la información relacionada a un Field definido por una propiedad
	 * (con un formato separado por puntos).
	 * <p>
	 * Por ejemplo, si invocamos de la siguiente manera:
	 * 
	 * <pre>
	 * 	class Pais {
	 * 		...
	 * 		Set{@literal <}Departamento> departamentos;
	 * 
	 * 		...
	 * 	}
	 * 
	 * 	class Departamento {
	 * 		...
	 * 		Set{@literal <}Ciudad> ciudades;
	 * 
	 * 		Set etnias;
	 * 
	 * 		Pais pais;
	 * 		...
	 * 	}
	 * 
	 * 	class Ciudad {
	 * 		...
	 * 
	 * 		Departamento departamento;
	 * 		...
	 * 	}
	 * 
	 * 1. Y invocamos de la siguiente manera:
	 * 
	 * 	fi = getFieldInfo(Ciudad.class, "departamento.pais");
	 * 	fi.getField() ==> Pais.class
	 * 	fi.isCollection() == > <code>false</code>
	 * 
	 * 2. El siguiente ejemplo, es cuando se encuentra una {@link Collection}
	 * 
	 * 	fi = getFieldInfo(Ciudad.class, "departamento.etnias");
	 * 	fi.getField() ==> Etnia.class
	 * 	fi.isCollection() == > <code>true</code>
	 * </pre>
	 * 
	 * <p>
	 * TODO ver para meter en una clase de utilidad
	 * </p>
	 * 
	 * @param root
	 *            {@link Class} que sirve de base para buscar la propiedad
	 * @param property
	 *            cadena separada por <code>.</code> (puntos) que sirve para
	 *            recorrer a través del objeto y obtener la propiedad deseada.
	 * @return {@link FieldInfo} con la información posible que se ha
	 *         conseguido.
	 * @throw {@link KarakuRuntimeException} si no encuentra el field o no es
	 *        accesible.
	 */
	public static FieldInfo getFieldInfo(@NotNull Class<?> root,
			@NotNull String property) {

		try {
			String[] splited = property.split(Pattern.quote("."));
			String cProperty;
			Class<?> cClass = root;
			Field toRet = null;
			boolean collectionFound = false;
			for (String element : splited) {
				cProperty = element;
				toRet = ReflectionUtils.findField(cClass, cProperty);
				if (toRet == null) {
					throw new KarakuRuntimeException("Field: " + cProperty
							+ " not found. (Full path: " + property + ")");
				}
				cClass = toRet.getType();
				// Si tenemos una lista, buscar el tipo de la lista.
				if (Collection.class.isAssignableFrom(cClass)) {
					cClass = GenericCollectionTypeResolver
							.getCollectionFieldType(toRet);
				}
				// TODO add expansion if found a @Display in the last field
				// Ejemplo: pais.departamento, puede seguir siendo explotado
				// si departamento tiene un DisplayName
			}
			return new FieldInfo(toRet, collectionFound);
		} catch (SecurityException e) {
			throw new KarakuRuntimeException("Field not accessible: "
					+ property + " in class " + root.getSimpleName(), e);
		}
	}

	/**
	 * Retorna el filtro simple.
	 * 
	 * @param where
	 *            {@link Where}, no puede ser <code>null</code>
	 * @param fi
	 *            propiedad de la que se saca la meta-información
	 * @param property
	 *            path al field
	 * @param value
	 *            valor con el que se compara
	 * @return {@link Where} con la opción agregada
	 */
	private <T> Clause getFilter(FieldInfo fi, String property, String value) {

		if (fi.collection) {
			return this.handleCollection(property, value);
		}
		try {
			if (String.class.isAssignableFrom(fi.field.getType())) {
				return this.handleString(property, value);
			}
			if (Number.class.isAssignableFrom(fi.field.getType())) {
				return this.handleNumber(property, value);
			}
			if (Date.class.isAssignableFrom(fi.field.getType())) {
				return this.handleDate(property, value);
			}
			throw new NotDisplayNameException();
		} catch (Exception e) {
			this.log.error("Error building clause for: {}", e, property);
		}
		return null;
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	private <T> Clause handleNumber(String property, String value) {

		return Clauses.numberLike(property, value);
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	private <T> Clause handleString(String property, String value) {

		return Clauses.iLike(property, value);
	}

	private <T> Clause handleCollection(String property, String value) {

		return Clauses.iLike(property, value);
	}

	/**
	 * @param property
	 * @param value
	 * @return
	 */
	private <T> Clause handleDate(String property, String value) {

		return this.dc.between(property, value, value, true);
	}

	/**
	 * Clase auxiliar para {@link #getField()} que se utiliza para obtener
	 * información valiosa a partir de un {@link Field}.
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Sep 26, 2013
	 * 
	 */
	public static class FieldInfo {

		private Field field;

		private boolean collection;

		/**
		 * Crea un nuevo field, que no es una colección con el {@link Field}
		 * dado.
		 * 
		 * @param f
		 *            {@link Field} del cual se presenta la información
		 */
		public FieldInfo(Field f) {

			this(f, false);
		}

		/**
		 * Crea un nuevo field, que es o no una colección (depende de
		 * {@link #isCollection()} con el {@link Field} dado.
		 * 
		 * @param f
		 *            {@link Field} del cual se presenta la información
		 * @param isCollection
		 *            define si el mismo es una colección o no.
		 */
		public FieldInfo(Field f, boolean isCollection) {

			this.field = f;
			this.collection = isCollection;
		}

		/**
		 * Retorna el {@link Field} Asociado a esta información. En el caso de
		 * que {@link #isCollection()} sea <code>true</code>, este método
		 * retorna el primer {@link Field} que es asignable desde una
		 * {@link Collection}.
		 * 
		 * @return field asociado.
		 */
		public Field getField() {

			return this.field;
		}

		/**
		 * Verifica si la información se asocia a una {@link Collection}.
		 * 
		 * @return isCollection <code>false</code> si no es una colección y
		 *         <code>true</code> en caso contrario.
		 */
		public boolean isCollection() {

			return this.collection;
		}
	}
}
