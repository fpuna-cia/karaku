/*
 * @ListSelectField.java 1.0 Jun 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.Collections;
import java.util.List;
import javax.faces.event.ValueChangeListener;
import py.una.med.base.domain.BaseEntity;
import py.una.med.base.util.KarakuListHelperProvider;
import py.una.med.base.util.LabelProvider;

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

		return "py.una.med.base.dynamic.forms.ListSelectField";
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
