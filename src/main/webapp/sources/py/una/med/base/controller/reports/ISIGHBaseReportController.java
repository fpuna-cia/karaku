package py.una.med.base.controller.reports;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ISIGHBaseReportController<T, ID extends Serializable> {

	public HashMap<String, Object> getFilterOptions();

	public void setFilterOptions(LinkedHashMap<String, Object> filterOptions);

	public abstract List<String> getBaseOrderOptions();

	public List<String> getOrderSelected();

	public void setOrderSelected(List<String> orderSelected);

	public String getTypeExport();

	public void setTypeExport(String typeExport);

	public Map<String, Object> getFilterReport(
			HashMap<String, Object> paramsReport);

	public abstract void generateReport();

}
