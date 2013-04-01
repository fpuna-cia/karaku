package py.una.med.base.dao.helper;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import py.una.med.base.dao.restrictions.NumberLike;

/**
 * Helper que se encarga de crear los alias necesarios para que se pueda navegar
 * en una relacion entre joins
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1 08/02/2013
 * 
 */
public class NumberLikerExpressionHelper {

	public static Map<String, String> applyNestedCriteria(Criteria criteria,
			NumberLike likeExpression, Map<String, String> aliases) {

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
			NumberLike likeExpression, Map<String, String> aliases) {

		String aliasWithProperty = configureAlias(criteria, likeExpression,
				aliases);
		if (aliasWithProperty == null) {
			return likeExpression;
		}
		likeExpression.setPropiedad(aliasWithProperty);
		return likeExpression;

	}

	/**
	 * Crea un nuevo alias para la exprseion pasada como parametro, si se genera
	 * un nuevo alias retorna con el parametro agregado, retornando una
	 * expresion valida para agregar a un where
	 * 
	 * @param criteria
	 * @param numberlike
	 * @param aliases
	 * @return
	 */
	public static String configureAlias(Criteria criteria,
			NumberLike numberlike, Map<String, String> aliases) {

		String property = numberlike.getPropiedad();
		if (!property.contains(".")) {

			return null;
		}

		String[] partes = property.split("\\.");
		if (partes.length > 2) {
			throw new RuntimeException("Pruebas con dos partes nomas!");
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

}
