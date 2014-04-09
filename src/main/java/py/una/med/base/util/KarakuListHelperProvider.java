package py.una.med.base.util;

import java.util.List;
import javax.faces.model.SelectItem;
import py.una.med.base.dao.restrictions.Where;

public interface KarakuListHelperProvider<T> {

	List<T> getEntities();

	/**
	 * @return simpleFilter
	 */
	SimpleFilter getSimpleFilter();

	/**
	 * @return filterOptions
	 */
	List<SelectItem> getFilterOptions();

	void setBaseWhere(Where<T> where);

	PagingHelper getHelper();

	/**
	 * Notifica que la lista de elementos ha cambiado y debe ser recargada.
	 * 
	 */
	void reloadEntities();

}
