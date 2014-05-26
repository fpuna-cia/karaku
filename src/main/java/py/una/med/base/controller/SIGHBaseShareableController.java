/*
 * @SIGHBaseShareableController.java 1.0 May 26, 2014 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.controller;

import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.replication.Shareable;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 26, 2014
 * 
 */
public abstract class SIGHBaseShareableController<T extends Shareable> extends
		SIGHAdvancedController<T, Long> {

	private static final String FIELD_ACTIVO = "activo";

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Agrega la restricción de que solo se muestren aquellas entidades que
	 * están activas.
	 * </p>
	 */
	@Override
	public Where<T> getBaseWhere() {

		return super.getBaseWhere().addClause(
				Clauses.eq(FIELD_ACTIVO, Shareable.YES));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Agrega la restricción de que solo se muestren aquellas entidades que
	 * están activas.
	 * </p>
	 */
	@Override
	public Where<T> getWhereReport() {

		return super.getWhereReport().addClause(
				Clauses.eq(FIELD_ACTIVO, Shareable.YES));
	}

	/**
	 * Retorna un Where que solo muestra entidades activas.
	 * 
	 * @return
	 */
	protected <Z> Where<Z> getOnlyActivosWhere() {

		Where<Z> where = Where.get();
		where.addClause(Clauses.eq(FIELD_ACTIVO, Shareable.YES));
		return where;

	}

}
