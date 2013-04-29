/*
 * @SGHUserDetails.java 1.0 10/12/12
 */

package py.una.med.base.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Esta clase implementa UserDetails, proporciona información básica del
 * usuario.
 * 
 * @author Uriel González
 * @version 1.0, 10/12/12
 * @since 1.0
 */
public class SIGHUserDetails implements Serializable, UserDetails {

	private static Logger logger = LoggerFactory
			.getLogger(SIGHUserDetails.class);

	private static final long serialVersionUID = 1L;

	private String userName;
	private List<GrantedAuthority> listaRoles;

	public SIGHUserDetails(String user, String ldapServer, String ldapUser,
			String ldapUserPassword) {

		logger.info("Recuperando detalles del usuario: " + user);

		this.userName = user;

		// Nos conectamos al ldap
		Hashtable<Object, String> env = new Hashtable<Object, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ldapServer);

		// env.put(Context.SECURITY_AUTHENTICATION, "DIGEST-MD5");
		env.put(Context.SECURITY_PRINCIPAL, ldapUser);
		env.put(Context.SECURITY_CREDENTIALS, ldapUserPassword);

		this.listaRoles = new ArrayList<GrantedAuthority>();

		try {
			DirContext ctx = new InitialDirContext(env);
			Attributes matchAttrs = new BasicAttributes(true);
			matchAttrs.put(new BasicAttribute("member", "uid=" + user
					+ ",ou=users,dc=med,dc=una,dc=py"));
			NamingEnumeration<SearchResult> answer = ctx.search(
					"ou=permissions", matchAttrs);

			while (answer.hasMore()) {
				SearchResult searchResult = answer.next();
				Attributes attributes = searchResult.getAttributes();
				Attribute attr = attributes.get("cn");
				String rol = (String) attr.get();
				SIGHUserGrantedAuthority grantedAuthority = new SIGHUserGrantedAuthority(
						rol);
				listaRoles.add(grantedAuthority);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retorna las autoridades otorgadas al usuario.
	 * 
	 * @return las autoridades.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.listaRoles;
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
