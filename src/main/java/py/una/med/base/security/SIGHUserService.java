/*
 * @SGHUserService.java 1.0 10/12/12
 */
package py.una.med.base.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Esta clase implementa UserDetailsService, permite recuperar datos del
 * usuario.
 * 
 * @author Uriel González
 * @version 1.0, 10/12/12
 * @since 1.0
 */
public class SIGHUserService implements UserDetailsService {

	/**
	 * Ubicacion del servidor Ldap
	 */
	private String ldapServer;
	
	//Usuario de LDAP usado por Spring Security para la autenticación
	private String ldapUser;
	
	//Password del usuario de LDAP
	private String ldapUserPassword;
	
	/**
	 * Localiza al usuario basándose en el nombre del usuario.
	 * @param username el nombre del usuario que identifica al usuario cuyos datos se requiere.
	 * @return la información del usuario.
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		return new SIGHUserDetails(username, this.ldapServer, this.ldapUser, this.ldapUserPassword);
	}

	
	public String getLdapServer() {
		return this.ldapServer;
	}

	
	public void setLdapServer(String ldapServer) {
		this.ldapServer = ldapServer;
	}
	
	public String getLdapUser(){
		return this.ldapUser;
	}
	
	public void setLdapUser(String ldapUser){
		this.ldapUser = ldapUser;
	}
	
	public String getLdapUserPassword(){
		return this.ldapUserPassword;
	}
	
	public void setLdapUserPassword(String ldapUserPassword){
		this.ldapUserPassword = ldapUserPassword;
	}
	
}