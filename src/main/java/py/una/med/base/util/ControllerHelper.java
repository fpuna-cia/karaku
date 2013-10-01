/**
 * @ControllerHelper 1.0 19/02/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.util;

import java.util.Iterator;
import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import org.hibernate.exception.ConstraintViolationException;
import org.richfaces.component.UIColumn;
import org.richfaces.component.UIExtendedDataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.med.base.reports.Column;

/**
 * Clase que implementa funcionalidades generales para la manipulacion de
 * vistas, proveen funcionalidades que ya integran todas las partes del sistema.
 * Es un singleton compartido por todas las sesiones
 * 
 * @author Arturo Volpe
 * @author Nathalia Ochoa
 * @since 1.1 08/02/2013
 * @version 2.0 19/02/2013
 */
@Component
public class ControllerHelper {

	private Logger log;

	@Autowired
	private UniqueHelper uniqueHelper;

	/**
	 * Construye el controller
	 */
	@PostConstruct
	public void init() {

		log = LoggerFactory.getLogger(ControllerHelper.class);
	}

	@Autowired
	private I18nHelper i18nHelper;

	/**
	 * Crea un mensaje global, que será mostrado en lugares como
	 * &lt;rich:messages ajaxRendered="true" showDetail="true"
	 * globalOnly="true"/&gt;
	 * <p>
	 * El parámetro GlobalOnly permite que solo se muestren este tipo de
	 * mensajes, en caso contrario se mostrarán todos los mensajes
	 * 
	 * </p>
	 * 
	 * @param severity
	 *            grado de severidad {@link FacesMessage}
	 * @param summary
	 *            resumen
	 * @param detail
	 *            detalle del mensaje
	 */
	public void createGlobalFacesMessage(final Severity severity,
			final String summary, final String detail) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String mensaje = !"".equals(detail) ? getMessage(detail) : "";
		String sum = !"".equals(summary) ? getMessage(summary) : "";

		FacesMessage msg = new FacesMessage(severity, sum, mensaje);
		facesContext.addMessage(null, msg);
	}

	/**
	 * Crea un mensaje global, que será mostrado en lugares como
	 * &lt;rich:messages ajaxRendered="true" showDetail="true"
	 * globalOnly="true">
	 * <p>
	 * El parámetro GlobalOnly permite que solo se muestren este tipo de
	 * mensajes, en caso contrario se mostraran todos los mensajes
	 * </p>
	 * 
	 * @param severity
	 *            grado de severidad {@link FacesMessage}
	 * @param summary
	 *            clave de lmensaje de internacionalización
	 */
	public void createGlobalFacesMessage(final Severity severity,
			final String summary) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String sum = !"".equals(summary) ? getMessage(summary) : "";

		FacesMessage msg = new FacesMessage(severity, sum, "");
		facesContext.addMessage(null, msg);
	}

	/**
	 * Crea un mensaje global, que será mostrado en lugares como
	 * &lt;rich:messages ajaxRendered="true" showDetail="true"
	 * globalOnly="true">
	 * <p>
	 * El parámetro GlobalOnly permite que solo se muestren este tipo de
	 * mensajes, en caso contrario se mostraran todos los mensajes
	 * </p>
	 * 
	 * @param severity
	 *            grado de severidad {@link FacesMessage}
	 * @param summary
	 *            mensaje
	 */
	public void createGlobalFacesMessageSimple(final Severity severity,
			final String summary) {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		String sum = !"".equals(summary) ? summary : "";

		FacesMessage msg = new FacesMessage(severity, sum, "");
		facesContext.addMessage(null, msg);
	}

	/**
	 * Retorna el mensaje internacionalizado del código dado, para claves no
	 * encontradas retorna &&&&&code&&&&&&
	 * 
	 * @param code
	 *            llave del mensaje
	 * @return cadena internacionalizada
	 */
	public String getMessage(final String code) {

		if (code == null) {
			log.info("Buscando cadena con llave null");
			return getDegeneratedString("null");
		}
		if ("".equals(code)) {
			log.info("Buscando cadena con llave vacia");
			return getDegeneratedString("empty");
		}
		return i18nHelper.getString(code);
	}

	/**
	 * Dada una cadena la retorna degenerada.
	 * 
	 * @param code
	 *            cadena a deformar
	 * @return Cadena degenerada, con el formato &&&&&code&&&&&
	 */
	public String getDegeneratedString(final String code) {

		log.info("Crear cadena para: " + code);
		return "&&&&&" + code + "&&&&&";
	}

	/**
	 * Crea un mensaje para un componente determinado
	 * 
	 * @param severity
	 *            Severidad {@link FacesMessage}
	 * @param summary
	 *            Clave internacionalizada del sumario
	 * @param detail
	 *            Clave internacionalizada del detalle
	 * @param componentId
	 *            Nombre del componente,
	 *            {@link ControllerHelper#getClientId(String)}
	 */
	public void createFacesMessage(final Severity severity,
			final String summary, final String detail, final String componentId) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage msg = new FacesMessage(severity, getMessage(summary),
				getMessage(detail));
		facesContext.addMessage(componentId, msg);
	}

	/**
	 * 
	 * @param severity
	 *            Severidad {@link FacesMessage}
	 * @param summary
	 *            Clave internacionalizada del sumario
	 * @param detail
	 *            Clave internacionalizada del detalle
	 * @param componentId
	 *            Nombre del componente,
	 *            {@link ControllerHelper#getClientId(String)}
	 */
	public void createFacesMessageSimple(final Severity severity,
			final String summary, final String detail, final String componentId) {

		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage msg = new FacesMessage(severity, getMessage(summary),
				detail);
		facesContext.addMessage(componentId, msg);
	}

	/**
	 * Returns the clientId for a component with id, esto se usa por que JSF
	 * genera claves distintas a las que se configuran en los componentes, esto
	 * se hace para evitar que el mismo ID se repite, por ejemplo si ponemos en
	 * un form un label, con formID y labelID como sus IDs respectivamente, JSF
	 * generara identificadorse parecidos a: formID para el form, y
	 * formID:labelID para el label, esta funcion recibe como parametro
	 * "labelID" y retorna "formID:labelID".
	 * 
	 * @param id
	 *            de la vista del elemento a buscar
	 * @return id del componente del lado del cliente
	 */
	public String getClientId(final String id) {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		UIComponent c = findComponent(root, id);
		if (c == null) {
			throw new IllegalArgumentException(
					"NO se encontro comoponente con id " + id);
		}
		return c.getClientId(context);
	}

	/**
	 * Dado un ID (vease {@link ControllerHelper#getClientId(String)}) retorna
	 * el componente al que pertenece
	 * 
	 * @param id
	 *            id del cliente para obtener el componente
	 * @return Componente de vista
	 */
	public UIComponent findComponent(final String id) {

		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();

		UIComponent c = findComponent(root, id);
		return c;
	}

	/**
	 * Finds component with the given id
	 */
	private UIComponent findComponent(final UIComponent c, final String id) {

		if (id.equals(c.getId())) {
			return c;
		}
		Iterator<UIComponent> kids = c.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	/**
	 * Retorna una EL expression correspondiente a un metodo.
	 * 
	 * @param valueExpression
	 *            cadena que representa la expresion.
	 * @param expectedReturnType
	 *            clase del tipo que se espera que retorna la expresion
	 * @param expectedParamTypes
	 *            clase de los parametros esperados que reciba el metodo
	 * 
	 * @return {@link MethodExpression} correspondiente
	 */
	public MethodExpression createMethodExpression(
			final String valueExpression, final Class<?> expectedReturnType,
			final Class<?> ... expectedParamTypes) {

		MethodExpression methodExpression = null;
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExpressionFactory factory = fc.getApplication()
					.getExpressionFactory();
			methodExpression = factory.createMethodExpression(
					fc.getELContext(), valueExpression, expectedReturnType,
					expectedParamTypes);
		} catch (Exception e) {
			throw new FacesException("Method expression '" + valueExpression
					+ "' could not be created.");
		}

		return methodExpression;
	}

	/**
	 * Muestra una excepción con severidad de error
	 * 
	 * @param e
	 *            excepción a mostrar.
	 */
	public void showException(final Exception e) {

		createGlobalFacesMessage(FacesMessage.SEVERITY_ERROR, "",
				e.getMessage());
	}

	/**
	 * Escanea el archivo columns.xhtml donde se definen las columnas
	 * visualizadas en la grilla, y retorna las mismas.
	 * 
	 * @return Lista de columnas -> [header, field]
	 */
	public LinkedList<Column> getColumns() {

		String id = "idListEntities";
		LinkedList<Column> columns = new LinkedList<Column>();
		UIExtendedDataTable table = (UIExtendedDataTable) findComponent(id);

		for (UIComponent ui : table.getChildren()) {
			if (ui instanceof UIColumn) {
				ValueExpression expressionHeader = ((HtmlOutputText) ((UIColumn) ui)
						.getHeader()).getValueExpression("value");

				String header = ELParser.getHeaderColumn(expressionHeader
						.getExpressionString());

				for (UIComponent children : ui.getChildren()) {
					if (children instanceof HtmlOutputText) {
						HtmlOutputText text = (HtmlOutputText) children;
						ValueExpression expression = text
								.getValueExpression("value");

						String field = ELParser.getFieldByExpression(expression
								.getExpressionString());
						columns.add(new Column(header, field));
					}
				}
			}
		}
		return columns;
	}

	/**
	 * Clase que sirve de punto de acceso para convertir excepciones del tipo
	 * que sea a excepciones manejadas por el sistema, si no puede convertirla
	 * retorna la misma excepcion que recibio.
	 * <p>
	 * <b>Las excepciones manejadas son:</b>
	 * <ol>
	 * <li>{@link ConstraintViolationException}: delega su manejo a
	 * {@link UniqueHelper#createUniqueException(Exception, Class)}</li>
	 * </ol>
	 * </p>
	 * 
	 * @param e
	 *            excepción que se desea manejar
	 * @param clazz
	 *            clase padre de la cual proviene la excepcion, esto se usa para
	 *            obtener fields y anotaciones
	 * @return Excepción convertida, si se puede, y si no la misma
	 */
	public Exception convertException(final Exception e, final Class<?> clazz) {

		if (e instanceof ConstraintViolationException) {
			return uniqueHelper.createUniqueException(e, clazz);
		}
		return e;
	}

	/**
	 * Actualizamos los valores de todos los componentes hijos de un componente
	 * cuyo id se pasa como parámetro, no toma en cuenta validaciones y deberia
	 * funcionar igual a {@link UIComponentBase#processUpdates(FacesContext)}.
	 * <p>
	 * Se crea un nuevo método que realiza el trabajo ya que el
	 * {@link UIComponent#processUpdates(FacesContext)} no realiza correctamente
	 * su trabajo si no se valida antes el formulario, esto se debe a quel el
	 * flujo normal de JSF es primero validar y luego actualizar, que serían las
	 * fases 3 y 4.
	 * </p>
	 * <p>
	 * El proceso se describe a continuación
	 * <ol>
	 * <li>Obtención de elementos: se obtiene el componente a través de su ID,
	 * el contexto de faces y el contexto de las expresiones del lenguaje</li>
	 * <li>Se obtiene un {@link SIGHConverterV2} para convertir los combos</li>
	 * <li>Se itera sobre la lista de los hijos del componente a actualizar</li>
	 * <ol>
	 * <li>Se verifica si es un combobox, si este es el caso se convierte su
	 * valor y se actualiza el objeto</li>
	 * <li>Para otro caso, se obtiene su valor y se lo actualiza sin realizar
	 * validaciones</li>
	 * </ol>
	 * </p>
	 * 
	 * </ol> <b>Observaciones:</b> no se verifica si algun valor no es
	 * compatible con su destino, esto es si se ingresa una cadena en el lugar
	 * de un numero, se lanzara una excepcion del tipo
	 * 
	 * @param componentID
	 *            es el id del lado servidor del objeto que sera actualizado.
	 */
	public void updateModel(String componentID) {

		// Obtenemos el id

		UIComponent formulario = findComponent(componentID);
		_updateModel(formulario);
	}

	private void _updateModel(UIComponent formulario) {

		FacesContext context = FacesContext.getCurrentInstance();
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		Iterator<UIComponent> iter = formulario.getFacetsAndChildren();
		while (iter.hasNext()) {

			UIComponent component = iter.next();
			// Si es un valor submiteable, o INPUTEaBLE
			if (component instanceof HtmlSelectOneMenu) {
				HtmlSelectOneMenu com = (HtmlSelectOneMenu) component;
				Object newValue = com.getSubmittedValue();
				if (newValue == null) {
					continue;
				}
				ValueExpression value = com.getValueExpression("value");
				// Si tiene un converter definido, entonces utilizamos ese
				// converter para obtener el valor
				if (!(com.getConverter() == null)) {
					newValue = com.getConverter().getAsObject(context, com,
							newValue.toString());
				}
				value.setValue(elContext, newValue);
				continue;
			}
			if (component instanceof UIInput) {
				UIInput com = (UIInput) component;
				Object newValue = com.getSubmittedValue();
				ValueExpression value = com.getValueExpression("value");

				value.setValue(elContext, newValue);
			}
			_updateModel(component);
		}
	}
}
