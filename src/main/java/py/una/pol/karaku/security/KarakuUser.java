/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2014, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
 * 		2012-2013, Centro Nacional de Computación, Universidad Nacional de Asunción.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package py.una.pol.karaku.security;

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
public class KarakuUser implements Serializable, UserDetails {

	private static final long serialVersionUID = 5902809292219265597L;

	private String userName;

	private Map<String, KarakuPermission> roles;

	/**
	 * Instancia una
	 */
	public KarakuUser() {

		roles = new HashMap<String, KarakuPermission>();
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
	public void addRoles(List<KarakuPermission> listaRoles) {

		for (KarakuPermission ga : listaRoles) {
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
