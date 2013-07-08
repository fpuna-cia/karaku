/*
 * @ISIGHBaseReportController.java 1.0 12/03/13 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.controller.reports;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import py.una.med.base.util.SIGHConverterV2;

/**
 * Interface del controlador utilizado para manejar los reportes complejos.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 13/05/2013
 * 
 */
public interface ISIGHBaseReportController<T, ID extends Serializable> {

	Map<String, Object> getFilterOptions();

	void setFilterOptions(Map<String, Object> filterOptions);

	List<String> getBaseOrderOptions();

	List<String> getOrderSelected();

	void setOrderSelected(List<String> orderSelected);

	String getTypeExport();

	void setTypeExport(String typeExport);

	Map<String, Object> getFilterReport(Map<String, Object> paramsReport);

	void generateReport();

	String getDefaultPermission();

	/**
	 * @return
	 */
	SIGHConverterV2 getConverter();

}
