/**
 * @MenuController 1.0 21/02/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.controller;

import java.io.IOException;
import java.util.Date;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.security.HasRole;
import py.una.pol.karaku.util.Util;

/**
 * Controlador que provee funcionalidades transversales para la aplicación, se
 * utiliza para proveer información al template principal, y también provee
 * ciertas funciones de utilidad para todas las vistas.
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 21, 2013
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class MenuController {

	@Autowired
	private Util util;

	@Autowired
	private PropertiesUtil properties;

	/**
	 * Retorna la URI para crear links de logout para la aplicación
	 * 
	 * @return URL
	 */
	public String getLogoutUrl() {

		return util.getLogoutUrl();
	}

	/**
	 * Retorna el titulo de la aplicación, esto esta en el manifest
	 * 
	 * @return Titulo de la aplicación
	 */
	public String getTitle() {

		return util.getTitle();
	}

	/**
	 * Retorna el nombre base del sistema
	 * 
	 * @return Nombre base del sistema
	 **/
	public String getBaseName() {

		return properties.get("application.baseName");
	}

	/**
	 * Retorna el nombre especifico de la aplicacion
	 * 
	 * @return Nombre especifico de la aplicacion
	 **/
	public String getAppName() {

		return properties.get("application.appName");
	}

	/**
	 * Retorna la version actual de la aplicación
	 * 
	 * @return numero identificador de la version
	 */
	public String getVersion() {

		return util.getVersion();
	}

	/**
	 * Retorna la dirección desde donde esta accediendo el usuario de la sesión
	 * actual.
	 * 
	 * @return cadena en formato NNN.NNN.NNN.NNN que representa la dirección del
	 *         cliente
	 */
	public String getIpAdress() {

		return util.getIpAdress();
	}

	/**
	 * Retorna true si la aplicación esta en estado de desarrollo y false si
	 * esta en otro estado.
	 * 
	 * @see javax.faces.application.ProjectStage
	 * @return true si es develop, false en otro caso
	 */
	public boolean isDevelop() {

		return util.isDevelop();
	}

	/**
	 * Retorna true si el entorno actual de ejecución es de Debug
	 * 
	 * @return true si se esta ejecutando en modo de depuración, false si se
	 *         esta ejecutando normalmente
	 */
	public boolean isDebug() {

		return util.isDebug();
	}

	/**
	 * Retorna la fecha actual del sistema.
	 * 
	 * @return {@link Date} representando el momento actual
	 * @see py.una.pol.karaku.util.Util#getCurrentTime()
	 */
	public Date getCurrentTime() {

		return util.getCurrentTime();
	}

	/**
	 * Método que redirige a la página principal.
	 **/
	public void toIndex() {

		FacesContext ctx = FacesContext.getCurrentInstance();
		String indexUrl = properties.get("application.homePage")
				+ "?breadcrum_reset=true";
		try {
			ctx.getExternalContext().redirect(indexUrl);
		} catch (IOException e) {
			throw new KarakuRuntimeException("Can not redirect", e);
		}
	}

	public String throwRuntimeException() {

		throw new KarakuRuntimeException("Excepcion");
	}

	@HasRole("PERMISO NO EXISTENTE")
	public String throwAccessDeniedException() {

		return "";
	}
}
