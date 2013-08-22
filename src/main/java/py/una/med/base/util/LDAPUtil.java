package py.una.med.base.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
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
import py.una.med.base.configuration.PropertiesUtil;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 * Clase para realizar operaciones de directorio en LDAP
 * 
 * @author Uriel Gonzalez
 * 
 */
@Component
public class LDAPUtil {

	private static final String LDAP_SERVER_KEY = "ldap.server.host";

	private static final String LDAP_ADMIN_KEY = "ldap.user";

	private static final String LDAP_DN_KEY = "ldap.DN";

	private static final String LDAP_ADMIN_PASS_KEY = "ldap.user.password";

	// Los usuarios especiales son utilizado por los sistemas para autenticarse.
	// Estos usuarios no se deben de mostrar al usuario final
	private static final String LDAP_SPECIAL_USER_PREFIX = "sigh_";

	@Autowired
	private PropertiesUtil propertiesUtil;

	private DirContext createInitialDirContext() {

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, propertiesUtil.get(LDAP_SERVER_KEY) + "/"
				+ propertiesUtil.get(LDAP_DN_KEY));
		env.put(Context.SECURITY_PRINCIPAL, propertiesUtil.get(LDAP_ADMIN_KEY));
		env.put(Context.SECURITY_CREDENTIALS,
				propertiesUtil.get(LDAP_ADMIN_PASS_KEY));

		try {
			DirContext ctx = new InitialDirContext(env);
			return ctx;

		} catch (NamingException e) {
			throw new KarakuRuntimeException(e.getMessage(), e.getCause());
		}

	}

	/**
	 * Recupera los usuarios de LDAP
	 * 
	 * @return Una lista con los usuarios de LDAP
	 */
	public List<String> getUsers() {

		List<String> users = new ArrayList<String>();

		try {
			DirContext ctx = createInitialDirContext();

			Attributes matchAttrs = new BasicAttributes(true);
			matchAttrs.put(new BasicAttribute("uid"));

			NamingEnumeration<SearchResult> answer = ctx.search("ou=users",
					matchAttrs);

			while (answer.hasMore()) {
				SearchResult sr = answer.next();
				String uid = sr.getName().substring(4);
				// No ser retornan los usuarios especialess
				if (!uid.startsWith(LDAP_SPECIAL_USER_PREFIX)) {
					users.add(uid);
				}
			}

		} catch (NamingException e) {
			throw new KarakuRuntimeException(e.getMessage(), e.getCause());
		}

		return users;

	}
}
