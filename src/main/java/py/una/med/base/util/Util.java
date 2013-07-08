/**
 * @Util.0 10/05/13. Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.util.Calendar;
import java.util.Date;
import java.util.jar.Manifest;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Componente que provee funcionalidades básicas del sistema
 * 
 * @author Nathalia Ochoa
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.2 27/06/2013
 * 
 */
@Component
public class Util {

	@Autowired
	private Manifest manifest;

	/**
	 * Retorna el titulo de la aplicación, esto esta en el manifest
	 * 
	 * @return Titulo de la aplicación
	 */
	public String getTitle() {

		return manifest.getMainAttributes().getValue("specification-title");
	}

	/**
	 * Retorna la version actual de la aplicación
	 * 
	 * @return numero identificador de la version
	 */
	public String getVersion() {

		return manifest.getMainAttributes().getValue("specification-version");
	}

	/**
	 * Retorna true si la aplicación esta en estado de desarrollo y false si
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
	 * Retorna true si el entorno actual de ejecución es de Debug
	 * 
	 * @return true si se esta ejecutando en modo de depuración, false si se
	 *         esta ejecutando normalmente
	 */
	public boolean isDebug() {

		return java.lang.management.ManagementFactory.getRuntimeMXBean()
				.getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}

	/**
	 * Retorna la URI para crear links de logout para la aplicación
	 * 
	 * @return URL
	 */
	public String getLogoutUrl() {

		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestContextPath()
				+ "/j_spring_cas_security_logout";
	}

	/**
	 * Retorna la dirección desde donde esta accediendo el usuario de la sesión
	 * actual.
	 * 
	 * @return cadena en formato NNN.NNN.NNN.NNN que representa la dirección del
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

	/**
	 * Obtiene el nombre base del sistema.
	 * 
	 * @return
	 */
	public String getBaseName() {

		return getTitle().split("-")[0].trim();
	}

	/**
	 * Retorna el momento actual para el sistema.
	 * 
	 * @return {@link Date} del momento.
	 */
	public Date getCurrentTime() {

		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

}
