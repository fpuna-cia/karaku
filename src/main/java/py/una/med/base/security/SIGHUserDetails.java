/*
 * @SGHUserDetails.java 1.0 10/12/12
 */

package py.una.med.base.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Esta clase implementa UserDetails, proporciona información básica del
 * usuario.
 *
 * @author Uriel González
 * @author Arturo Volpe
 * @version 1.0, 10/12/12
 * @since 1.0
 */
public class SIGHUserDetails implements Serializable, UserDetails {

	private static final long serialVersionUID = 5902809292219265597L;

	private String userName;

	private Map<String, SIGHUserGrantedAuthority> roles;

	/**
	 * Instancia una
	 */
	public SIGHUserDetails() {

		roles = new HashMap<String, SIGHUserGrantedAuthority>();
	}

	/**
	 * Retorna las autoridades otorgadas al usuario.
	 *
	 * @return las autoridades.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.roles.values();
	}

	/**
	 * Agrega una lista de permisos al usurio.
	 *
	 * @param listaRoles
	 *            lista que contiene los roles a añadir al usuario.
	 */
	public void addRoles(List<SIGHUserGrantedAuthority> listaRoles) {

		for (SIGHUserGrantedAuthority ga : listaRoles) {
			this.roles.put(ga.getAuthority(), ga);
		}
	}

	/**
	 * Define si un usuario tiene un rol.
	 *
	 * @param roleName
	 *            nombre del rol
	 * @return <code>true</code> si el usuario tiene el permiso,
	 *         <code>false</code> en caso contrario.
	 */
	public boolean hasRole(String roleName) {

		return roles.containsKey(roleName);
	}

	/**
	 * @param userName
	 *            userName para setear
	 */
	public void setUserName(String userName) {

		this.userName = userName;
	}

	/**
	 * Retorna el password usado para autenticar el usuario.
	 *
	 * @return el password.
	 */
	@Override
	public String getPassword() {

		// TODO Recuperar el password de ldap
		return null;
	}

	/**
	 * Retorna el nombre del usuario usado para autenticar el usuario.
	 *
	 * @return el nombre del usuario.
	 */
	@Override
	public String getUsername() {

		return this.userName;
	}

	/**
	 * Indica si la cuenta del usuario ha caducado. Una cuenta caducada no se
	 * puede autenticar.
	 *
	 * @return true si la cuenta del usuario es válida (es decir, que no haya
	 *         caducado), false si no es válida (es decir, caduco).
	 */
	@Override
	public boolean isAccountNonExpired() {

		// TODO Recuperar del ldap
		return true;
	}

	/**
	 * Indica si un usuario esta bloqueado o no. Un usuario bloqueado no se
	 * puede autenticar.
	 *
	 * @return true si el usuario esta bloqueado, false en caso contrario.
	 */
	@Override
	public boolean isAccountNonLocked() {

		// TODO Recuperar del ldap
		return true;
	}

	/**
	 * Indica si las credenciales del usuario (password) ha expirado.
	 * Credenciales caducadas impiden la autenticación.
	 *
	 * @return true si las credenciales del usuario son válidas (es decir, que
	 *         no haya caducado), false si ya no es válido (es decir, caducado)
	 */
	@Override
	public boolean isCredentialsNonExpired() {

		// TODO Recuperar del ldap
		return true;
	}

	/**
	 * Indica si el usuario esta habilitado o no. El usuario deshabilitado no se
	 * puede autenticar.
	 *
	 * @return true si el usuario esta habilitado, false en caso contrario.
	 */
	@Override
	public boolean isEnabled() {

		// TODO Recuperar del ldap
		return true;
	}

}
