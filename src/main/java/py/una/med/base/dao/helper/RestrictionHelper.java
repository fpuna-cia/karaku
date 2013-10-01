package py.una.med.base.dao.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LikeExpression;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.log.Log;

/**
 * Clase que sirve para interceptar restricciones que se agregan a un query, por
 * ejemplo para agregar alias para joins entre columnas, característica no
 * soportada por Hibernate Criteria
 * 
 * @author Arturo Volpe
 * @version 1.1
 * @since 1.0 08/02/2013
 * 
 * @param <T>
 */
@Component
public class RestrictionHelper implements ApplicationContextAware {

	@Log
	private transient Logger log;

	private static Map<Class<?>, BaseClauseHelper<?>> helpers;

	private static void register(BaseClauseHelper<?> newHelper) {

		getHelpers().put(newHelper.getClassOfClause(), newHelper);
	}

	/**
	 * @return helpers
	 */
	private static Map<Class<?>, BaseClauseHelper<?>> getHelpers() {

		if (helpers == null) {
			helpers = new HashMap<Class<?>, BaseClauseHelper<?>>();
		}

		return helpers;
	}

	private ApplicationContext applicationContext;

	@SuppressWarnings("rawtypes")
	@PostConstruct
	void postConstruct() {

		Map<String, BaseClauseHelper> help = this.applicationContext
				.getBeansOfType(BaseClauseHelper.class);
		for (Entry<String, BaseClauseHelper> entry : help.entrySet()) {
			register(entry.getValue());
		}
	}

	/**
	 * Agrega todas las restriciones a la criteria, si encuentra expresiones del
	 * tipo {@link LikeExpression}, las modifica para aceptar paths anidados
	 * como "pais.descripcion", los cuales no son soportados nativamente por
	 * 
	 * <p>
	 * Para agregar soporte a mas restricciones, véase
	 * {@link #register(BaseClauseHelper)}
	 * </p>
	 * 
	 * @param where
	 *            filtros que se desea aplicar
	 * @param criteria
	 *            consulta actual
	 * @param alias
	 *            lista acutal de todos los alias que ya fueron agregados.
	 * @return Criteria con los filtro aplicados
	 */
	@SuppressWarnings("deprecation")
	public Criteria applyRestrictions(final Criteria criteria,
			final Where<?> where, final Map<String, String> alias) {

		Map<String, String> aliaz = alias;

		if (aliaz == null) {
			throw new IllegalArgumentException("Alias can't be null");
		}
		if (where == null || where.getCriterions() == null
				&& where.getClauses() == null) {
			return criteria;
		}
		if (where.getClauses() != null) {
			List<Criterion> criterions = this.getCriterions(where.getClauses(),
					criteria, aliaz);
			for (Criterion c : criterions) {
				criteria.add(c);
			}
		}
		return criteria;
	}

	/**
	 * Retorna la lista de {@link Criterion}, sin realmente modificar la
	 * consulta (solamente los alias listados se agregan a la consulta).
	 * 
	 * @param clauses
	 *            lista de {@link Clause}, not null.
	 * @param criteria
	 *            criteria para agregar alias
	 * @param alias
	 *            mapa de alias actuales
	 * @return {@link List} de criteriones, nunca null.
	 */
	public List<Criterion> getCriterions(
			@NotNull @Size(min = 1) List<Clause> clauses,
			@NotNull Criteria criteria, @NotNull Map<String, String> alias) {

		ArrayList<Criterion> criterions = new ArrayList<Criterion>(
				clauses.size());
		for (Clause cr : clauses) {

			criterions.add(this.getCriterion(cr, criteria, alias));
		}
		return criterions;
	}

	/**
	 * Retorna el {@link Criterion}, sin realmente modificar la consulta
	 * (solamente los alias listados se agregan a la consulta).
	 * 
	 * @param clause
	 *            {@link Clause}, not null.
	 * @param criteria
	 *            criteria para agregar alias
	 * @param alias
	 *            mapa de alias actuales
	 * @return {@link List} de criteriones, nunca null.
	 */
	@SuppressWarnings("deprecation")
	public Criterion getCriterion(@NotNull Clause clause,
			@NotNull Criteria criteria, @NotNull Map<String, String> alias) {

		BaseClauseHelper<?> helper = getHelpers().get(clause.getClass());

		if (helper == null) {
			this.log.info("Helper not found for: {}", clause.getClass());
			return clause.getCriterion();
		} else {
			return getHelpers().get(clause.getClass()).getCriterion(criteria,
					clause, alias, true);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
	 *      (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;
	}
}
