package py.una.med.base.dao.helper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.Restrictions;
import py.una.med.base.dao.where.Ge;

/**
 * 
 * @author Uriel Gonzalez
 * 
 */
public class GeExpressionHelper {

	private GeExpressionHelper() {

		// No-op
	}

	public static Map<String, String> applyNestedCriteria(Criteria criteria,
			Ge ge, Map<String, String> aliases) {

		criteria.add(getCriterion(criteria, ge, aliases));
		return aliases;
	}

	/**
	 * Retorna la restriccion de esta Clausula, si hace falta agrega los alias
	 * en el mapa de alias y en la consulta, no agrega efectivamente la
	 * restriccion a la consulta
	 * 
	 * @param criteria
	 * @param le
	 * @param aliases
	 * @return
	 */
	public static Criterion getCriterion(Criteria criteria, Ge le,
			Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, le.getPath(),
				aliases);
		if (aliasWithProperty == null) {
			return le.getCriterion();
		}
		return Restrictions.ge(aliasWithProperty, le.getValue());

	}

	/**
	 * Crea un nuevo alias para la expresion pasada como parametro, si se genera
	 * un nuevo alias retorna con el parametro agregado, retornando una
	 * expresion valida para agregar a un where
	 * 
	 * @param criteria
	 * @param property
	 * @param aliases
	 * @return
	 */
	public static String configureAlias(Criteria criteria, String property,
			final Map<String, String> aliases) {

		Map<String, String> aliass = aliases;
		if (!property.contains(".")) {

			return null;
		}

		String[] partes = property.split("\\.");
		if (partes.length > 2) {
			throw new IllegalArgumentException("Pruebas con dos partes nomas!");
		}

		if (aliass == null) {
			aliass = new HashMap<String, String>();
		}

		String alias = aliass.get(partes[0]);
		if (alias == null) {
			alias = partes[0] + "_";
			aliass.put(partes[0], alias);
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
