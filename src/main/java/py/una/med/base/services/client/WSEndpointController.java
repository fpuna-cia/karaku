/**
 * @WSEndpointController 1.0 26/06/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.services.client;

/**
 * Controlador para la vista de manejo de URL's.
 * 
 * @author Arturo Volpe Torres
 * @since 1.2
 * @version 1.0 25/06/2013
 * 
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.controller.ISIGHAdvancedController;
import py.una.med.base.controller.SIGHAdvancedController;

@Scope(value = SIGHConfiguration.SCOPE_CONVERSATION)
@Controller
public class WSEndpointController extends
		SIGHAdvancedController<WSEndpoint, Long> implements
		ISIGHAdvancedController<WSEndpoint, Long> {

	@Autowired
	private WSEndpointLogic logic;

	@Override
	public ISIGHBaseLogic<WSEndpoint, Long> getBaseLogic() {

		return logic;
	}

}
