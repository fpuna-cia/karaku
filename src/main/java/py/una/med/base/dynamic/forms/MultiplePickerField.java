/*
 * @MultiplePickerField.java 1.0 Jun 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import java.util.Collections;
import java.util.List;
import py.una.med.base.util.KarakuListHelperProvider;
import py.una.med.base.util.LabelProvider;

/**
 * 
 * @author Jorge Ramírez
 * @since 1.0
 * @version 1.0 Jun 25, 2013
 * 
 */
public class MultiplePickerField<T> extends LabelField {

	public interface ItemKeyProvider<T> {

		Object getKeyValue(T item);
	}

	public interface ValuesChangeListener<T> {

		boolean onChange(Field source, List<T> values);
	}

	public interface PickerValidator<T> {

		boolean validate(Field source, List<T> values);
	}

	public static final String TYPE = "py.una.med.base.dynamic.forms.MultiplePickerField";

	private String popupID;
	private String dataTableID;
	private boolean buttonDisabled;
	private List<T> values;
	private LabelProvider<List<T>> valuesLabelProvider;
	private KarakuListHelperProvider<T> listHelper;
	private String urlColumns;
	private ItemKeyProvider<T> itemKeyProvider;
	private ValuesChangeListener<T> valuesChangeListener;
	private PickerValidator<T> pickerValidator;

	public MultiplePickerField() {

		super();
		popupID = getId() + "popup";
		dataTableID = popupID + "datatable";
	}

	@Override
	public String getType() {

		return TYPE;
	}

	public String getPopUpClientID() {

		return findComponent(getPopupID()).getClientId();
	}

	@Override
	public boolean disable() {

		setButtonDisabled(true);
		return true;
	}

	@Override
	public boolean enable() {

		setButtonDisabled(false);
		return true;
	}

	public String getPopupID() {

		return popupID;
	}

	public void setPopupID(String id) {

		popupID = id;
	}

	public boolean isButtonDisabled() {

		return buttonDisabled;
	}

	public void setButtonDisabled(boolean buttonDisabled) {

		this.buttonDisabled = buttonDisabled;
	}

	public List<T> getValues() {

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

	public Object getItemKey(T item) {

		if (itemKeyProvider == null) {
			throw new IllegalArgumentException(
					"itemKeyProvider no puede ser null");
		}
		return itemKeyProvider.getKeyValue(item);
	}

	/**
	 * Retorna la url donde se encuentran las columnas mostradas por el popup de
	 * seleccion
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

	public ItemKeyProvider<T> getItemKeyProvider() {

		return itemKeyProvider;
	}

	public void setItemKeyProvider(ItemKeyProvider<T> itemKeyProvider) {

		this.itemKeyProvider = itemKeyProvider;
	}

	/**
	 * Se encarga de llamar al metodo {@link ValueChangeListener#onChange }
	 * cuando existe un cambio en el valor asociado al {@link PickerField}
	 **/
	public void changeValueListener() {

		if (valuesChangeListener == null) {
			return;
		}
		valuesChangeListener.onChange(this, getValues());
		performValidation();
	}

	/**
	 * Setea el {@link ValuesChangeListener} cuando existe un cambio en el valor
	 * asociado al {@link PickerField}
	 **/
	public void setValuesChangeListener(ValuesChangeListener<T> listener) {

		this.valuesChangeListener = listener;
	}

	public void performValidation() {

		if (pickerValidator == null) {
			return;
		}
		pickerValidator.validate(this, getValues());
	}

	public void setPickerValidator(PickerValidator<T> validator) {

		if (validator == null) {
			throw new IllegalArgumentException("validator no puede ser null");
		}
		pickerValidator = validator;
	}

	public void clear() {

		values = Collections.<T> emptyList();

	}

}
