package py.una.med.base.dao.helper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class LikeExpressionHelper {

	private LikeExpressionHelper() {
		// No-op
	}
	
	public static Map<String, String> applyNestedCriteria(Criteria criteria,
			LikeExpression likeExpression, Map<String, String> aliases) {

		criteria.add(getCriterion(criteria, likeExpression, aliases));
		return aliases;
	}

	/**
	 * Retorna la restriccion de esta Clausula, si hace falta agrega los alias
	 * en el mapa de alias y en la consulta, no agrega efectivamente la
	 * restriccion a la consulta
	 * 
	 * @param criteria
	 * @param likeExpression
	 * @param aliases
	 * @return
	 */
	public static Criterion getCriterion(Criteria criteria,
			LikeExpression likeExpression, Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, likeExpression,
				aliases);
		if (aliasWithProperty == null) {
			return likeExpression;
		}
		return Restrictions.ilike(aliasWithProperty, getValue(likeExpression)
				.toString(), MatchMode.ANYWHERE);

	}

	/**
	 * Crea un nuevo alias para la exprseion pasada como parametro, si se genera
	 * un nuevo alias retorna con el parametro agregado, retornando una
	 * expresion valida para agregar a un where
	 * 
	 * @param criteria
	 * @param likeExpression
	 * @param aliases
	 * @return
	 */
	public static String configureAlias(Criteria criteria,
			LikeExpression likeExpression, Map<String, String> aliases) {

		String property = getPropety(likeExpression);
		if (!property.contains(".")) {

			return null;
		}

		String[] partes = property.split("\\.");
		if (partes.length > 2) {
			throw new IllegalArgumentException("Pruebas con dos partes nomas!");
		}

		if (aliases == null) {
			aliases = new HashMap<String, String>();
		}

		String alias = aliases.get(partes[0]);
		if (alias == null) {
			alias = partes[0] + "_";
			aliases.put(partes[0], alias);
			criteria.createAlias(partes[0], alias);
		}
		return alias + "." + partes[1];
	}

	public static Object getValue(LikeExpression likeExpression) {

		try {
			return getField("value").get(likeExpression);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Imposible obtener field 'value'");
		}
	}

	public static String getPropety(LikeExpression likeExpression) {

		try {
			return (String) getField("propertyName").get(likeExpression);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Imposible obtener field 'propertyName'");
		}
	}

	private static Field getField(String nombre) {

		try {
			Field f = LikeExpression.class.getDeclaredField(nombre);
			f.setAccessible(true);
			return f;
		} catch (Exception e) {
			throw new IllegalArgumentException("Imposible obtener field");
		}
	}
}
