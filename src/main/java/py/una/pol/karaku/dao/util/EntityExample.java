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
package py.una.pol.karaku.dao.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import py.una.pol.karaku.dao.where.MatchMode;

public class EntityExample<T> {

	private T entity;
	private List<String> excluded;
	private MatchMode matchMode = MatchMode.CONTAIN;
	private boolean ignoreCase = true;
	private boolean excludeZeroes = true;

	public EntityExample(T entity, String ... excluded) {

		this.excluded = new ArrayList<String>();
		if (excluded != null) {
			this.excluded.addAll(Arrays.asList(excluded));
		}
		this.entity = entity;

	}

	public void addExcludedColumn(String column) {

		if (excluded == null) {
			excluded = new ArrayList<String>();
		}
		excluded.add(column);
	}

	public boolean isIgnoreCase() {

		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {

		this.ignoreCase = ignoreCase;
	}

	public void setMatchMode(MatchMode matchMode) {

		this.matchMode = matchMode;
	}

	public MatchMode getMatchMode() {

		return matchMode;
	}

	public T getEntity() {

		return entity;
	}

	public List<String> getExcluded() {

		return excluded;
	}

	public boolean isExcludeZeroes() {

		return excludeZeroes;
	}

	public void setExcludeZeroes(boolean excludeZeroes) {

		this.excludeZeroes = excludeZeroes;
	}

}
