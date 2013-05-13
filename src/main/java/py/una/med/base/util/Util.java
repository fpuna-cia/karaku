/**
 * @Util.0 10/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.util.jar.Manifest;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Componente que provee funcionalidades basicas del sistema
 * 
 * @author Nathalia Ochoa
 * @since 1.0
 * @version 1.0 10/05/2013
 * 
 */
@Component
public class Util {

	@Autowired
	private Manifest manifest;

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
	 * Obtiene el nombre especifico del sistema actual.
	 * 
	 * @return
	 */
	public String getNameSystem() {

		return getTitle().split("-")[1];
	}
}
