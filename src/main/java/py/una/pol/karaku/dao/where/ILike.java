/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package py.una.pol.karaku.dao.where;

import javax.annotation.Nonnull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class ILike implements Clause {

	@Nonnull
	private String path;

	@Nonnull
	private Object value;

	@Nonnull
	private MatchMode mode;

	public ILike(@Nonnull String path, @Nonnull Object value) {

		this(path, value, MatchMode.CONTAIN);
	}

	public ILike(@Nonnull String path, @Nonnull Object value,
			@Nonnull MatchMode mode) {

		super();
		this.path = path;
		this.value = value;
		this.mode = mode;
	}

	@Nonnull
	public String getPath() {

		return path;
	}

	public void setPath(@Nonnull String path) {

		this.path = path;
	}

	public Object getValue() {

		return value;
	}

	public void setValue(@Nonnull Object value) {

		this.value = value;
	}

	@Override
	public Criterion getCriterion() {

		return Restrictions.ilike(path, value.toString(), getMode()
				.getMatchMode());
	}

	public void setMode(@Nonnull MatchMode mode) {

		this.mode = mode;
	}

	public MatchMode getMode() {

		return mode;
	}

}
