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
package py.una.pol.karaku.dynamic.forms;

import java.util.Collections;
import java.util.List;
import javax.faces.event.ValueChangeListener;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.util.KarakuListHelperProvider;
import py.una.pol.karaku.util.LabelProvider;

/**
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 Jun 25, 2013
 * 
 */
public class ListSelectField<T> extends LabelField {

	public interface ValuesChangeListener<T> {

		boolean onChange(Field source, List<T> values);
	}

	private String dataTableID;
	private List<T> values;
	private LabelProvider<List<T>> valuesLabelProvider;
	private KarakuListHelperProvider<T> listHelper;
	private String urlColumns;
	private ValuesChangeListener<T> valuesChangeListener;
	private boolean disabled;

	public ListSelectField() {

		super();
		dataTableID = getId() + "datatable";
	}

	@Override
	public String getType() {

		return "py.una.pol.karaku.dynamic.forms.ListSelectField";
	}

	@Override
	public boolean disable() {

		setDisabled(true);
		return true;
	}

	@Override
	public boolean enable() {

		setDisabled(false);
		return true;
	}

	public List<T> getValues() {

		if (values == null) {
			return Collections.emptyList();
		}
		return values;
	}

	public void setValues(List<T> values) {

		this.values = values;
	}

	public LabelProvider<List<T>> getValuesLabelProvider() {

		return valuesLabelProvider;
	}

	public void setValuesLabelProvider(
			LabelProvider<List<T>> valuesLabelProvider) {

		this.valuesLabelProvider = valuesLabelProvider;
	}

	public String getFormatedSelectedOptions() {

		return getFormatedSelectedOptions(values);
	}

	public String getFormatedSelectedOptions(List<T> options) {

		if (options == null) {
			return "";
		}
		if (valuesLabelProvider == null) {
			return Integer.toString(options.size());
		}
		return valuesLabelProvider.getAsString(options);
	}

	public Object getItemKey(Object item) {

		return getKeyValue(item);
	}

	/**
	 * Retorna la url donde se encuentran las columnas mostradas en la grilla de
	 * elementos.
	 * 
	 * @return urlColumns
	 */
	public String getUrlColumns() {

		return urlColumns;
	}

	/**
	 * Configura el componente para mostrar las filas que se encuetnran en la
	 * url pasada como parametro
	 * 
	 * @param urlColumns
	 *            urlColumns para setear
	 */
	public void setUrlColumns(final String urlColumns) {

		this.urlColumns = urlColumns;
	}

	/**
	 * Retorna el list helper, el cual se encarga de los filtros y de mostrar
	 * informacion
	 * 
	 * @return ListHelper
	 */
	public KarakuListHelperProvider<T> getListHelper() {

		return listHelper;
	}

	/**
	 * Setea el list helper que sera utilizado por el sistema
	 * 
	 * @param listHelper
	 *            listHelper para setear
	 */
	public void setListHelper(final KarakuListHelperProvider<T> listHelper) {

		if (listHelper == null) {
			throw new IllegalArgumentException("listHelper no puede ser null");
		}
		this.listHelper = listHelper;
	}

	public String getDataTableID() {

		return dataTableID;
	}

	public void setDataTableID(String id) {

		dataTableID = id;
	}

	public Object getKeyValue(Object item) {

		if (!(item instanceof BaseEntity)) {
			return "";
		}
		return ((BaseEntity) item).getId().toString();

	}

	/**
	 * Se encarga de llamar al metodo {@link ValueChangeListener#onChange }
	 * cuando existe un cambio en la lista de elementos seleccionados.
	 **/
	public void changeValueListener() {

		if (valuesChangeListener == null) {
			return;
		}
		valuesChangeListener.onChange(this, getValues());
	}

	/**
	 * Setea el {@link ValuesChangeListener} cuando existe un cambio en la lista
	 * de elementos seleccionados.
	 **/
	public void setValuesChangeListener(ValuesChangeListener<T> listener) {

		this.valuesChangeListener = listener;
	}

	/**
	 * Limpia la lista de elementos seleccionados.
	 */
	public void clear() {

		setValues(Collections.<T> emptyList());

	}

	public boolean isDisabled() {

		return disabled;
	}

	public void setDisabled(boolean disabled) {

		this.disabled = disabled;
	}

}
