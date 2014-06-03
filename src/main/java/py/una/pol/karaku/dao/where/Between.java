/*
 * @Between.java 1.0 Oct 4, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 4, 2013
 * 
 */
public class Between implements Clause {

	@Nonnull
	private final String path;
	@Nonnull
	private final Object begin;
	@Nonnull
	private final Object end;

	/**
	 * @param path
	 * @param begin
	 * @param end
	 */
	public Between(@Nonnull String path, @Nonnull Object begin,
			@Nonnull Object end) {

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
	@Nonnull
	public String getPath() {

		return path;
	}

	/**
	 * @return end
	 */
	@Nonnull
	public Object getEnd() {

		return end;
	}

	/**
	 * @return begin
	 */
	@Nonnull
	public Object getBegin() {

		return begin;
	}

}
