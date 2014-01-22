/*
 * @Regex.java 1.0 21/01/2014 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;

/**
 * Implementacion del operador Expresion Regular "~"
 * 
 * @author Arsenio Ferreira
 * @since 1.0
 * @version 1.0 21/01/2014
 * 
 */
public class Regex implements Clause, Criterion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Nonnull
	private final Object value;
	@Nonnull
	private final String path;

	public Regex(@Nonnull String path, @Nonnull Object value) {

		this.path = path;
		this.value = value;
	}

	@Nonnull
	public Object getValue() {

		return value;
	}

	@Nonnull
	public String getPath() {

		return path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public Criterion getCriterion() {

		return null;
	}

	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {

		String[] columns = criteriaQuery.findColumns(getPath(), criteria);
		if (columns.length != 1) {
			throw new HibernateException(
					"Regex may only be used with single-column properties");
		}
		SessionFactoryImplementor factory = criteriaQuery.getFactory();

		if (factory.getDialect() instanceof H2Dialect) {
			return columns[0] + " regexp ?";
		} else {
			// XXX ver consistencia entre bases de datos
			return columns[0] + " ~ ?";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypedValue[] getTypedValues(Criteria criteria,
			CriteriaQuery criteriaQuery) {

		TypedValue tv = new TypedValue(new org.hibernate.type.StringType(),
				value, EntityMode.POJO);
		return new TypedValue[] { tv };
	}

}
