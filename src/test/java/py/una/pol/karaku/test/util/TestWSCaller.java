/*
 * @TestWSCaller.java 1.0 Oct 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.test.util;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import java.io.IOException;
import javax.xml.transform.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.support.MarshallingSource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.ResponseMatcher;
import py.una.pol.karaku.services.client.WSCallBack;
import py.una.pol.karaku.services.client.WSCaller;
import py.una.pol.karaku.services.client.WSInformationProvider.Info;

/**
 * {@link WSCaller} utilizado para fines de test.
 *
 * <p>
 * Omite la url y utiliza un entorno simulado.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 21, 2013
 *
 */
public class TestWSCaller extends WSCaller {

	@Autowired
	MockWebServiceClient client;

	@Autowired
	Marshaller marshaller;

	@Autowired
	Unmarshaller unmarshaller;

	@Override
	public <T> void call(final Object request, final Info info,
			final WSCallBack<T> callBack) {

		if (callBack == null) {
			throw new IllegalArgumentException("CallBack no puede ser nulo");
		}

		// TODO agregar callback#onfailure
		client.sendRequest(
				withPayload(new MarshallingSource(marshaller, request)))
				.andExpect(new ResponseMatcher() {

					@Override
					public void match(WebServiceMessage request,
							WebServiceMessage response) throws IOException,
							AssertionError {

						WebServiceMessage dos = response;
						Source source = dos.getPayloadSource();
						@SuppressWarnings("unchecked")
						T result = (T) unmarshaller.unmarshal(source);
						callBack.onSucess(result);
					}
				});

	}

	/**
	 * Retorna un {@link ResponseMatcher} que marshaliza el resultado.
	 * 
	 * <p>
	 * Este {@link ResponseMatcher} no realiza ninguna acción, siempre valida y
	 * se encarga de parsear el resultado
	 * </p>
	 * 
	 * @return dummy {@link ResponseMatcher}
	 */
	public ResponseMatcher responseMarshaller() {

		return new ResponseMatcher() {

			@Override
			public void match(WebServiceMessage request,
					WebServiceMessage response) throws IOException,
					AssertionError {

			}
		};
	}
}
