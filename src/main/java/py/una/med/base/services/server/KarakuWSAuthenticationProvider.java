/*
 * @KarakuWSAuthenticationProvider.java 1.0 Aug 6, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.services.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import py.una.med.base.security.SIGHUserDetails;
import py.una.med.base.security.SIGHUserService;

/**
 * Clase que provee autenticación para usuarios, esta pensada para ser usada con
 * un {@link BasicAuthenticationEntryPoint}, pero aún así y al heredar de
 * {@link AbstractUserDetailsAuthenticationProvider} provee un mecanismo para
 * autenticar cualquier mecanismo basado en Usuario y Password. <br />
 * <br />
 * <p>
 * Se basa en {@link SIGHUserService} para autenticar el usuario y luego para
 * obtener los permisos, esto se hace en dos fases, la primera es
 * {@link #retrieveUser(String, UsernamePasswordAuthenticationToken)} donde se
 * autentica el usuario y se autoriza el acceso a expresiones como
 * 'isAuthenticated()' y luego en
 * {@link #additionalAuthenticationChecks(UserDetails, UsernamePasswordAuthenticationToken)}
 * se cargan los permisos necesarios para que pueda navegar.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 6, 2013
 * @see UserDetails
 * 
 */
public class KarakuWSAuthenticationProvider extends
		AbstractUserDetailsAuthenticationProvider implements
		AuthenticationProvider {

	@Autowired
	private SIGHUserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.dao.
	 * AbstractUserDetailsAuthenticationProvider
	 * #additionalAuthenticationChecks(org
	 * .springframework.security.core.userdetails.UserDetails,
	 * org.springframework
	 * .security.authentication.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		userService.loadAuthorization(userDetails);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.dao.
	 * AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String,
	 * org
	 * .springframework.security.authentication.UsernamePasswordAuthenticationToken
	 * )
	 */
	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		if (userService.checkAuthenthicationByUID(username, authentication
				.getCredentials().toString())) {

			SIGHUserDetails user = new SIGHUserDetails();
			user.setUserName(username);
			return user;
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

}
