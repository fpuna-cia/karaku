package py.una.med.base.dao.where;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.MatchMode;

/**
 * Clase que representa una condición de Where para hacer búsqueda en similitud
 * entre numeros, por ejemplo, si buscamos <code>787</code> entre los numeros,
 * convertirá a cadena la columna y hará un like convencional.
 * 
 * <p>
 * <h3>Ejemplos de uso:</h3>
 * 
 * <pre>
 * new NumberLike("codigoIso", "12", {@link MatchMode#CONTAIN})
 * </pre>
 * 
 * Generará la siguiente cláusula:
 * 
 * <pre>
 * ::codigoIso ilike %12%
 * </pre>
 * 
 * Notese que para postgresql, si agregamos <code>::</code> antes de una
 * columna, la convertirá automáticamente a String.
 * </p>
 * 
 * TODO ver para agregar comparación con decimales.
 * 
 * @author Arturo Volpe
 * @version 1.0
 * @since 1.0 08/02/2013
 * 
 */
public class NumberLike implements Criterion, Clause {

	private String propiedad;

	private String valor;

	private MatchMode matchMode;

	/**
	 * Define la propiedad por la cual se buscara, la propiedad, es el path a un
	 * atributo de una entidad.
	 * 
	 * @param propiedad
	 */
	public void setPropiedad(String propiedad) {

		this.propiedad = propiedad;
	}

	/**
	 * Retorna una cadena que representa el atributo por el cual se buscará
	 * 
	 * @return path al atributo
	 */
	public String getPropiedad() {

		return propiedad;
	}

	/**
	 * Define una nueva cláusula del tipo like entre números, la cláusula se
	 * lee: <i>todos los los registros que en <b>propiedad</b> contengan al
	 * número <b>valor</b>, se dice que la propiedad <code>x</code> contiene al
	 * numero <code>y</code>, sí el <b>valor</b> es una subcadena* de la
	 * propiedad</b> convertida a cadena</i>
	 * <p>
	 * 
	 * <p>
	 * *: esto depende del {@link MatchMode}
	 * </p>
	 * 
	 * @param propiedad
	 *            atributo a ser buscado
	 * @param valor
	 *            numero con el cual se comparará
	 * @param matchMode
	 *            por el cual se buscará la subcadena
	 */
	public NumberLike(String propiedad, String valor, MatchMode matchMode) {

		super();
		this.matchMode = matchMode;
		this.propiedad = propiedad;
		this.valor = valor;
	}

	/**
	 * Define una nueva cláusula del tipo like entre números, la cláusula se
	 * lee: <i>todos los los registros que en <b>propiedad</b> contengan al
	 * número <b>valor</b>, se dice que la propiedad <code>x</code> contiene al
	 * numero <code>y</code>, sí el <b>valor</b> es una subcadena de la
	 * propiedad</b> convertida a cadena</i>
	 * <p>
	 * 
	 * 
	 * @param propiedad
	 *            atributo a ser buscado
	 * @param valor
	 *            numero con el cual se comparará
	 */
	public NumberLike(String propiedad, String valor) {

		this(propiedad, valor, MatchMode.CONTAIN);
	}

	private static final long serialVersionUID = 46392094642533751L;

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {

		String[] columns = criteriaQuery.findColumns(getPropiedad(), criteria);
		if (columns.length != 1) {
			throw new HibernateException(
					"NumberLike may only be used with single-column properties");
		}
		SessionFactoryImplementor factory = criteriaQuery.getFactory();

		if (factory.getDialect() instanceof H2Dialect) {
			return "cast(cast(" + columns[0] + " as int) as VARCHAR) "
					+ "like ?";
		} else {
			// XXX ver consistencia entre bases de datos
			return columns[0] + "::text like ?";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypedValue[] getTypedValues(Criteria criteria,
			CriteriaQuery criteriaQuery) {

		TypedValue tv = new TypedValue(new org.hibernate.type.StringType(),
				matchMode.toString(valor), EntityMode.POJO);
		return new TypedValue[] { tv };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Criterion getCriterion() {

		return this;
	}

}
