/*
 * @PickerField.java 1.0 Feb 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.dynamic.forms;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import py.una.med.base.util.LabelProvider;
import py.una.med.base.util.SIGHListHelper;

/**
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
public class PickerField<T> extends LabelField {

	public static interface KeyListener {

		public boolean onBlur(Field source, AjaxBehaviorEvent event,
				Object submittedValue);
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

	/**
	 * Crea un picker field, con ID autogenerado
	 */
	public PickerField() {

		super();
		System.out.println("Creando pickerField con id: " + getId());
		setCodeInput(SIGHComponentFactory.getHtmlInputText());
		setPopupID(getId() + "popup");
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
		// try {
		// getCodeExpression().setValue(fc.getELContext(), submitted);
		// } catch (PropertyNotFoundException exception) {
		// throw new PropertyNotFoundException(
		// "Imposible setear el codigo, con expresion:"
		// + getCodeExpression().getExpressionString());
		// }
		if (keyListener != null) {
			boolean bool = keyListener.onBlur(this, event, submitted);
			// Tratar cuadno se selecciona un valor nulo
			if (bool == false) {
				getValueExpression().setValue(fc.getELContext(), null);
				createFacesMessage(FacesMessage.SEVERITY_WARN, "",
						"No se encuentra la entidad seleccionada");
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

		if (listHelper == null)
			throw new NullPointerException("List Helper can be null");
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

		if (keyListener == null)
			throw new NullPointerException("keyListener can be null");
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

		System.out.println("PickerField.getCode()");
		if (!hasCodeInput)
			return "";
		FacesContext fc = FacesContext.getCurrentInstance();
		if (codeExpression == null)
			throw new NullPointerException("Code Expression not set, "
					+ "set one with PickerField#setCodeExpression(expression))");
		System.out.println("Retornando:"
				+ codeExpression.getValue(fc.getELContext()).toString());
		System.out.println("Current selected " + getFormmatedSelectedOption());
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
	@SuppressWarnings("unchecked")
	public String getFormmatedSelectedOption() {

		T option = (T) getValueExpression().getValue(
				getFacesContext().getELContext());
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

		System.out.println("PickerField.getFormmatedOption()");
		if (getValueLabelProvider() == null) {
			if (option == null)
				return "";
			else
				return option.toString();
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

		return findComponent(getPopupID()).getClientId();
	}

	public void setValue(final T value) {

		FacesContext fc = FacesContext.getCurrentInstance();

		getValueExpression().setValue(fc.getELContext(), value);
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getValue() {

		FacesContext fc = FacesContext.getCurrentInstance();
		return (T) getValueExpression().getValue(fc.getELContext());

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
}
