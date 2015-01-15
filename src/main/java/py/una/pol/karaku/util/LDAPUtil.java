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
package py.una.pol.karaku.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.exception.KarakuRuntimeException;

/**
 * Clase para realizar operaciones de directorio en LDAP
 * 
 * @author Uriel Gonzalez
 * @author Romina Fernandez
 * 
 */
@Component
public class LDAPUtil {

	private static final String LDAP_SERVER_KEY = "ldap.server.host";

	private static final String LDAP_ADMIN_KEY = "ldap.user";

	private static final String LDAP_DN_KEY = "ldap.DN";

	private static final String LDAP_ADMIN_PASS_KEY = "ldap.user.password";

	/**
	 * Usuarios especiales que no deben mostrarse al usuario final.
	 * 
	 * TODO cambiar a propiedad
	 */
	private static final String LDAP_SPECIAL_USER_PREFIX = "sigh_";
	private static final String[] EXCLUDED_USERS = { "cas", "root",
			"configuracion", "desarrollador", "farmacia", "identificacion",
			"social", "stock", "ws_sigh" };

	@Autowired
	private PropertiesUtil propertiesUtil;

	private DirContext createInitialDirContext() {

		Map<Object, String> env = new HashMap<Object, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, propertiesUtil.get(LDAP_SERVER_KEY) + "/"
				+ propertiesUtil.get(LDAP_DN_KEY));
		env.put(Context.SECURITY_PRINCIPAL, propertiesUtil.get(LDAP_ADMIN_KEY));
		env.put(Context.SECURITY_CREDENTIALS,
				propertiesUtil.get(LDAP_ADMIN_PASS_KEY));

		try {
			return new InitialDirContext(new Hashtable<Object, String>(env));

		} catch (NamingException e) {
			throw new KarakuRuntimeException(e.getMessage(), e);
		}

	}

	/**
	 * Recupera los usuarios de LDAP
	 * 
	 * @return Una lista con los usuarios de LDAP
	 */
	public List<User> getUsers() {

		List<User> users = new ArrayList<User>();

		try {
			DirContext ctx = createInitialDirContext();

			Attributes matchAttrs = new BasicAttributes(true);
			matchAttrs.put(new BasicAttribute("uid"));

			NamingEnumeration<SearchResult> answer = ctx.search("ou=users",
					matchAttrs);

			while (answer.hasMore()) {
				SearchResult sr = answer.next();
				String uid = sr.getName().substring(4);
				// No se retornan los usuarios especiales
				if (!uid.startsWith(LDAP_SPECIAL_USER_PREFIX)
						&& !ListHelper.contains(EXCLUDED_USERS, uid)) {
					User user = new User();
					user.setUid(uid);
					Attributes atributos = sr.getAttributes();
					String cn = atributos.get("cn").toString().substring(4);
					user.setCn(cn);
					users.add(user);
				}
			}

		} catch (NamingException e) {
			throw new KarakuRuntimeException(e.getMessage(), e);
		}

		return users;

	}

	public static class User {

		private String uid;

		private String cn;

		public void setUid(String uid) {

			this.uid = uid;
		}

		public String getUid() {

			return this.uid;
		}

		public void setCn(String cn) {

			this.cn = cn;
		}

		public String getCn() {

			return this.cn;
		}

	}
}
