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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import py.una.pol.karaku.configuration.PropertiesUtil;

/**
 * Esta clase implementa UserDetailsService, permite recuperar datos del
 * usuario.
 * 
 * @author Uriel González
 * @author Arturo Volpe
 * @version 1.2, 08/08/13
 * @since 1.0
 */
public class KarakuUserService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory
			.getLogger(KarakuUserService.class);

	@Autowired
	private PropertiesUtil propertiesUtil;

	/**
	 * Patrón por defecto para buscar usuarios en el LDAP. Valor por defecto: <br />
	 * 
	 * <pre>
	 * {@code
	 * 		uid=UUU,ou=users,dc=med,dc=una,dc=py
	 * }
	 * </pre>
	 * 
	 */
	public static final String USER_PATTERN_DEFAULT = "uid=UUU,ou=users,dc=med,dc=una,dc=py";

	/**
	 * Clave del archivo de propiedades que se utiliza para obtener la ubicación
	 * del servidor Ldap. <br />
	 * Debe de ser un número IP o un nombre de la página.
	 * 
	 */
	private static final String LDAP_SERVER_KEY = "ldap.server.host";

	/**
	 * Usuario de LDAP usado por Spring Security para la autenticación
	 */
	private static final String LDAP_ADMIN_KEY = "ldap.user";

	private static final String LDAP_DN_KEY = "ldap.DN";

	/**
	 * Password del usuario de LDAP
	 */
	private static final String LDAP_ADMIN_PASS_KEY = "ldap.user.password";

	/**
	 * Key del archivo de propiedades (cambiantes o no) donde se almacena el
	 * patrón para buscar en el ldap.
	 * 
	 * @see #USER_PATTERN_DEFAULT
	 */
	public static final String USER_PATTERN_KEY = "ldap.user.pattern";

	private static final String USER_PATTERN_TO_REPLACE = "UUU";

	/**
	 * Llave del archivo de propiedades que define el permiso básico que debe
	 * tener un usuario para poder ingresar al sistema.
	 */
	public static final String BASIC_PERMISSION_KEY = "karaku.security.basic";

	private static final String BASIC_PERMISSION_KEY_DEFAULT = "SIGH";

	/**
	 * Localiza al usuario basándose en el nombre del usuario.
	 * 
	 * @param username
	 *            el nombre del usuario que identifica al usuario cuyos datos se
	 *            requiere.
	 * @return la información del usuario.
	 */
	@Override
	public UserDetails loadUserByUsername(String uid) {

		KarakuUser user = new KarakuUser();
		user.setUserName(uid);
		user.addRoles(loadAuthoritiesByDn(uid));

		String permiso = propertiesUtil.get(BASIC_PERMISSION_KEY,
				BASIC_PERMISSION_KEY_DEFAULT);

		boolean allow = false;
		for (GrantedAuthority o : user.getAuthorities()) {
			if (o.getAuthority().equals(permiso)) {
				allow = true;
			}
		}
		if (!allow) {
			throw new InsufficientAuthenticationException(
					"No posee privilegios para este sistema");
		}
		return user;
	}

	private List<KarakuPermission> loadAuthoritiesByDn(String uid) {

		List<KarakuPermission> listaRoles = new ArrayList<KarakuPermission>();

		try {
			DirContext ctx = getInitialDirContext(
					propertiesUtil.get(LDAP_ADMIN_KEY),
					propertiesUtil.get(LDAP_ADMIN_PASS_KEY));
			Attributes matchAttrs = new BasicAttributes(true);
			matchAttrs.put(new BasicAttribute("member", getRealUsername(uid)));
			NamingEnumeration<SearchResult> answer = ctx.search(
					"ou=permissions", matchAttrs);

			while (answer.hasMore()) {
				SearchResult searchResult = answer.next();
				Attributes attributes = searchResult.getAttributes();
				Attribute attr = attributes.get("cn");
				String rol = (String) attr.get();
				KarakuPermission grantedAuthority = new KarakuPermission(rol);
				listaRoles.add(grantedAuthority);
			}

			return listaRoles;
		} catch (NamingException e) {
			LOG.warn("Can't create Ldap Context", e);
			return Collections.emptyList();
		}
	}

	/**
	 * Carga los permisos a un usuario.
	 * 
	 * @param user
	 *            {@link KarakuUser} donde se agregan los usuarios.
	 * @return {@link KarakuUser} con los permisos cargados.
	 */
	public UserDetails loadAuthorization(UserDetails user) {

		KarakuUser karakuUser = (KarakuUser) user;
		karakuUser.addRoles(loadAuthoritiesByDn(user.getUsername()));
		return user;
	}

	/**
	 * Verifica si el usuario (Pasando usuario y password), son usuarios
	 * válidos. <br />
	 * Que un usuario sea válido implica que se puede acceder al LDAP con las
	 * credenciales proveídas. <br />
	 * Los parámetros de este método deben estar correctamente configurados para
	 * realizar la llamada, si desea llamar utilizando solamente el DN, ver
	 * {@link #checkAuthenthicationByUID(String, String)}
	 * 
	 * @param username
	 *            usuario con el formato necesario para realizar la consulta.
	 * @param password
	 *            contraseña del usuario.
	 * @return <code>true</code> si se puede crear un contexto, es decir
	 */
	public boolean checkAuthenthication(String username, String password) {

		try {
			getInitialDirContext(username, password).close();
			return true;
		} catch (NamingException ne) {
			LOG.trace("User don't have rights", ne);
			return false;
		}
	}

	private InitialDirContext getInitialDirContext(String user, String pass)
			throws NamingException {

		Map<Object, String> env = new HashMap<Object, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, getServerLocation());

		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, pass);
		return new InitialDirContext(new Hashtable<Object, String>(env));
	}

	/**
	 * Verifica si el usuario (Pasando usuario y password), son usuarios
	 * válidos. <br />
	 * Que un usuario sea válido implica que se puede acceder al LDAP con las
	 * credenciales proveídas. <br />
	 * 
	 * @param username
	 *            usuario del ldap (uid).
	 * @param password
	 *            contraseña del usuario.
	 * @return <code>true</code> si se puede crear un contexto, es decir
	 * @see #USER_PATTERN_KEY
	 */
	public boolean checkAuthenthicationByUID(String uid, String password) {

		return checkAuthenthication(getRealUsername(uid), password);
	}

	private String getRealUsername(String uid) {

		String realUsername = propertiesUtil.get(USER_PATTERN_KEY,
				USER_PATTERN_DEFAULT);
		return realUsername.replace(USER_PATTERN_TO_REPLACE, uid);
	}

	private String getServerLocation() {

		String server = propertiesUtil.get(LDAP_SERVER_KEY);
		String dn = propertiesUtil.get(LDAP_DN_KEY);
		return server + "/" + dn;
	}
}
