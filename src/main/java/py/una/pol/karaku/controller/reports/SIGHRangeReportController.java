package py.una.pol.karaku.controller.reports;

import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import org.richfaces.component.UICalendar;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.util.ControllerHelper;
import py.una.pol.karaku.util.DateUtils;
import py.una.pol.karaku.util.FormatProvider;
import py.una.pol.karaku.util.I18nHelper;

/**
 * 
 * Controlador que maneja la validación de rangos de fechas, compara dos fechas
 * y verifica que estas esten dentro de un rango significativo.
 * 
 * <p>
 * Cuenta con dos funciones
 * 
 * {@link #onChangeDateAfter(AjaxBehaviorEvent)}
 * {@link #onChangeDateBefore(AjaxBehaviorEvent)} para realizar la validación
 * del rango.
 * </p>
 * 
 * <p>
 * Para agregar las variables al Map{@literal <}String, Object> se debe utilizar
 * {@link #DATE_BEFORE}, para la fecha anterior {@link #DATE_AFTER}, para la
 * fecha posterior.
 * </p>
 * 
 * <p>
 * Los identificadores de los componentes de fecha deben ser <b>fecha_hasta</b>
 * y <b>fecha_desde</b>.
 * </p>
 * 
 * <h3>Ejemplo</h3>
 * 
 * <p>
 * Incluir lo siguiente en el archivo de fields de reportes
 * </p>
 * 
 * <pre>
 * 	&lt;h:outputText value="#{msg['ENTREGA_ARTICULO_REPORT_FECHA_DESDE']}" />
 * 	&lt;rich:calendar id="<b>fecha_desde</b>"
 * 		value="#{controller.filterOptions['ENTREGA_ARTICULO_REPORT_FECHA_DESDE']}"
 * 		datePattern="#{fp.dateFormat}" enableManualInput="true"
 * 		required="true">
 * 		&lt;a4j:ajax event="change" listener="<b>#{controller.onChangeDateBefore}</b>"
 * 			render="fecha_desde" />
 * 	&lt;/rich:calendar>
 * 	&lt;rich:message for="fecha_desde" />
 * 
 * 	&lt;h:outputText value="#{msg['ENTREGA_ARTICULO_REPORT_FECHA_HASTA']}" />
 * 	&lt;rich:calendar id="<b>fecha_hasta</b>"
 * 		value="#{controller.filterOptions['ENTREGA_ARTICULO_REPORT_FECHA_HASTA']}"
 * 		datePattern="#{fp.dateFormat}" enableManualInput="true"
 * 		required="true">
 * 		&lt;a4j:ajax event="change" listener="<b>#{controller.onChangeDateAfter}</b>"
 * 			render="fecha_hasta" />
 * 	&lt;/rich:calendar>
 * 	&lt;rich:message for="fecha_hasta" />
 * </pre>
 * 
 * <p>
 * Donde <b>controller</b> debe ser modificado por el nombre del controlador
 * </p>
 * 
 * @author Abrahan Fretes
 * @since 1.0
 * @version 1.0 29/01/2014S
 * 
 */
public abstract class SIGHRangeReportController<T, K extends Serializable>
		extends SIGHBaseReportController<T, K> implements
		ISIGHRangeReportController {

	/**
	 * Define una fecha desde la cual se generará el reporte.
	 */
	public static final String DATE_BEFORE = "REPORT_FECHA_DESDE";

	/**
	 * Define una fecha limite para incluir registros en el reporte a generar.
	 */
	public static final String DATE_AFTER = "REPORT_FECHA_HASTA";

	private static final String MESSAGE_AFTER = "REPORT_MESSAGE_AFTER";
	private static final String MESSAGE_BEFORE = "REPORT_MESSAGE_BEFORE";
	private static final String FECHA = "FECHA";

	@Autowired
	private FormatProvider formatProvider;

	@Autowired
	private ControllerHelper controllerHelper;

	@Autowired
	private I18nHelper i18nHelper;

	/**
	 * Validación del Rango de fechas.
	 * 
	 * <p>
	 * Considera válida una fecha posterior, si la fecha anterior es nula, o la
	 * fecha posterior ocurrio despues
	 * </p>
	 * 
	 * <p>
	 * Genera un mensaje de error en el componente en el caso de que la fecha no
	 * sea posterior
	 * </p>
	 **/
	@Override
	public void onChangeDateBefore(AjaxBehaviorEvent event) {

		Date dateBefore = (Date) ((UICalendar) event.getComponent()).getValue();
		Date dateAfter = (Date) getFilterOptions().get(DATE_AFTER);

		if (DateUtils.isBeforeOrEqual(dateBefore, dateAfter)) {
			getFilterOptions().put(DATE_BEFORE, dateBefore);
		} else {
			controllerHelper.createFacesMessageSimple(
					FacesMessage.SEVERITY_WARN,
					createMessageDate(dateAfter, MESSAGE_BEFORE),
					createMessageDate(dateAfter, MESSAGE_BEFORE),
					controllerHelper.getClientId("fecha_desde"));
			getFilterOptions().put(DATE_BEFORE, null);
		}
	}

	/**
	 * Validación del Rango de fechas.
	 * 
	 * <p>
	 * Considera válida una fecha posterior, si la fecha anterior es nula, o la
	 * fecha posterior ocurrio despues
	 * </p>
	 * 
	 * <p>
	 * Genera un mensaje de error en el componente en el caso de que la fecha no
	 * sea posterior
	 * </p>
	 **/
	@Override
	public void onChangeDateAfter(AjaxBehaviorEvent event) {

		Date dateAfter = (Date) ((UICalendar) event.getComponent()).getValue();
		Date dateBefore = (Date) getFilterOptions().get(DATE_BEFORE);

		if (DateUtils.isAfterOrEqual(dateBefore, dateAfter)) {
			getFilterOptions().put(DATE_AFTER, dateAfter);
		} else {
			controllerHelper.createFacesMessageSimple(
					FacesMessage.SEVERITY_WARN,
					createMessageDate(dateBefore, MESSAGE_AFTER),
					createMessageDate(dateBefore, MESSAGE_AFTER),
					controllerHelper.getClientId("fecha_hasta"));
			getFilterOptions().put(DATE_AFTER, null);
		}
	}

	private String createMessageDate(Date date, String msg) {

		return i18nHelper.getString(msg).replaceAll(FECHA,
				formatProvider.asDate(date));
	}
}
