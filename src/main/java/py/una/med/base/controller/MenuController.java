/**
 * @MenuController 1.0 21/02/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.controller;

import javax.faces.application.ProjectStage;
import javax.faces.bean.ManagedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.util.Util;

/**
 * Controlador que provee funcionalidades transversales para la aplicacion, se
 * utiliza para proveer informacion al template principal, y tambien provee
 * ciertas funciones de utilidad para todas las vistas.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
@Controller
@ManagedBean
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class MenuController {

	@Autowired
	private Util util;

	/**
	 * Retorna la URI para crear links de logout para la aplicacion
	 * 
	 * @return URL
	 */
	public String getLogoutUrl() {

		return util.getLogoutUrl();
	}

	/**
	 * Retorna el titulo de la aplicacion, esto esta en el manifest
	 * 
	 * @return Titulo de la aplicacion
	 */
	public String getTitle() {

		return util.getTitle();
	}

	/**
	 * Retorna la version actual de la aplicacion
	 * 
	 * @return numero identificador de la version
	 */
	public String getVersion() {

		return util.getVersion();
	}

	/**
	 * Retorna la direccion desde donde esta accediento el usuario de la sesion
	 * actual.
	 * 
	 * @return cadena en formato NNN.NNN.NNN.NNN que representa la direccion del
	 *         cliente
	 */
	public String getIpAdress() {

		return util.getIpAdress();
	}

	/**
	 * Retorna true si la aplicacion esta en estado de desarrollo y false si
	 * esta en otro estado.
	 * 
	 * @see ProjectStage
	 * @return true si es develop, false en otro caso
	 */
	public boolean isDevelop() {

		return util.isDevelop();
	}

	/**
	 * Retorna true si el entorno actual de ejecucion es de Debug
	 * 
	 * @return true si se esta debugeando, false si se esta ejecutando
	 *         normalmente
	 */
	public boolean isDebug() {

		return util.isDebug();
	}
}
