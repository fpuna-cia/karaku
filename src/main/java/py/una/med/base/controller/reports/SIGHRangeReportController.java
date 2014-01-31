package py.una.med.base.controller.reports;

import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import org.richfaces.component.UICalendar;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.util.ControllerHelper;
import py.una.med.base.util.DateUtils;
import py.una.med.base.util.FormatProvider;
import py.una.med.base.util.I18nHelper;

/**
 * 
 * 
 * @author Abrahan Fretes
 * @since 1.0
 * @version 1.0 29/01/2014S
 * 
 *          Controlador que maneja la validación de rangos de fechas, compara
 *          dos fechas y verifica que estas esten dentro de un rango
 *          significativo. Cuenta con dos funciones
 * 
 *          - onChangeDateAfter(AjaxBehaviorEvent event -
 *          onChangeDateAfter(AjaxBehaviorEvent event)
 * 
 *          Para agregar las varaibles al Map<String, Object> se debe utilizar
 * 
 *          REPORT_FECHA_DESDE, para la fecha anterior REPORT_FECHA_HASTA, para
 *          la fecha posterior
 * 
 * 
 */

public abstract class SIGHRangeReportController<T, K extends Serializable>
		extends SIGHBaseReportController<T, K> implements
		ISIGHRangeReportController {

	private static final String DATE_BEFORE = "REPORT_FECHA_DESDE";
	private static final String DATE_AFTER = "REPORT_FECHA_HASTA";

	private static final String MESSAGE_AFTER = "REPORT_MESSAGE_DESDE";
	private static final String MESSAGE_BEFORE = "REPORT_MESSAGE_BEFORE";
	private static final String FECHA = "FECHA";

	@Autowired
	private FormatProvider formatProvider;

	@Autowired
	private ControllerHelper controllerHelper;

	@Autowired
	private I18nHelper i18nHelper;

	/**
	 * Validación del Rango de fechas
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

		String message = i18nHelper.getString(msg);
		return message.replaceAll(FECHA, formatProvider.asDate(date));
	}
}
