/**
 * @WSEndpointController 1.0 26/06/13. Sistema Integral de Gestion Hospitalaria
 */

package py.una.pol.karaku.services.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import py.una.pol.karaku.business.IKarakuBaseLogic;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.configuration.KarakuBaseConfiguration;
import py.una.pol.karaku.controller.IKarakuAdvancedController;
import py.una.pol.karaku.controller.KarakuAdvancedController;

/**
 * Controlador para la vista de manejo de URL's.
 * 
 * @author Arturo Volpe Torres
 * @since 2.1
 * @version 1.0 25/06/2013
 * 
 */
@Scope(value = KarakuBaseConfiguration.SCOPE_CONVERSATION)
@Controller
public class WSEndpointController extends
		KarakuAdvancedController<WSEndpoint, Long> implements
		IKarakuAdvancedController<WSEndpoint, Long> {

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
	public IKarakuBaseLogic<WSEndpoint, Long> getBaseLogic() {

		return logic;
	}

	@Override
	public String getDefaultPermission() {

		return util.get(DEFAULT_PERMISSION_KEY, DEFAULT_PERMISSION_D_VALUE);
	}

}
