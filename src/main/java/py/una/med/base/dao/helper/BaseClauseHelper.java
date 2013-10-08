/*
 * @BaseClauseHelper.java 1.0 Sep 10, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dao.helper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 * Clase que sirve de base para los componentes que se utilizan para mapear una
 * {@link Clause} a su correspondiente {@link Criterion} de hibernate.
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
public abstract class BaseClauseHelper<T extends Clause> {

	/**
	 * Cadena que se utiliza para separar los alias generados por esta clase.
	 */
	public static final String ALIAS_SEPARATOR = "_";

	/**
	 * Cadena que se utiliza para separar las propiedades, esta debe ser igual a
	 * la utilizada por hibernate.
	 */
	private static final String PROPERTY_SEPARATOR = ".";

	/**
	 * Expresión regular que se utiliza para partir la propiedad
	 * 
	 * @see #PROPERTY_SEPARATOR
	 */
	private static final String PROPERTY_SEPARATOR_REGEX = "\\.";

	private Class<T> clazz;

	/**
	 * Retorna la restricción de esta Cláusula, si hace falta agrega los alias
	 * en el mapa de alias y en la consulta, no agrega efectivamente la
	 * restricción a la consulta
	 * 
	 * @param criteria
	 *            solamente para agregar alias, no se modifica el Where de esta
	 *            criteria.
	 * @param clause
	 *            clausula a ser ayudada.
	 * @param aliases
	 *            lista de alias
	 * @return {@link Criterion} formado con los alias.
	 */
	public abstract Criterion getCriterion(Criteria criteria, T clause,
			Map<String, String> aliases);

	/**
	 * Retorna la restricción de esta Cláusula, si hace falta agrega los alias
	 * en el mapa de alias y en la consulta, no agrega efectivamente la
	 * restricción a la consulta
	 * 
	 * @param criteria
	 *            solamente para agregar alias, no se modifica el Where de esta
	 *            criteria.
	 * @param likeExpression
	 *            expresión a ser ayudada.
	 * @param aliases
	 *            lista de alias
	 * @param typeSafe
	 *            bandera que indica que es sano realizar al conversion.
	 * @return {@link Criterion} formado con los alias.
	 */
	@SuppressWarnings("unchecked")
	Criterion getCriterion(Criteria criteria, Clause clause,
			Map<String, String> aliases, boolean typeSafe) {

		return getCriterion(criteria, (T) clause, aliases);
	}

	/**
	 * Crea un nuevo alias para la expresión pasada como parámetro, si se genera
	 * un nuevo alias retorna con el parámetro agregado, retornando una
	 * expresión valida para agregar a un <code>where</code>.
	 * <p>
	 * Si criteria no es <code>null</code>, agrega automáticamente los alias al
	 * criteria.
	 * </p>
	 * 
	 * @param criteria
	 *            {@link Criteria} que se esta construyendo. Puede ser
	 *            <code>null</code>, en tal caso no se agregan los alias.
	 * @param property
	 *            nombre de la propiedad, debe ser un camino válido en HQL
	 * @param aliases
	 *            lista alias que ya fueron agregados.
	 * @return alias configurado (y agregado si {@link Criteria} no es
	 *         <code>null</code>);
	 */
	public static String configureAlias(@Nullable Criteria criteria,
			@NotNull String property, @NotNull final Map<String, String> aliases) {

		// TODO mover a algo parecido a un AliasHelper
		if (aliases == null) {
			throw new IllegalArgumentException("Aliases can not be null");
		}
		if (!property.contains(PROPERTY_SEPARATOR)) {

			return property;
		}

		String[] partes = property.split(PROPERTY_SEPARATOR_REGEX);

		if (partes.length == 1) {
			return partes[0];
		}

		StringBuilder sbAlias;
		if (aliases.get(partes[0]) == null) {
			sbAlias = new StringBuilder(partes[0]).append(ALIAS_SEPARATOR);
			String alias = sbAlias.toString();
			aliases.put(partes[0], alias);
			addAliasToCriteria(alias, partes[0], criteria);
		} else {
			sbAlias = new StringBuilder(aliases.get(partes[0]));
		}

		for (int i = 1; i < partes.length - 1; i++) {

			StringBuilder pathBuilder = new StringBuilder(sbAlias.toString())
					.append(PROPERTY_SEPARATOR).append(partes[i]);
			sbAlias.append(partes[i]).append(ALIAS_SEPARATOR);
			String path = pathBuilder.toString();
			String currentAlias = sbAlias.toString();
			if (!aliases.containsKey(path)) {
				addAliasToCriteria(currentAlias, path, criteria);
				aliases.put(path, currentAlias);
			}
		}

		sbAlias.append(PROPERTY_SEPARATOR).append(partes[partes.length - 1]);
		return sbAlias.toString();
	}

	/**
	 * Método de conveniencia que retorna el field determinado por el nombre del
	 * {@link Clause}.
	 * 
	 * @param nombre
	 * @return {@link Field} ya accesible, nunca <code>null</code>
	 */
	Field getField(String nombre) {

		try {
			Field f = getClassOfClause().getDeclaredField(nombre);
			f.setAccessible(true);
			return f;
		} catch (Exception e) {
			throw new KarakuRuntimeException("Imposible obtener field", e);
		}
	}

	/**
	 * Busca en la jerarquía de esta clase la heredera directa de
	 * {@link BaseClauseHelper} y retorna la clase parametrizada.
	 * 
	 * @return clase que hereda {@link Clause} al cual este helper auxilia.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getClassOfClause() {

		if (clazz == null) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			clazz = (Class<T>) type.getActualTypeArguments()[0];
		}
		return clazz;
	}

	private static void addAliasToCriteria(String alias, String path,
			Criteria criteria) {

		if (criteria == null) {
			return;
		}
		criteria.createAlias(path, alias);
	}
}
