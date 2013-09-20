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

	}

	@Override
	public PhaseId getPhaseId() {

		return PhaseId.RENDER_RESPONSE;
	}

}
