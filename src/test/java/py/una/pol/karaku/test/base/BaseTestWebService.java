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
package py.una.pol.karaku.test.base;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import java.io.IOException;
import javax.xml.transform.Source;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.support.MarshallingSource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.ResponseActions;
import org.springframework.ws.test.server.ResponseMatcher;
import py.una.pol.karaku.test.configuration.WebServiceTestConfiguration;

/**
 * 
 * Base clase para Test que simulen llamadas a WebServices.
 * 
 * <h3>Beans Disponibles</h3>
 * <p>
 * Esta hecha para funcionar en conjunto con {@link WebServiceTestConfiguration}
 * y disponibiliza los beans mas importantes para la manipulación de servicios.
 * <ol>
 * <li>{@link #marshaller} Sirve para convertir un objeto a un XML</li>
 * <li>
 * {@link #unmarshaller} Sirve para convertir un XML a un objeto</li>
 * </ol>
 * </p>
 * <h3>Métodos Disponibles</h3>
 * <p>
 * La mayoría de los métodos para realizar verificaciónes sobre las respuesta de
 * los servicios se encuentran en
 * {@link org.springframework.ws.test.server.RequestCreators} (se recomienda
 * hacer static import de los métodos de esta clase).
 * </p>
 * <p>
 * Además se proveen métodos muy utilices para la manipulación de llamadas a
 * través de objetos y no de XML, los cuales son (en orden de uso):
 * <ol>
 * <li>{@link #getPayload(Object)} retorna un {@link Source} válido para enviar
 * a través de un WS <b>(OPCIONAL)</b></li>
 * <li> {@link #sendRequest(Object)} que envía una petición con el objeto,
 * primero lo <i>marshalea</i> y luego busca el endpoint correspondiente.</li>
 * <li> {@link #getResponse()} obtiene la respuesta de la última llamada
 * realizada con {@link #sendRequest(Object)}</li>
 * </ol>
 * </p>
 * <h3>Ejemplo de uso</h3>
 * <p>
 * 
 * <pre>
 * &#064;ContextConfiguration(loader = AnnotationConfigContextLoader.class)
 * public class ClientTest extends BaseTestWebService {
 * 
 * 	&#064;Configuration
 * 	static class ContextConfiguration extends WebServiceTestConfiguration {
 * 
 * 		&#064;Override
 * 		(1) public Class&lt;?&gt;[] getClassesToBound() {
 * 
 * 			return TestUtils.getAsClassArray(MenuRequest.class,
 * 					MenuResponse.class);
 * 		}
 * 
 * 
 * 		(2) ServerLogic ServerLogic() {
 * 
 * 			return new ServerLogic();
 * 		}
 * 
 * 
 * 		&#064;Bean
 * 		(3) MenuServiceEndpoint menuServiceEndpoint() {
 * 
 * 			return new MenuServiceEndpoint();
 * 		}
 * 
 * 	}
 * 
 * 	&#064;Test
 * 	public void testCallMocked() {
 * 
 * 
 * 		(4) WSRequest wsRequest = new WSRequest();
 * 		(5) sendRequest(wsRequest).
 * 		(6) 	andExpect(noFault());
 * 		(7) MenuResponse response = getResponse();
 * 		(8) assertEquals(&quot;Stock&quot;, response.getMenu().getName());
 * 
 * 	}
 * }
 * 
 * 
 * </pre>
 * 
 * <h4>Explicación</h4>
 * <ol>
 * <li>Clases que serán marshalizadas o desmarshalizadas</li>
 * <li>Beans que utiliza nuestro método servidor</li>
 * <li>Endpoint para escuchar llamadas</li>
 * <li>Creamos un objeto para enviar</li>
 * <li>Enviamos la petición</li>
 * <li>Podemos agregar tantos resultados como queramos</li>
 * <li>Obtenemos la respuesta ya casteada</li>
 * <li>Validamos el objeto ya convertido</li>
 * </ol>
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 18, 2013
 * @see WebServiceTestConfiguration
 * 
 */
public abstract class BaseTestWebService extends BaseTest {

	@Autowired
	protected Marshaller marshaller;

	@Autowired
	protected Unmarshaller unmarshaller;

	@Autowired
	protected MockWebServiceClient client;

	private ResponseActions current;

	private Object result;

	@Before
	public void createMock() {

		result = null;
	}

	/**
	 * Envia una petición al servidor emulado.
	 * <p>
	 * Se diferencia de {@link MockWebServiceClient#sendRequest(RequestCreator)}
	 * en que agrega un validador que almacena desmarshaliza la respuesta y la
	 * almacena para poder utilizarla despues mediante el método
	 * {@link #getResponse()}
	 * </p>
	 * 
	 * @param requestCreator
	 * @return
	 * @see org.springframework.ws.test.server.MockWebServiceClient#sendRequest(org.springframework.ws.test.server.RequestCreator)
	 */
	public ResponseActions sendRequest(RequestCreator requestCreator) {

		current = client.sendRequest(requestCreator).andExpect(
				responseMarshaller());
		return current;
	}

	/**
	 * Serializa un objeto y envía la petición.
	 * 
	 * <p>
	 * <b>La clase del objeto debe ser una de las retornadas por
	 * {@link WebServiceTestConfiguration#getClassesToBound()}</b>
	 * </p>
	 * <p>
	 * Se diferencia de {@link MockWebServiceClient#sendRequest(RequestCreator)}
	 * en que agrega un validador que almacena desmarshaliza la respuesta y la
	 * almacena para poder utilizarla después mediante el método
	 * {@link #getResponse()}
	 * </p>
	 * 
	 * @param object
	 *            objeto a serializar
	 * @return {@link ResponseActions} para agregar validaciones
	 */
	public ResponseActions sendRequest(Object object) {

		return sendRequest(withPayload(getPayload(object)));
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

				WebServiceMessage dos = response;
				Source source = dos.getPayloadSource();
				result = unmarshaller.unmarshal(source);
			}
		};
	}

	/**
	 * Retorna el resultado del ultimo {@link #sendRequest(Object)} válido (en
	 * este test actual).
	 * 
	 * <p>
	 * Para que este método funcione, se debio agregar al menos un
	 * {@link #responseMarshaller()}
	 * </p>
	 * 
	 * @return respuesta marshalizada.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getResponse() {

		return (T) result;
	}

	/**
	 * Marshaliza un objeto y lo agrega a un {@link Source}
	 * 
	 * <p>
	 * <b>La clase del objeto debe ser una de las retornadas por
	 * {@link WebServiceTestConfiguration#getClassesToBound()}</b>
	 * </p>
	 * 
	 * @param o
	 *            Objeto a serializar
	 * @return {@link Source}
	 */
	public Source getPayload(Object o) {

		return new MarshallingSource(marshaller, o);
	}

}
