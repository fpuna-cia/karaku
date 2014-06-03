/*
 * @ISIGHQueryController.java 1.0 02/08/2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import py.una.med.base.util.SIGHConverterV2;

/**
 * 
 * @author Osmar Vianconi
 * @since 1.0
 * @version 1.0 02/08/2013
 * 
 */
public interface ISIGHQueryController<T, K extends Serializable> extends
		ISIGHAdvancedController<T, K> {

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
	SIGHConverterV2 getConverter();
}
