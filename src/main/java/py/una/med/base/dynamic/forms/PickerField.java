/*
 * @PickerField.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.validator.ValidatorException;
import py.una.med.base.exception.KarakuRuntimeException;
import py.una.med.base.util.LabelProvider;
import py.una.med.base.util.LabelProvider.StringLabelProvider;
import py.una.med.base.util.SIGHListHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class PickerField<T> extends LabelField {

	public interface KeyListener {

		boolean onBlur(Field source, AjaxBehaviorEvent event,
				Object submittedValue);
	}

	public static interface ValueChangeListener<T> {

		public boolean onChange(Field source, T value);
	}

	/**
	 * Tipo de este componente
	 */
	public static final String TYPE = "py.una.med.base.dynamic.forms.PickerField";
	private boolean hasCodeInput;
	private String popupID;
	private KeyListener keyListener;
	private ValueExpression valueExpression;
	private ValueExpression codeExpression;
	private String urlColumns;
	private SIGHListHelper<T, Long> listHelper;
	private LabelProvider<T> valueLabelProvider;
	private HtmlInputText codeInput;
	private boolean buttonDisabled;
	private boolean popupShow;
	private T temp;
	private boolean nullable;
	private boolean selected;
	private ValueChangeListener<T> valueChangeListener;
	private PickerValidator validator;
	private String popupTitle;

	// private HtmlInputText hidden;

	/**
	 * Crea un picker field, con ID autogenerado
	 */
	public PickerField() {

		super();
		this.codeInput = SIGHComponentFactory.getHtmlInputText();
		this.popupID = getId() + "popup";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see py.una.med.base.forms.dynamic.Field#getType()
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
				createFacesMessage(FacesMessage.SEVERITY_WARN, "",
						"COMPONENT_PICKER_INPUT_NOT_FOUND");
			}
		}

	}

	/**
	 * Retorna el list helper, el cual se encarga de los filtros y de mostrar
	 * informacion
	 * 
	 * @return ListHelper
	 */
	public SIGHListHelper<T, Long> getListHelper() {

		return listHelper;
	}

	/**
	 * Setea el list helper que sera utilizado por el sistema
	 * 
	 * @param listHelper
	 *            listHelper para setear
	 */
	public void setListHelper(final SIGHListHelper<T, Long> listHelper) {

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

		return codeInput;
	}

	/**
	 * @return popupShow
	 */
	public boolean isPopupShow() {

		return popupShow;
	}

	public void emptyMethod() {

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

	static UIComponent lastFound;

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

	public PickerValidator getValidator() {

		if (validator == null) {
			validator = new PickerValidator();
			validator.setPickerField(this);
		}
		return validator;
	}

	public boolean isNullable() {

		return nullable;
	}

	public void setNullable(boolean nullable) {

		this.nullable = nullable;
	}

	public UIInput getHidden() {

		return new HiddenText();
	}

	public void setHidden(UIInput hidden) {

	}

	public static class PickerValidator implements
			javax.faces.validator.Validator {

		PickerField<?> pickerField;

		public PickerValidator() {

		}

		public void setPickerField(PickerField<?> pickerField) {

			this.pickerField = pickerField;
		}

		@Override
		public void validate(FacesContext context, UIComponent component,
				Object value) throws ValidatorException {

			if (pickerField == null)
				return;
			if (!pickerField.isNullable() && pickerField.temp == null) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						pickerField.getMessage("COMPONENT_PICKER_NOT_SELECTED"),
						pickerField.getMessage("COMPONENT_PICKER_NOT_SELECTED"));
				throw new ValidatorException(msg);
			}

		}

		public PickerField<?> getPickerField() {

			return pickerField;
		}
	}

	private class HiddenText extends HtmlInputText {

		@Override
		public void updateModel(FacesContext context) {

			T objectToSave = PickerField.this.getValue();
			PickerField.this.getValueExpression().setValue(
					context.getELContext(), objectToSave);
			PickerField.this.temp = null;
			PickerField.this.selected = false;
		}
	}

	/**
	 * Se encarga de llamar al metodo
	 * {@link ValueChangeListener#onChange(Field, Object)} cuando existe un
	 * cambio en el valor asociado al {@link PickerField}
	 **/
	public void changeValueListener() {

		if (valueChangeListener == null) {
			return;
		}
		valueChangeListener.onChange(this, getValue());
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

}
