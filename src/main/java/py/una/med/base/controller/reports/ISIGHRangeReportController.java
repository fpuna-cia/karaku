package py.una.med.base.controller.reports;

import javax.faces.event.AjaxBehaviorEvent;

/**
 * 
 * 
 * @author Abrahan Fretes
 * @since 1.0
 * @version 1.0 29/01/2014
 * 
 */

public interface ISIGHRangeReportController {

	void onChangeDateBefore(AjaxBehaviorEvent event);

	void onChangeDateAfter(AjaxBehaviorEvent event);

}
