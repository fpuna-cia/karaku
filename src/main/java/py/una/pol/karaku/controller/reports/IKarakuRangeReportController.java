/*
 * 
 */
package py.una.pol.karaku.controller.reports;

import javax.faces.event.AjaxBehaviorEvent;

/**
 * 
 * 
 * @author Abrahan Fretes
 * @since 1.0
 * @version 1.0 29/01/2014
 * 
 */

public interface IKarakuRangeReportController {

	void onChangeDateBefore(AjaxBehaviorEvent event);

	void onChangeDateAfter(AjaxBehaviorEvent event);

}
