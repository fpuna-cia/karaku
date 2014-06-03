/*
 * @SIGHBaseReportController 1.0 12/03/13. Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.controller.reports;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.FormatProvider;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.LabelProvider;
import py.una.pol.karaku.util.SIGHConverterV2;
import py.una.pol.karaku.util.Serializer;
import py.una.pol.karaku.util.StringUtils;

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
	@Autowired
	private transient FormatProvider fp;
	@Autowired
	protected ControllerHelper controllerHelper;

	/**
	 * Genera la lista de ordenamiento disponible, solo es necesaria en algunos
	 * casos de uso, en los cuales debe ser sobreescrito y retornar la lista de
	 * ordenes disponibles.
	 * 
	 * @return lista no nula de columnas.
	 */
	@Override
	public List<String> getBaseOrderOptions() {

		return Collections.emptyList();
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
			if (StringUtils.isValid(entry.getValue())) {
				String value;
				if (labels != null && labels.get(entry.getKey()) != null) {
					value = labels.get(entry.getKey()).getAsString(
							entry.getValue());

				} else {
					value = entry.getValue().toString();
					if (entry.getValue() instanceof Date) {
						value = fp.asDate((Date) entry.getValue());
					} else {
						value = entry.getValue().toString();
					}
					if ("true".equals(value)) {
						value = "SI";
					} else {
						if ("false".equals(value)) {
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

	public <U> LabelProvider<U> setLabelProvider(String field,
			LabelProvider<U> lp) {

		this.getLabels().put(field, lp);
		return labels.get(field);
	}

	@Override
	public String getDefaultPermission() {

		return "SIGH";
	}

	/**
	 * Retorna una cadena internacionalizada dada la llave.
	 * 
	 * @param code
	 *            clave del archivo de internacionalización
	 * @return cadena internacionalizada
	 */
	public String getMessage(String code) {

		return this.controllerHelper.getMessage(code);
	}

}
