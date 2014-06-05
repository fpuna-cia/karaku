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
package py.una.pol.karaku.services.client;

import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;
import py.una.pol.karaku.services.client.WSInformationProvider.Info;

/**
 * {@link org.springframework.ws.client.support.interceptor.ClientInterceptor}
 * que se encarga de agregar seguridad a todas las peticiones realizadas por
 * Karaku.
 * 
 * @author Arturo Volpe
 * @since 2.2.
 * @version 1.0 Aug 5, 2013
 * @see #getWebServiceMessageCallback(Info)
 * @see #addSecurity(Info, WebServiceMessage)
 */
@Component
public class WSSecurityInterceptor {

	/**
	 * Nombre del parámetro, es un estándar definido en el RFC.
	 * 
	 * 
	 * @see <a
	 *      href="http://www.w3.org/Protocols/HTTP/1.0/spec.html#Authorization"
	 *      >http://www.w3.org/Protocols/HTTP/1.0/spec.html#Authorization</a>
	 */
	public static final String AUTHORIZATION_HEADER_PARAM = "Authorization";
	/**
	 * Formato de las credenciales. <br />
	 */
	public static final String HEADER_CREDENTIALS_FORMAT = "Basic CREDENTIALS";
	public static final String CREDENTIALS_FORMAT = "USER:PASSWORD";

	/**
	 * Agrega seguridad a una llamada a un servicio, para ello agrega dos header
	 * params, pertenecientes a Usuario y Password.
	 * 
	 * @param user
	 *            usuario de la llamada
	 * @param password
	 *            contraseña del que invoca el servicio.
	 * @param message
	 *            mensaje que actualmente se esta enviando
	 */
	public void addSecurity(String user, String password,
			WebServiceMessage message) {

		Charset cs = Charset.forName(CharEncoding.UTF_8);
		TransportContext context = TransportContextHolder.getTransportContext();
		HttpUrlConnection connection = (HttpUrlConnection) context
				.getConnection();
		HttpURLConnection uRLConnection = connection.getConnection();
		String auth = CREDENTIALS_FORMAT.replace("USER", user).replace(
				"PASSWORD", password);
		byte[] encode = Base64.encode(auth.getBytes(cs));
		uRLConnection.addRequestProperty(AUTHORIZATION_HEADER_PARAM,
				HEADER_CREDENTIALS_FORMAT.replace("CREDENTIALS", new String(
						encode, cs)));
	}

	/**
	 * Método que crea una nueva instancia de {@link WebServiceMessageCallback}
	 * y le agrega un Interceptor que se encarga de invocar al método
	 * {@link #addSecurity(Info, WebServiceMessage)}, el cual añade la seguridad
	 * necesaria.
	 * 
	 * @see #addSecurity(Info, WebServiceMessage)
	 * @param info
	 *            información de la llamada
	 * @return {@link WebServiceMessageCallback} que agrega seguridad antes de
	 *         llamar
	 */
	public WebServiceMessageCallback getWebServiceMessageCallback(
			final Info info) {

		return createCallback(info.getUser(), info.getPassword());
	}

	/**
	 * Método que crea una nueva instancia de {@link WebServiceMessageCallback}
	 * y le agrega un Interceptor que se encarga de invocar al método
	 * {@link #addSecurity(Info, WebServiceMessage)}, el cual añade la seguridad
	 * necesaria.
	 * 
	 * @see #addSecurity(Info, WebServiceMessage)
	 * @param info
	 *            información de la llamada
	 * @return {@link WebServiceMessageCallback} que agrega seguridad antes de
	 *         llamar
	 */
	public WebServiceMessageCallback getWebServiceMessageCallback(
			final WSEndpoint endpoint) {

		return createCallback(endpoint.getUser(), endpoint.getPassword());
	}

	/**
	 * @param info
	 * @return
	 */
	private WebServiceMessageCallback createCallback(final String user,
			final String pass) {

		return new WebServiceMessageCallback() {

			@Override
			public void doWithMessage(WebServiceMessage message) {

				addSecurity(user, pass, message);
			}
		};
	}
}
