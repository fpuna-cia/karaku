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
	 * Este {@link ResponseMatcher} no realiza ninguna acciÃ³n, siempre valida y
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
