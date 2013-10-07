/*
 * @Between.java 1.0 Oct 4, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dao.where;

import org.hibernate.criterion.Criterion;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 4, 2013
 * 
 */
public class Between implements Clause {

	private final String path;
	private final Object begin;
	private final Object end;

	/**
	 * @param path
	 * @param begin
	 * @param end
	 */
	public Between(String path, Object begin, Object end) {

		super();
		this.path = path;
		this.begin = begin;
		this.end = end;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Criterion getCriterion() {

		return null;
	}

	/**
	 * @return path
	 */
	public String getPath() {

		return path;
	}

	/**
	 * @return end
	 */
	public Object getEnd() {

		return end;
	}

	/**
	 * @return begin
	 */
	public Object getBegin() {

		return begin;
	}

}
