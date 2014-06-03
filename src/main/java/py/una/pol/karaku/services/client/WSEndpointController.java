/**
 * @WSEndpointController 1.0 26/06/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.med.base.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import py.una.med.base.business.ISIGHBaseLogic;
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.configuration.SIGHConfiguration;
import py.una.med.base.controller.ISIGHAdvancedController;
import py.una.med.base.controller.SIGHAdvancedController;

/**
 * Controlador para la vista de manejo de URL's.
 * 
 * @author Arturo Volpe Torres
 * @since 2.1
 * @version 1.0 25/06/2013
 * 
 */
@Scope(value = SIGHConfiguration.SCOPE_CONVERSATION)
@Controller
public class WSEndpointController extends
		SIGHAdvancedController<WSEndpoint, Long> implements
		ISIGHAdvancedController<WSEndpoint, Long> {

	@Autowired
	private PropertiesUtil util;

	/**
	 * Clave del archivo de propiedades que define el nombre del permiso que se
	 * necesita para poder manipular la tabla de servicios.
	 */
	public static final String DEFAULT_PERMISSION_KEY = "karaku.ws.client.endpoint.permission";

	/**
	 * Valor por defecto de la llave {@link #DEFAULT_PERMISSION_KEY}, sirve para
	 * determinar que permiso se requiere para manipular la tabla de servicios.
	 * 
	 * @see #DEFAULT_PERMISSION_KEY
	 */
	public static final String DEFAULT_PERMISSION_D_VALUE = "KARAKU_WS_MANAGEMENT";

	@Autowired
	private WSEndpointLogic logic;

	@Override
	public ISIGHBaseLogic<WSEndpoint, Long> getBaseLogic() {

		return logic;
	}

	@Override
	public String getDefaultPermission() {

		return util.get(DEFAULT_PERMISSION_KEY, DEFAULT_PERMISSION_D_VALUE);
	}

}
