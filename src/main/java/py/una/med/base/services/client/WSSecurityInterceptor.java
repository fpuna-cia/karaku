/*
 * @WSSecurityHeaderInterceptor.java 1.0 Aug 5, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.services.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import javax.xml.transform.TransformerException;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;
import py.una.med.base.services.client.WSInformationProvider.Info;

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
	 * @param info
	 *            información de la llamada que se esta realizando
	 * @param message
	 *            mensaje que actualmente se esta enviando
	 */
	public void addSecurity(Info info, WebServiceMessage message) {

		Charset cs = Charset.forName(CharEncoding.UTF_8);
		TransportContext context = TransportContextHolder.getTransportContext();
		HttpUrlConnection connection = (HttpUrlConnection) context
				.getConnection();
		HttpURLConnection uRLConnection = connection.getConnection();
		String auth = CREDENTIALS_FORMAT.replace("USER", info.getUser())
				.replace("PASSWORD", info.getPassword());
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

		return new WebServiceMessageCallback() {

			@Override
			public void doWithMessage(WebServiceMessage message)
					throws IOException, TransformerException {

				addSecurity(info, message);
			}
		};
	}
}
