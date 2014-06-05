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
package py.una.pol.karaku.services.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import py.una.pol.karaku.security.KarakuUser;
import py.una.pol.karaku.security.KarakuUserService;

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
 * Se basa en {@link KarakuUserService} para autenticar el usuario y luego para
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
	private KarakuUserService userService;

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

			KarakuUser user = new KarakuUser();
			user.setUserName(username);
			return user;
		}
		throw new UsernameNotFoundException(username);
	}

}
