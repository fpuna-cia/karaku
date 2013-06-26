package py.una.med.base.dao.restrictions;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.engine.spi.TypedValue;
import py.una.med.base.dao.where.IClause;

/**
 * Clase que representa una condicion de Where para hacer busqueda en similutd
 * entre numeros, por ejemplo, si buscamos 787 entre los numeros, convertira a
 * cadena la columna y hara un like convencional.
 * 
 * @author Arturo Volpe
 * @version 1.0
 * @since 1.0 08/02/2013
 * 
 */
public class NumberLike implements Criterion, IClause {

	private String propiedad;

	private String valor;

	private MatchMode matchMode;

	public void setPropiedad(String propiedad) {

		this.propiedad = propiedad;
	}

	public String getPropiedad() {

		return propiedad;
	}

	public NumberLike(String propiedad, String valor, MatchMode matchMode) {

		super();
		this.matchMode = matchMode;
		this.propiedad = propiedad;
		this.valor = valor;
	}

	public NumberLike(String propiedad, String valor) {

		this(propiedad, valor, MatchMode.ANYWHERE);
	}

	private static final long serialVersionUID = 46392094642533751L;

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException {

		String[] columns = criteriaQuery.findColumns(getPropiedad(), criteria);
		if (columns.length != 1) {
			throw new HibernateException(
					"Like may only be used with single-column properties");
		}
		// XXX ver consistencia entre bases de datos
		return columns[0] + "::text ilike ?";
	}

	@Override
	public TypedValue[] getTypedValues(Criteria criteria,
			CriteriaQuery criteriaQuery) throws HibernateException {

		return new TypedValue[] { new TypedValue(
				new org.hibernate.type.StringType(),
				matchMode.toMatchString(valor), EntityMode.POJO) };
	}

	@Override
	public Criterion getCriterion() {

		return this;
	}

}
