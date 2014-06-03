/*
 * @SGHUserGrantedAuthority.java 1.0 10/10/12
 */

package py.una.pol.karaku.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Esta clase implementa GrantedAuthority, el cual representa una
 * autoridad otorgada a un objecto de autenticación.
 * 
 * @author Uriel González
 * @version 1.0, 10/10/12
 * @since 1.0
 */
public class SIGHUserGrantedAuthority implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;
	
	private String rol;
	
	SIGHUserGrantedAuthority(String rol){
		this.rol = rol;
	}
	
	/**
	 * Retorna la autoridad.
	 * @return la autoridad.
	 */
	public String getAuthority() {
		return this.rol;
	}

}