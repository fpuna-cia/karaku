/*
 * @SIGHBaseShareableController.java 1.0 May 26, 2014 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.pol.karaku.controller;

import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.replication.Shareable;

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
				Clauses.eq(getActivoFieldName(), Shareable.YES));
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
				Clauses.eq(getActivoFieldName(), Shareable.YES));
	}

	/**
	 * Retorna un Where que solo muestra entidades activas.
	 * 
	 * @return
	 */
	protected <Z> Where<Z> getOnlyActivosWhere() {

		Where<Z> where = Where.get();
		where.addClause(Clauses.eq(getActivoFieldName(), Shareable.YES));
		return where;

	}

	/**
	 * Devuelve el nombre del campo que indica si la entidad está activa.
	 * 
	 * @return nombre del campo activo
	 */
	protected String getActivoFieldName() {

		return FIELD_ACTIVO;
	}

}
