/*
 * @Where Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.restrictions;

import static py.una.med.base.util.Checker.notNull;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;
import py.una.med.base.dao.util.EntityExample;
import py.una.med.base.dao.where.Clause;

/**
 * Clase que representa las restricciones para la busqueda.
 * 
 * <p>
 * Todo lo que corresponde al parámetro Where. <i>Notese que se puede utilizar
 * un ejemplo y Clauses para filtrar la Consulta</i>
 * </p>
 * 
 * @author Arturo Volpe
 * @param <T>
 *            Entidad
 * @version 1.1
 * @since 1.0 06/02/2013
 */
public class Where<T> {

	/**
	 * Construye un nuevo {@link Where} y lo retorna, la intención de este
	 * método, es facilitar la craeción de consultas en una sintaxis Fluent-like
	 * 
	 * @return {@link Where}, nunca <code>null</code>
	 */
	public static <T> Where<T> get() {

		return new Where<T>();
	}

	private List<Criterion> criterions;

	private List<Clause> clauses;

	private EntityExample<T> example;

	private boolean distinct;

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

		return this.example;
	}

	/**
	 * Utilize {@link Where#addClause(Clause)} y
	 * {@link py.una.med.base.dao.where.Clauses} para generar restricciones.
	 * 
	 * @param crit
	 *            para agregar.
	 * @deprecated utilizar {@link #addClause(Clause...)}
	 */
	@Deprecated
	public void addRestriction(Criterion crit) {

		if (this.criterions == null) {
			this.criterions = new ArrayList<Criterion>();
		}
		this.criterions.add(crit);
	}

	/**
	 * Agrega una restricción a la consulta, estos se construyen con la clase
	 * {@link py.una.med.base.dao.where.Clauses} y se pueden agregar tantos como
	 * se desean. Todas las clausulas que se agregan por este método se añaden
	 * como una condición <code>and</code>.
	 * <p>
	 * Si se desea que se agregen como <code>or</code>, ver
	 * {@link py.una.med.base.dao.where.Or}
	 * </p>
	 * 
	 * @param clauses
	 *            {@link Clause} a ser añadidas, ningún elemento puede ser
	 *            <code>null</code>
	 * @return this
	 */
	public Where<T> addClause(Clause ... clauses) {

		if (this.criterions == null) {
			this.criterions = new ArrayList<Criterion>(1);
		}
		for (Clause clause : clauses) {
			this.getClauses().add(clause);
		}
		return this;
	}

	/**
	 * Retorna la lista de criterios que se utilizan actualmente en la consulta
	 * 
	 * @return Criterias utilizadas
	 * @deprecated Use {@link Where#getClauses()} para mantener la independencia
	 *             del motor
	 */
	@Deprecated
	public List<Criterion> getCriterions() {

		return this.criterions;
	}

	/**
	 * Retorna la lista de {@link Clause} que se utiliza actualmente para
	 * filtrar la consulta
	 * 
	 * @return {@link Clause}'s utilizados
	 */
	@Nonnull
	public List<Clause> getClauses() {

		if (clauses == null) {
			clauses = new ArrayList<Clause>(1);
		}
		return notNull(clauses);
	}

	/**
	 * Si retorna <code>true</code>, todos los resultados serán diferentes unos
	 * de otros.
	 * <p>
	 * Esto ocurre cuando se hacen joins del tipo:
	 * 
	 * <pre>
	 * select * from Pais p
	 * 	join p.departamento d
	 * 	join d.ciudad c
	 * 
	 * where c.descripcion like 'San%'
	 * 
	 * </pre>
	 * 
	 * Si existe un país con varias ciudades cuyo nombre empiece con
	 * <code>San</code>, entonces ese país será retornado varias veces.
	 * </p>
	 * 
	 * <p>
	 * Este valor es por defecto <code>false</code> (para mantener
	 * compatibilidad), si se desea que los resultados sean únicos, utilizar
	 * {@link #makeDistinct()}
	 * </p>
	 * 
	 * @return distinct <code>true</code> si no puede repetir,
	 *         <code>false</code> si los resultados son distintos.
	 */
	public boolean isDistinct() {

		return this.distinct;
	}

	/**
	 * Modifica la consulta Select, para que retorne solo los valores no
	 * repetidos.
	 * <p>
	 * Convierte la consulta:
	 * 
	 * <pre>
	 * select * from Pais p join p.departamento d
	 * 	join d.ciudad c  where c.descripcion like 'San%'
	 * </pre>
	 * 
	 * en:
	 * 
	 * <pre>
	 * select distinct(*) from Pais p join p.departamento d
	 * 	join d.ciudad c  where c.descripcion like 'San%'
	 * </pre>
	 * 
	 * 
	 * </p>
	 * 
	 * @see #isDistinct()
	 * @return this
	 */
	public Where<T> makeDistinct() {

		this.distinct = true;
		return this;
	}
}
