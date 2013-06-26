/*
 * @Where Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.restrictions;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Criterion;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.dao.where.IClause;

/**
 * Clase que representa las restricciones para la busqueda, todo lo que
 * corresponde al parametro Where <br>
 * <i>Notese que se puede utilizar un ejemplo y Clauses para filtrar la
 * consutla</i>
 * 
 * @author Arturo Volpe
 * 
 * @param <T>
 *            Entidad
 * @version 1.1
 * @since 1.0 06/02/2013
 */
public class Where<T> {

	private List<Criterion> criterions;

	private List<IClause> clauses;

	private EntityExample<T> example;

	/**
	 * Setea una entidad para ser utilizada como ejemplo, al hacer esto todas
	 * las columnas del ejemplo seran utilizadas para filtrar la consulta.
	 * 
	 * @param example
	 *            entidad a ser usada como ejemplo
	 */
	public void setExample(T example) {

		this.example = new EntityExample<T>(example);
	}

	/**
	 * Al igual que {@link Where#setExample(EntityExample)}, solo que recibe un
	 * {@link EntityExample}, el cual tiene mas atributos, y se pueden filtrar
	 * los atributos a ser utilizados como filtros
	 * 
	 * @param example
	 *            ejemplo de entidad para usar de filtro
	 */
	public void setExample(EntityExample<T> example) {

		this.example = example;
	}

	/**
	 * Retorna el {@link EntityExample} que se utiliza actualemnte para filtrar
	 * la consulta.
	 * 
	 * @return {@link EntityExample}
	 */
	public EntityExample<T> getExample() {

		return example;
	}

	/**
	 * Utilize {@link Where#addClause(Clause)} y {@link Clauses} para generar
	 * restricciones
	 * 
	 * @param crit
	 */
	@Deprecated
	public void addRestriction(Criterion crit) {

		if (criterions == null) {
			criterions = new ArrayList<Criterion>();
		}
		criterions.add(crit);
	}

	/**
	 * Agrega una restriccion a la consulta, estos se construyen con la clase
	 * {@link Clauses} y se pueden agregar tantos como se desean
	 * 
	 * @param clause
	 * @return this
	 */
	public Where<T> addClause(IClause clause) {

		if (criterions == null) {
			criterions = new ArrayList<Criterion>(1);
		}
		if (clauses == null) {
			clauses = new ArrayList<IClause>(1);
		}
		criterions.add(clause.getCriterion());
		clauses.add(clause);
		return this;
	}

	/**
	 * Retorna la lista de criterios que se utilizan actualemnte en la consulta
	 * 
	 * @return Criterias utilizadas
	 * @deprecated Use {@link Where#getClauses()} para mantener la independencia
	 *             del motor
	 */
	@Deprecated
	public List<Criterion> getCriterions() {

		return criterions;
	}

	/**
	 * Retorna la lista de {@link Clause} que se utiliza actualmente para
	 * filtrar la consulta
	 * 
	 * @return {@link Clause}'s utilizados
	 */
	public List<IClause> getClauses() {

		return clauses;
	}
}
