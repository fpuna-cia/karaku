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
