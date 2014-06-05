/*
 * @IKarakuQueryController.java 1.0 02/08/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import py.una.pol.karaku.util.KarakuConverter;

/**
 * 
 * @author Osmar Vianconi
 * @since 1.0
 * @version 1.0 02/08/2013
 * 
 */
@Deprecated
public interface IKarakuQueryController<T, K extends Serializable> extends
		IKarakuAdvancedController<T, K> {

	Map<String, Object> getFilterOptions();

	void setFilterOptions(Map<String, Object> filterOptions);

	List<String> getBaseOrderOptions();

	List<String> getOrderSelected();

	void setOrderSelected(List<String> orderSelected);

	Map<String, Object> getFilterQuery(Map<String, Object> paramsQuery);

	void generateQuery();

	@Override
	String getDefaultPermission();

	/**
	 * @return
	 */
	KarakuConverter getConverter();
}
