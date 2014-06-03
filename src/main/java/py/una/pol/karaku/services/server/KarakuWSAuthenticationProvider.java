/*
 * @KarakuWSAuthenticationProvider.java 1.0 Aug 6, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.services.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import py.una.med.base.security.SIGHUserDetails;
import py.una.med.base.security.SIGHUserService;

/**
 * Clase que provee autenticación para usuarios.
 * 
 * <p>
 * Esta pensada para ser usada con un
 * {@link org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint}
 * , pero aún así y al heredar de
 * {@link AbstractUserDetailsAuthenticationProvider} provee un mecanismo para
 * autenticar cualquier mecanismo basado en Usuario y Password.
 * </p>
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

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) {

		userService.loadAuthorization(userDetails);
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication) {

		if (userService.checkAuthenthicationByUID(username, authentication
				.getCredentials().toString())) {

			SIGHUserDetails user = new SIGHUserDetails();
			user.setUserName(username);
			return user;
		}
		throw new UsernameNotFoundException(username);
	}

}
