package py.una.med.base.dao.helper;

import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.criterion.LikeExpression;
import py.una.med.base.dao.restrictions.NumberLike;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.Not;
import py.una.med.base.dao.where.Or;

/**
 * Clase qeu sirve para interceptar restricciones que se agregan a un query, por
 * ejemplo para agregar alias para joins entre columnas, caracteristica no
 * soportada por Hibernate Criteria
 * 
 * @author Arturo Volpe
 * @version 1.1
 * @since 1.0 08/02/2013
 * 
 * @param <T>
 */
public class RestrictionHelper<T> {

	private Map<String, String> alias;

	/**
	 * Agrega todas las restriciones a la criteria, si encuentra expresiones del
	 * tipo {@link LikeExpression}, las modifica para aceptar paths anidados
	 * como "pais.descripcion", los cuales no son soportados nativamente por
	 * hibernate. <br>
	 * <ol>
	 * <li>{@link LikeExpression}</li>
	 * <li>{@link NumberLike}</li>
	 * </ol>
	 * 
	 * @param where
	 *            filtros que se desea apilcar
	 * @param criteria
	 *            consulta actual
	 * @return Criteria con los filtro aplicados
	 */
	@SuppressWarnings("deprecation")
	public Criteria applyRestrictions(final Criteria criteria,
			final Where<T> where, final Map<String, String> alias) {

		Map<String, String> aliaz = alias;

		if (aliaz == null) {
			throw new IllegalArgumentException("Alias can't be null");
		}
		if ((where == null)
				|| (where.getCriterions() == null && where.getClauses() == null)) {
			return criteria;
		}
		if (where.getClauses() != null) {
			for (Clause cr : where.getClauses()) {
				if (cr.getCriterion() == null) {
					continue;
				}
				if (cr.getCriterion().getClass().equals(LikeExpression.class)) {
					LikeExpressionHelper.applyNestedCriteria(criteria,
							(LikeExpression) cr.getCriterion(), aliaz);
				} else if (cr.getCriterion().getClass()
						.equals(NumberLike.class)) {
					NumberLikerExpressionHelper.applyNestedCriteria(criteria,
							(NumberLike) cr.getCriterion(), aliaz);
				} else if (cr.getClass().equals(Or.class)) {
					aliaz = OrExpressionHelper.applyNestedCriteria(criteria,
							(Or) cr, aliaz);
				} else if (cr.getClass().equals(Not.class)) {
					aliaz = NotExpressionHelper.applyNestedCriteria(criteria,
							(Not) cr, aliaz);
				} else {
					criteria.add(cr.getCriterion());
				}
			}
		}
		// if (where.getCriterions() != null) {
		// for (Criterion c : where.getCriterions()) {
		// criteria.add(c);
		// }
		// }
		return criteria;
	}

}
