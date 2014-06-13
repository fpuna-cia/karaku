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

package py.una.pol.karaku.util;

import static py.una.pol.karaku.util.Checker.notNull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.Nonnull;
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
import javax.faces.component.UISelectOne;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import org.hibernate.exception.ConstraintViolationException;
import org.richfaces.component.UICalendar;
import org.richfaces.component.UIColumn;
import org.richfaces.component.UIExtendedDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.reports.Column;
import com.google.common.annotations.VisibleForTesting;

/**
 * Clase que implementa funcionalidades generales para la manipulacion de
 * vistas, proveen funcionalidades que ya integran todas las partes del sistema.
 * Es un singleton compartido por todas las sesiones
 * 
 * @author Arturo Volpe
 * @author Nathalia Ochoa
 * @since 2.0 08/02/2013
 * @version 2.0 19/02/2013
 */
@Component
public class ControllerHelper {

	private static final String NULL_SEVERITY_IS_NOT_ALLOWED = "Null severity is not allowed";
	private static final String EMPTY_STRING = "";
	private static final String EL_VALUE_PROPERTY = "value";

	@Autowired
	private UniqueHelper uniqueHelper;

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
			@Nonnull final String summary, @Nonnull final String detail) {

		createGlobalSimpleMessage(severity, getString(notNull(summary)),
				getString(notNull(detail)));
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

		createGlobalSimpleMessage(severity, getString(summary), null);
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

		createGlobalSimpleMessage(severity, summary, null);
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
	public void createGlobalSimpleMessage(@Nonnull final Severity severity,
			final String summary, final String detail) {

		notNull(severity, NULL_SEVERITY_IS_NOT_ALLOWED);

		String sum = summary == null ? EMPTY_STRING : summary;
		String det = detail == null ? EMPTY_STRING : detail;

		createFacesMessageSimple(severity, sum, det, null);
	}

	/**
	 * Retorna el mensaje internacionalizado del código dado, para claves no
	 * encontradas retorna &&&&&code&&&&&&
	 * 
	 * @param code
	 *            llave del mensaje
	 * @deprecated utilizar el {@link I18nHelper}
	 * @return cadena internacionalizada
	 */
	@Deprecated
	public String getMessage(final String code) {

		return i18nHelper.getString(code);
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

		createFacesMessageSimple(severity, getString(summary),
				getString(detail), componentId);
	}

	/**
	 * Emite un mensaje recibido como parámetro en el componente cuyo
	 * identificador es recibido como parámetro.
	 * 
	 * <p>
	 * Desde la version
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
	public void createFacesMessageSimple(@Nonnull final Severity severity,
			final String summary, final String detail, final String componentId) {

		notNull(severity, NULL_SEVERITY_IS_NOT_ALLOWED);

		addMessage(severity, summary, detail, componentId);
	}

	/**
	 * Agrega un mensaje con severidad <b>info</b> e internacionalizado.
	 * 
	 * @param summary
	 *            cadena del archivo de internacionalización del sumario del
	 *            mensaje
	 * @param params
	 *            parametros del mensaje.
	 */
	public void addInfoMessage(@Nonnull final String summary,
			final Object ... params) {

		addMessage(FacesMessage.SEVERITY_INFO,
				i18nHelper.getString(summary, params), null, null);
	}

	/**
	 * Agrega un mensaje con severidad warn a un componente.
	 * 
	 * @see I18nHelper#getString(String, Object...)
	 * @param id
	 *            identificador del componente (puede ser id de cliente o no).
	 * @param summary
	 *            sumario del mensaje
	 * @param detail
	 *            detalle
	 */
	public void addWarnMessage(@Nonnull final String id,
			@Nonnull final String summary, final String detail) {

		addMessage(FacesMessage.SEVERITY_WARN, summary, detail, id);
	}

	/**
	 * Agrega un mensaje con severidad <b>warn</b> e internacionalizado
	 * 
	 * @param summary
	 * @param params
	 */
	public void addGlobalWarnMessage(@Nonnull final String summary,
			final String detail) {

		addMessage(FacesMessage.SEVERITY_WARN, summary, detail, null);
	}

	/**
	 * Agrega un mensaje con severidad <b>warn</b>.
	 * 
	 * <p>
	 * Las cadenas pasadas ya deben estar internacionalizadas
	 * </p>
	 * 
	 * @param summary
	 * @param params
	 */
	public void addSimpleGlobalWarnMessage(@Nonnull final String summary,
			final String detail) {

		addMessage(FacesMessage.SEVERITY_WARN, summary, detail, null);
	}

	/**
	 * Agrega un nuevo mensaje, busca el ID si el mismo no es un id compuesto.
	 * 
	 * <h3>Consideraciones:</h3>
	 * <p>
	 * <b>El id ya puede ser del cliente</b> en este caso se realiza una
	 * verifiacion si posee el separador <code>:</code>, si es así entonces
	 * utiliza directamente el parámetro. En el caso de que sea un client id y
	 * no tenga <code>:</code> se realiza una búsqueda innecesaria.
	 * </p>
	 * <p>
	 * <b>Si el id es <code>null</code></b> entonces se crea un mensaje global
	 * </p>
	 * 
	 * @param severity
	 * @param summary
	 * @param detail
	 * @param id
	 * @since 2.0
	 */
	private void addMessage(FacesMessage.Severity severity, String summary,
			String detail, String id) {

		String realId = id;
		if (realId != null && realId.indexOf(':') == -1) {
			realId = getClientId(id);
		}
		getContext().addMessage(realId,
				new FacesMessage(severity, summary, detail));
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

		UIComponent c = findComponent(id);
		if (c == null) {
			throw new IllegalArgumentException(
					"NO se encontro comoponente con id " + id);
		}
		return c.getClientId(getContext());
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

		FacesContext context = getContext();
		UIViewRoot root = context.getViewRoot();

		return findComponent(root, id);
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
			FacesContext fc = getContext();
			ExpressionFactory factory = fc.getApplication()
					.getExpressionFactory();
			methodExpression = factory.createMethodExpression(
					fc.getELContext(), valueExpression, expectedReturnType,
					expectedParamTypes);
		} catch (Exception e) {
			throw new FacesException("Method expression '" + valueExpression
					+ "' could not be created.", e);
		}

		return methodExpression;
	}

	/**
	 * Escanea el archivo columns.xhtml donde se definen las columnas
	 * visualizadas en la grilla, y retorna las mismas.
	 * 
	 * @return Lista de columnas -> [header, field]
	 */
	public List<Column> getColumns() {

		String id = "idListEntities";
		List<Column> columns = new LinkedList<Column>();
		UIExtendedDataTable table = (UIExtendedDataTable) findComponent(id);

		for (UIComponent ui : table.getChildren()) {
			if (ui instanceof UIColumn) {
				Column toAdd = buildColumn(ui);
				if (toAdd != null) {
					columns.add(buildColumn(ui));
				}
			}
		}
		return columns;
	}

	/**
	 * @param columns
	 * @param ui
	 */
	private Column buildColumn(UIComponent ui) {

		ValueExpression expressionHeader = ((HtmlOutputText) ((UIColumn) ui)
				.getHeader()).getValueExpression(EL_VALUE_PROPERTY);

		String header = ELParser.getHeaderColumn(expressionHeader
				.getExpressionString());

		for (UIComponent children : ui.getChildren()) {
			if (children instanceof HtmlOutputText) {
				HtmlOutputText text = (HtmlOutputText) children;
				ValueExpression expression = text
						.getValueExpression(EL_VALUE_PROPERTY);

				String field = ELParser.getFieldByExpression(expression
						.getExpressionString());
				return new Column(header, field);
			}
		}
		return null;
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
	 * <li>Se obtiene un {@link KarakuConverter} para convertir los combos</li>
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
		updateModel(formulario);
	}

	private void updateModel(UIComponent formulario) {

		FacesContext context = getContext();
		ELContext elContext = getContext().getELContext();
		Iterator<UIComponent> iter = formulario.getFacetsAndChildren();
		while (iter.hasNext()) {

			UIComponent component = iter.next();
			// Si es un valor submiteable, o INPUTEaBLE
			if (component instanceof UISelectOne) {
				UISelectOne com = (UISelectOne) component;
				Object newValue = com.getSubmittedValue();
				ValueExpression value = com
						.getValueExpression(EL_VALUE_PROPERTY);
				if (newValue != null && com.getConverter() != null) {
					// Si tiene un converter definido, entonces utilizamos ese
					// converter para obtener el valor
					newValue = com.getConverter().getAsObject(context, com,
							newValue.toString());
				}
				value.setValue(elContext, newValue);
			} else if (component instanceof UICalendar) {
				UICalendar com = (UICalendar) component;
				Object newValue = com.getSubmittedValue();
				if (newValue != null) {
					ValueExpression value = com
							.getValueExpression(EL_VALUE_PROPERTY);
					newValue = getConverter().getAsObject(context, component,
							newValue.toString());
					value.setValue(elContext, newValue);
				}

			} else if (component instanceof UIInput
					&& !(component instanceof UICalendar)) {
				UIInput com = (UIInput) component;
				Object newValue = com.getSubmittedValue();
				ValueExpression value = com
						.getValueExpression(EL_VALUE_PROPERTY);
				if (value.getType(elContext).equals(Quantity.class)) {
					if (StringUtils.isValid(newValue)) {
						newValue = new Quantity((String) newValue);
					} else {
						newValue = Quantity.ZERO;
					}
				}

				if (newValue instanceof String
						&& !StringUtils.isValid(newValue)) {
					newValue = null;
				}
				value.setValue(elContext, newValue);
			}
			updateModel(component);
		}
	}

	private DateTimeConverter getConverter() {

		DateTimeConverter converter = new DateTimeConverter();
		converter.setPattern(FormatProvider.DATE_FORMAT);
		converter.setTimeZone(TimeZone.getDefault());
		return converter;
	}

	@VisibleForTesting
	protected String getRealId(String id) {

		if (id.indexOf(':') != -1) {
			return getClientId(id);
		}
		return id;
	}

	/**
	 * Finds component with the given id
	 */
	@VisibleForTesting
	protected UIComponent findComponent(final UIComponent c, final String id) {

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

	private String getString(final String code) {

		return i18nHelper.getString(code);
	}

	/**
	 * Retorna el contexto.
	 * 
	 * @return
	 */
	@VisibleForTesting
	protected FacesContext getContext() {

		return FacesContext.getCurrentInstance();
	}
}
