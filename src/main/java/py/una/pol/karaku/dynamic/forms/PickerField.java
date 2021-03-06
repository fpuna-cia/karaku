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

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.util.KarakuListHelperProvider;
import py.una.pol.karaku.util.LabelProvider;
import py.una.pol.karaku.util.LabelProvider.StringLabelProvider;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class PickerField<T> extends LabelField {

	private static final String COMPONENT_PICKER_INPUT_NOT_FOUND = "COMPONENT_PICKER_INPUT_NOT_FOUND";

	public interface KeyListener {

		boolean onBlur(Field source, AjaxBehaviorEvent event,
				Object submittedValue);
	}

	public interface ValueChangeListener<T> {

		boolean onChange(Field source, T value);
	}

	/**
	 * Tipo de este componente
	 */
	public static final String TYPE = "py.una.pol.karaku.dynamic.forms.PickerField";
	private boolean hasCodeInput;
	private String popupID;
	private KeyListener keyListener;
	private ValueExpression valueExpression;
	private ValueExpression codeExpression;
	private String urlColumns;
	private KarakuListHelperProvider<T> listHelper;
	private LabelProvider<T> valueLabelProvider;
	private HtmlInputText codeInput;
	private boolean buttonDisabled;
	private boolean popupShow;
	private T temp;
	private boolean nullable;
	private boolean selected;
	private ValueChangeListener<T> valueChangeListener;
	private String popupTitle;
	private String dataTableID;

	/**
	 * Crea un picker field, con ID autogenerado
	 */
	public PickerField() {

		super();
		this.popupID = getId() + "popup";
		dataTableID = popupID + "datatable";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.pol.karaku.forms.dynamic.Field#getType()
	 */
	@Override
	public String getType() {

		return TYPE;
	}

	/**
	 * Listener que escucha las peticiones del field codigo y llama al
	 * correspondiente {@link KeyListener} cuando un evento ocurre, si el
	 * correspondiente {@link KeyListener} no maneja el evento, se setea a null
	 * el objeto seleccionado
	 * 
	 * @param event
	 */
	public void keyUpListener(final AjaxBehaviorEvent event) {

		Object submitted = ((HtmlInputText) event.getSource())
				.getSubmittedValue();
		FacesContext fc = FacesContext.getCurrentInstance();
		if (keyListener != null) {
			boolean bool = keyListener.onBlur(this, event, submitted);
			// Tratar cuadno se selecciona un valor nulo
			if (!bool) {
				getValueExpression().setValue(fc.getELContext(), null);
				createFacesMessage(FacesMessage.SEVERITY_WARN,
						COMPONENT_PICKER_INPUT_NOT_FOUND,
						COMPONENT_PICKER_INPUT_NOT_FOUND);
			}
		}

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
			throw new IllegalArgumentException("listHelper can't be null");
		}
		this.listHelper = listHelper;
	}

	/**
	 * Verifica si el componente tiene visible la opcion de insertar el codigo a
	 * mano
	 * 
	 * @return hasCodeInput
	 */
	public boolean isHasCodeInput() {

		return hasCodeInput;
	}

	/**
	 * Configura el componente para que cuando sea mostrado tenga un output
	 * 
	 * @param hasCodeInput
	 *            hasCodeInput para setear
	 */
	public void setHasCodeInput(final boolean hasCodeInput) {

		this.hasCodeInput = hasCodeInput;
	}

	/**
	 * Retorna el {@link KeyListener} que esta actualmente activo, se encarga de
	 * escuchar todas las peticiones que se realizan sobre el input
	 * 
	 * @return keyListener
	 */
	public KeyListener getKeyListener() {

		return keyListener;
	}

	/**
	 * Setea el {@link KeyListener} y configura el componente para visualizar la
	 * opción de insercion manual de codigo
	 * 
	 * @param keyListener
	 *            keyListener para setear
	 */
	public void setKeyListener(final KeyListener keyListener) {

		if (keyListener == null) {
			throw new IllegalArgumentException("keyListener can be null");
		}
		setHasCodeInput(true);
		this.keyListener = keyListener;
	}

	/**
	 * Retorna la expresion que se utiliza para mostrar el codigo en el
	 * textfield de codigo, aqui se almacenaran automaticamente todos los
	 * cambios que se realizen sobre este campo, y si esta configurado, el campo
	 * activara {@link PickerField#keyUpListener(AjaxBehaviorEvent)} que llamara
	 * al {@link KeyListener} configurado en
	 * {@link PickerField#getKeyListener()}
	 * 
	 * @return idExpression
	 */
	public ValueExpression getCodeExpression() {

		return codeExpression;
	}

	/**
	 * Asigna la expresion que se utiliza para mostrar el codigo
	 * 
	 * @param idExpression
	 *            idExpression para setear
	 */
	public void setCodeExpression(final ValueExpression idExpression) {

		codeExpression = idExpression;
	}

	/**
	 * Retorna el codigo a ser mostrado por el componente
	 * 
	 * @return "" si no esta configurado para mostrar el codigo, en otro caso
	 *         retorna la evaluacion de {@link PickerField#getCodeExpression()}
	 */
	public String getCode() {

		if (!hasCodeInput) {
			return "";
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		if (codeExpression == null) {
			throw new IllegalArgumentException("Code Expression not set, "
					+ "set one with PickerField#setCodeExpression(expression))");
		}

		Object value = codeExpression.getValue(fc.getELContext());
		if (value == null) {
			return "";
		}

		return codeExpression.getValue(fc.getELContext()).toString();
	}

	public void setCode(final String code) {

		// el codigo no importa, se utiliza solamente para respetar la
		// convencion.
	}

	/**
	 * @return valueExpression
	 */
	public ValueExpression getValueExpression() {

		return valueExpression;
	}

	/**
	 * @param valueExpression
	 *            valueExpression para setear
	 */
	public void setValueExpression(final ValueExpression valueExpression) {

		this.valueExpression = valueExpression;
	}

	/**
	 * @return popupID
	 */
	public String getPopupID() {

		return popupID;
	}

	/**
	 * @param popupID
	 *            popupID para setear
	 */
	public void setPopupID(final String popupID) {

		this.popupID = popupID;
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
	 * Formatea la opcion actualmente seleccionada para ser mostrada al usuario
	 * 
	 * @return opcion correctamente formateada
	 */
	public String getFormmatedSelectedOption() {

		T option = getValue();
		return getFormmatedOption(option);

	}

	/**
	 * Formatea una opcion pasada, esto invoca al
	 * {@link PickerField#getValueLabelProvider()} para formatear segun
	 * configuracion del usuario, si no se configura el {@link LabelProvider},
	 * entonces utiliza {@link StringLabelProvider}
	 * 
	 * @param option
	 * @return
	 */
	public String getFormmatedOption(final T option) {

		if (getValueLabelProvider() == null) {
			if (option == null) {
				return "";
			} else {
				return option.toString();
			}
		}
		return getValueLabelProvider().getAsString(option);
	}

	/**
	 * @return valueLabelProvider
	 */
	public LabelProvider<T> getValueLabelProvider() {

		return valueLabelProvider;
	}

	/**
	 * @param valueLabelProvider
	 *            valueLabelProvider para setear
	 */
	public void setValueLabelProvider(final LabelProvider<T> valueLabelProvider) {

		this.valueLabelProvider = valueLabelProvider;
	}

	/**
	 * @return codeInput
	 */
	public HtmlInputText getCodeInput() {

		if (codeInput == null) {
			codeInput = KarakuComponentFactory.getHtmlInputText();

		}

		return codeInput;
	}

	/**
	 * @return popupShow
	 */
	public boolean isPopupShow() {

		return popupShow;
	}

	public void emptyMethod() {

		// utilizado para representar una acción nula
	}

	/**
	 * @param popupShow
	 *            popupShow para setear
	 */
	public void setPopupShow(final boolean popupShow) {

		this.popupShow = popupShow;
	}

	/**
	 * @param codeInput
	 *            codeInput para setear
	 */
	public void setCodeInput(final HtmlInputText codeInput) {

		this.codeInput = codeInput;
	}

	public String getPopUpClientID() {

		UIComponent find = findComponent(getPopupID());
		if (find == null) {
			throw new KarakuRuntimeException("Popup with id: " + getId()
					+ " not found.");
		} else {
			return find.getClientId();
		}
	}

	public void setValue(final T value) {

		temp = value;
		selected = true;
	}

	public void initialize() {

		temp = null;
		selected = false;
	}

	/**
	 * Retorna el valor actualmente seleccionado en el Picker, si se selecciono
	 * un valor retorna el valor temporal (antes de persistir) y si ya se
	 * selecciono y se acepto, retorna el valor asociado en
	 * {@link #getValueExpression()}
	 * 
	 * @return T objeto actualmente seleccionado.
	 */
	@SuppressWarnings("unchecked")
	public T getValue() {

		// Si se selecciono un valor
		if (selected) {
			return temp;
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			return (T) getValueExpression().getValue(fc.getELContext());
		}
	}

	@Override
	public boolean enable() {

		getCodeInput().setDisabled(false);
		setButtonDisabled(false);
		return true;
	}

	@Override
	public boolean disable() {

		getCodeInput().setDisabled(true);
		setButtonDisabled(true);

		return true;
	}

	public boolean isButtonDisabled() {

		return buttonDisabled;
	}

	public void setButtonDisabled(final boolean buttonDisabled) {

		this.buttonDisabled = buttonDisabled;
	}

	public String getIdMessage() {

		return getId() + "messages";
	}

	public boolean isNullable() {

		return nullable;
	}

	public void setNullable(boolean nullable) {

		this.nullable = nullable;
	}

	/**
	 * Método invocado una vez que se realiza el proceso de update para que el
	 * valor temporal se vuelva final.
	 */
	void postUpdate() {

		T objectToSave = getValue();
		getValueExpression().setValue(
				FacesContext.getCurrentInstance().getELContext(), objectToSave);
		temp = null;
		selected = false;
	}

	/**
	 * Se encarga de llamar al metodo
	 * {@link ValueChangeListener#onChange(Field, Object)} cuando existe un
	 * cambio en el valor asociado al {@link PickerField}
	 **/
	@SuppressWarnings("unchecked")
	public void changeValueListener(Object value) {

		setValue((T) value);
		if (valueChangeListener == null) {
			return;
		}
		valueChangeListener.onChange(this, (T) value);
	}

	/**
	 * Setea el {@link ValueChangeListener} cuando existe un cambio en el valor
	 * asociado al {@link PickerField}
	 **/
	public void setValueChangeListener(ValueChangeListener<T> listener) {

		this.valueChangeListener = listener;
	}

	/**
	 * Asigna un titulo al popup donde se selecciona la entidad, por defecto es
	 * igual a {@link #getLabel()}
	 * 
	 * @param popupTitle
	 *            key del archivo de internacionalizacion.
	 */
	public void setPopupTitle(String popupTitle) {

		this.popupTitle = getMessage(popupTitle);
	}

	/**
	 * Retorna el titulo del popUp, por defecto es igual a {@link #getLabel()}
	 * 
	 * @return Cadena ya internacionalizada
	 */
	public String getPopupTitle() {

		return popupTitle == null ? getLabel() : popupTitle;
	}

	public String getDataTableID() {

		return dataTableID;
	}

	public void setDataTableID(String id) {

		dataTableID = id;
	}

}
