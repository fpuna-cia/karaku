package py.una.med.base.util;

import java.util.List;
import javax.faces.model.SelectItem;

public interface KarakuListHelperProvider<T> {

	public abstract List<T> getEntities();

	/**
	 * @return simpleFilter
	 */
	public abstract SimpleFilter getSimpleFilter();

	/**
	 * @return filterOptions
	 */
	public abstract List<SelectItem> getFilterOptions();

}
