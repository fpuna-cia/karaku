/*
 * @AuthorityController.java 1.0 21/02/2013
 */
package py.una.med.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import py.una.med.base.security.SIGHUserGrantedAuthority;

/**
 * Controller que se encarga de verificar permisos.
 * 
 * @author Arturo Volpe
 * @version 1.1
 * @since 1.0
 * 
 */
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class AuthorityController {

	private static final Logger log = LoggerFactory
			.getLogger(AuthorityController.class);

	/**
	 * Metodo que se utiliza como punto unico de acceso para la seguridad en
	 * cuento a vistas, esto es, todas las vistas deberian preguntar a este
	 * controller, si se tienen los permisos adecuados para visualizar un
	 * elemento, esto se hace mediante el tag, &lt security &gt.<br>
	 * Funciona buscando en la session el usuario actual de la aplicacion,
	 * obtiene la autenticacion, y un vector de autoridades (
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
	
	public static boolean hasRoleStatic(String rol) {

		Object[] sighUserGrantedAuthorities = SecurityContextHolder
				.getContext().getAuthentication().getAuthorities().toArray();
		if (sighUserGrantedAuthorities == null
				|| sighUserGrantedAuthorities.length == 0) {
			log.warn("Usuario sin permisos");
			return false;
		}

		for (Object o : sighUserGrantedAuthorities) {
			SIGHUserGrantedAuthority authority = (SIGHUserGrantedAuthority) o;
			if (authority.getAuthority().equals(rol)){
				return true;
			}
		}
		
		log.debug("Usuario sin permiso: {}", rol);
		return false;
	}
}
