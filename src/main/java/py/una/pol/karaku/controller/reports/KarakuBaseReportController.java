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
package py.una.pol.karaku.controller.reports;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.util.FormatProvider;
import py.una.pol.karaku.util.I18nHelper;
import py.una.pol.karaku.util.KarakuConverter;
import py.una.pol.karaku.util.LabelProvider;
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
public abstract class KarakuBaseReportController<T, K extends Serializable>
		implements IKarakuBaseReportController<T, K> {

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
	protected I18nHelper helper;

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
	public KarakuConverter getConverter() {

		return KarakuConverter.getInstance();
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

		return this.helper.getString(code);
	}

}
