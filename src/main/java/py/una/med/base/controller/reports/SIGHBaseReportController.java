/**
 * @SIGHBaseReportController 1.0 12/03/13. Sistema Integral de Gestion
 *                           Hospitalaria
 */
package py.una.med.base.controller.reports;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import py.una.med.base.util.I18nHelper;
import py.una.med.base.util.LabelProvider;
import py.una.med.base.util.SIGHConverterV2;
import py.una.med.base.util.Serializer;

/**
 * 
 * Controlador utilizado para manejar los reportes complejos, es decir aquellos
 * que implican una serie de filtros y/o lista de ordenes.
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 15/03/2013
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class SIGHBaseReportController<T, K extends Serializable>
		implements ISIGHBaseReportController<T, K> {

	/**
	 * Mantiene los filtros ingresados
	 */
	private Map<String, Object> filterOptions;
	/**
	 * Se utiliza para indicar el valor que debe ser visualizado en los filtros
	 * para los componentes de la vista, en caso de que sea necesario.
	 */
	private Map<String, LabelProvider> labels;
	/**
	 * Mantiene la lista de ordenes seleccionados
	 */
	private List<String> orderSelected;

	private String typeExport;

	/**
	 * Genera la lista de ordenamiento disponible, solo es necesaria en algunos
	 * casos de uso, en los cuales debe ser sobreescrito y retornar la lista de
	 * ordenes disponibles.
	 */
	@Override
	public List<String> getBaseOrderOptions() {

		return null;
	}

	@Override
	public abstract void generateReport();

	@Override
	public SIGHConverterV2 getConverter() {

		return new SIGHConverterV2();
	}

	@Override
	public Map<String, Object> getFilterReport(Map<String, Object> paramsReport) {

		StringBuilder sb = new StringBuilder();

		for (Entry<String, Object> entry : filterOptions.entrySet()) {

			if (entry.getValue() != null && !entry.getValue().equals("")) {
				String value;
				if (labels.get(entry.getKey()) != null) {
					value = labels.get(entry.getKey()).getAsString(
							entry.getValue());

				} else {
					value = entry.getValue().toString();
					if (value.equals("true")) {
						value = "SI";
					} else {
						if (value.equals("false")) {
							value = "NO";
						}
					}
				}
				Serializer.contruct(sb, I18nHelper.getMessage(entry.getKey()),
						value.toUpperCase());
			}

		}
		paramsReport.put("selectionFilters", sb.toString());
		return paramsReport;

	}

	@Override
	public String getTypeExport() {

		if (typeExport == null) {
			return "pdf";
		}
		return typeExport;
	}

	@Override
	public void setTypeExport(String typeExport) {

		this.typeExport = typeExport;
	}

	@Override
	public List<String> getOrderSelected() {

		return orderSelected;
	}

	@Override
	public void setOrderSelected(List<String> orderSelected) {

		this.orderSelected = orderSelected;
	}

	@Override
	public Map<String, Object> getFilterOptions() {

		if (filterOptions == null) {
			filterOptions = new LinkedHashMap<String, Object>();
		}
		return filterOptions;
	}

	@Override
	public void setFilterOptions(Map<String, Object> filterOptions) {

		this.filterOptions = filterOptions;
	}

	public Map<String, LabelProvider> getLabels() {

		if (labels == null) {
			labels = new LinkedHashMap<String, LabelProvider>();
		}
		return labels;
	}

	public void setLabels(Map<String, LabelProvider> labels) {

		this.labels = labels;
	}

	public LabelProvider setLabelProvider(String field, LabelProvider lp) {

		getLabels().put(field, lp);
		return labels.get(field);
	}

	@Override
	public String getDefaultPermission() {

		return "SIGH";
	}

}
