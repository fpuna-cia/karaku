/*
 * @AuthorityController.java 1.0 21/02/2013
 */
package py.una.med.base.security;

import static py.una.med.base.util.Checker.notNull;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import py.una.med.base.util.StringUtils;

/**
 * Controller que se encarga de verificar permisos.
 * 
 * 
 * TODO cambiar a AuthorityService
 * 
 * @author Arturo Volpe
 * @version 1.1
 * @since 1.0
 * 
 */
@Service
public class AuthorityController {

	private static final Logger LOG = LoggerFactory
			.getLogger(AuthorityController.class);

	/**
	 * Método que se utiliza como punto único de acceso para la seguridad en
	 * cuento a vistas, esto es, todas las vistas deberían preguntar a este
	 * controller, si se tienen los permisos adecuados para visualizar un
	 * elemento, esto se hace mediante el tag, &lt security &gt.<br>
	 * Funciona buscando en la session el usuario actual de la aplicación,
	 * obtiene la autenticación, y un vector de autoridades (
	 * {@link SIGHUserGrantedAuthority}), compara el permiso que da cada una de
	 * esas autoridades con el permiso solicitado, si no tiene el permiso
	 * retorna falso, y loguea un mensaje de error
	 * 
	 * @param rol
	 *            a probar
	 * @return true si tiene permiso, false en otro caso
	 */
	public boolean hasRole(String rol) {

		return hasRoleStatic(rol);
	}

	/**
	 * Retorna el usuario que actualmente esta autenticado en el sistema.
	 * 
	 * @return {@link String} representando el nombre del usuario.
	 */
	@Nonnull
	public String getUsername() {

		return notNull(SecurityContextHolder.getContext().getAuthentication()
				.getName(), "Can't get the current username");
	}

	/**
	 * Retorna el usuario que actualmente esta autenticado en el sistema.
	 * 
	 * @return {@link String} representando el nombre del usuario.
	 * @see #getUsername()
	 */
	public static String getUsernameStatic() {

		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	/**
	 * Igual que {@link #hasRole(String)}, solamente que es un método estático,
	 * lo que permite a clases que no están en el mismo alcance (
	 * {@link WebApplicationContext#SCOPE_SESSION}) puedan realizar controles de
	 * permisos.
	 * 
	 * @param rol
	 *            nombre del permiso.
	 * @return <code>true</code> si se encuentra el permiso, <code>false</code>
	 *         en caso contrario.
	 */
	public static boolean hasRoleStatic(String rol) {

		if (StringUtils.isInvalid(rol)) {
			LOG.trace("Checking a invalid rol ({})", rol);
		}
		return ((SIGHUserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).hasRole(rol);

	}
}
