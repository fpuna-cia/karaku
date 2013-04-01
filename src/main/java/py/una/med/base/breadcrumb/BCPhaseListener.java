/*
 * @BCPhaseListener 1.1 18/02/2013 Sistema integral de Gestion Hospitalaria
 */
package py.una.med.base.breadcrumb;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * {@link PhaseListener} que se encarga de filtrar los request para verificar si
 * hay que agregar un nuevo registro al breadcrum
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.1 18/02/2013
 * 
 */
@Deprecated
public class BCPhaseListener implements PhaseListener {

	private static final long serialVersionUID = -8467642233780593017L;

	@Override
	public void afterPhase(final PhaseEvent arg0) {

	}

	@Override
	public void beforePhase(final PhaseEvent phaseEvent) {

		// BreadcrumbController mc = getBreadcrumController();
		// FacesContext fc = phaseEvent.getFacesContext();
		//
		// String url = phaseEvent.getFacesContext().getViewRoot().getViewId();
		// if ("true".equals(fc.getExternalContext().getRequestParameterMap()
		// .get(BreadcrumbController.BREADCRUM_VARIABLE))) {
		// mc.clearBreadcrum();
		// mc.setBreadcrum(url);
		// } else {
		// mc.addBreadcrum(url);
		// }

	}

	@Override
	public PhaseId getPhaseId() {

		return PhaseId.RENDER_RESPONSE;
	}

	/**
	 * Retorna el controller del breadCrum qeu se esta utilizando, este metodo
	 * existe por que no hay inyeccion de dependencias fuera de Spring
	 * 
	 * @return {@link BreadcrumbController} actual
	 */
	// private BreadcrumbController getBreadcrumController() {
	//
	// FacesContext facesContext = FacesContext.getCurrentInstance();
	// BreadcrumbController breadcrumController = facesContext
	// .getApplication().evaluateExpressionGet(facesContext,
	// "#{breadcrumController}", BreadcrumbController.class);
	// return breadcrumController;
	// }

}
