/*
 * @MenuController.java 1.0 21/02/2013
 */
package py.una.med.base.controller;

import java.util.jar.Manifest;
import javax.faces.application.ProjectStage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
	private Manifest manifest;

	/**
	 * Retorna la URI para crear links de logout para la aplicacion
	 * 
	 * @return URL
	 */
	public String getLogoutUrl() {

		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath()
				+ "/j_spring_cas_security_logout";
	}

	/**
	 * Retorna el titulo de la aplicacion, esto esta en el manifest
	 * 
	 * @return Titulo de la aplicacion
	 */
	public String getTitle() {

		return manifest.getMainAttributes().getValue("specification-title");
	}

	/**
	 * Retorna la version actual de la aplicacion
	 * 
	 * @return numero identificador de la version
	 */
	public String getVersion() {

		return manifest.getMainAttributes().getValue("specification-version");
	}

	/**
	 * Retorna la direccion desde donde esta accediento el usuario de la sesion
	 * actual.
	 * 
	 * @return cadena en formato NNN.NNN.NNN.NNN que representa la direccion del
	 *         cliente
	 */
	public String getIpAdress() {

		return ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest().getRemoteAddr();
	}

	/**
	 * Retorna true si la aplicacion esta en estado de desarrollo y false si
	 * esta en otro estado.
	 * 
	 * @see ProjectStage
	 * @return true si es develop, false en otro caso
	 */
	public boolean isDevelop() {

		return FacesContext.getCurrentInstance().isProjectStage(
				ProjectStage.Development);
	}

	/**
	 * Retorna true si el entorno actual de ejecucion es de Debug
	 * 
	 * @return true si se esta debugeando, false si se esta ejecutando
	 *         normalmente
	 */
	public boolean isDebug() {

		return java.lang.management.ManagementFactory.getRuntimeMXBean()
				.getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}
}
