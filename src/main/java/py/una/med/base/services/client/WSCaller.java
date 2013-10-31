/*
 * @WSCaller.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import java.util.concurrent.Executor;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.med.base.exception.KarakuException;
import py.una.med.base.services.client.WSInformationProvider.Info;

/**
 * <p>
 * Clase que sirve de abstracción para llamada a servicios del tipo SOAP, provee
 * las funcionalidades básicas y debe ser heredada por cualquier Servicio que
 * desee realizar invocaciones.
 * </p>
 * <p>
 * El objetivo de esta clase es que todas las llamadas se realicen mediante
 * ella. Además asegura que todas las llamadas sean asíncronas, funcionamiento
 * que es deseado dentro de Karaku.
 * </p>
 * <p>
 * Esta implementación inyecta un {@link Executor}, encapsula todas sus llamadas
 * (no validaciones) en un {@link Runnable} y lo lanza.
 * </p>
 * <h3>
 * Formas de utilización</h3>
 * <p>
 * Para poder utilizar estos métodos, se muestran los siguientes ejemplos, La
 * clase Controller invoca a un servicio de obtención de personas mediante la
 * URI de la misma, y la clase PersonImporter hereda de {@link WSCaller} para
 * proveer mecanismos de llamadas a los servicios.
 * </p>
 *
 * <pre>
 *    {@literal @}{@link Component}
 *    public class Controller {
 *
 *        {@literal @}{@link Autowired}
 *        PersonImporter personImporter;  // Clase que injecta  {@link WSCaller}
 *
 *        public <b>{@link Void}</b> loadData() {
 *             personImporter.getByUri(
 *                 uri,
 *                 new {@link WSCallBack}&lt;List&lt;Persona&gt;&gt;() {
 *                     public void onSucess(List&lt;Persona&gt; result) {
 *                         for (Persona p&#160;: result) {
 *                             System.out.println(p.getNombre());
 *                         }
 *                     }
 *                     public void onFailure({@link KarakuException} ke) {
 *                         System.out.println(ke.getMessage());
 *                     }
 *                });
 *         }
 *         ...
 *    }
 *
 *    {@literal @}{@link Component}
 *    public class PersonImporter extends {@link WSCaller} {
 *
 *        public <b>{@link Void}</b> getByUri(String uri, {@link WSCallBack}&lt;List&lt;Persona&gt;&gt; WSCallBack) {
 *            PersonaRequest Object = new PersonaRequest(uri);
 *            {@link #call(Object, WSCallBack)};
 *        }
 *
 *    }
 * </pre>
 *
 * <b>Si invocamos de la siguiente manera</b>
 *
 * <pre>
 * System.out.printLn(&quot;Llamando al servicio&quot;);
 * controler.loadData();
 * System.out.printLn(&quot;Finalizando la llamada&quot;);
 * </pre>
 *
 * <b>Producirá la siguiente salida</b>
 *
 * <pre>
 * Llamando al servicio
 * Finalizando la llamada
 * Álvar Núñez Cabeza de Vaca
 * </pre>
 *
 * <p>
 * Siendo <b>Álvar Núñez Cabeza de Vaca</b> el resultado del servicio, podemos
 * notar que no son métodos síncronos, por ello la importancia del
 * {@link WSCallBack}.
 * </p>
 *
 *
 *
 * @author Arturo Volpe
 * @since 2.1.3
 * @version 1.0 Jun 11, 2013
 * @see WSCallBack
 * @see Info
 * @see #call(Object, WSCallBack) Invocación normal
 * @see #call(Object, Info, WSCallBack) Invocación TypeSafe
 * @see #call(Object, Class, WSCallBack) Invocación con información del servicio
 *
 */
@Component
public class WSCaller {

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Template utilizado para realizar llamadas al servicio a través de la
	 * Abstracción de Spring
	 */
	@Autowired
	private WebServiceTemplate template;

	/**
	 * Proveedor de URL's, se inyecta la interfaz por que cualquier
	 * implementación sirve.
	 */
	@Autowired
	private WSInformationProvider provider;

	@Autowired
	private Executor executor;

	@Autowired
	private WSSecurityInterceptor interceptor;

	/**
	 * Invoca a un servicio en un hilo paralelo y ejecuta al {@link WSCallBack}
	 * una vez finalizado.
	 * <p>
	 * Para obtener información acerca del servicio utiliza la clase de
	 * <code>request</code>, por ello no puede ser nula.
	 * </p>
	 * <p>
	 * Se puede notar además, de que este método no es TypeSafe, es decir, este
	 * método puede ser invocado pasando parámetros que no tienen sentido, como
	 * un {@link WSCallBack} que no corresponde al resultado del servicio.
	 * </p>
	 *
	 * @param request
	 *            no nula, la información que será serializada y enviada.
	 * @param callback
	 *            clase que será invocada cuando se retorne el servicio.
	 * @see #call(Object, Class, WSCallBack)
	 */
	public <T> void call(@NotNull T request, @NotNull WSCallBack<?> callback) {

		if (request == null) {
			throw new IllegalArgumentException("Request can not be null");
		}
		Info info = provider.getInfoByReturnType(request.getClass());
		call(request, info, callback);
	}

	/**
	 * Invoca a un servicio, este método es TypeSafe, pues también recibe el
	 * tipo de la respuesta que espera.
	 * <p>
	 * Utiliza el parámetro <code>request</code> para definir el tipo de
	 * servicio al que desea invocar. El {@link WSCallBack} debe de ser del tipo
	 * de la respuesta (<code>responseType</code>), pues se espera que la misma
	 * sea de ese tipo.
	 * </p>
	 * <p>
	 * Este método es TypeSafe, es decir nunca dará un error de casteo entre
	 * clases, su utilización es muy similar a la de
	 * {@link #call(Object, WSCallBack)}.
	 * </p>
	 *
	 * @see #call(Object, WSCallBack)
	 * @param request
	 *            no nula, la información que será serializada.
	 * @param responseType
	 *            tipo de la respuesta para des-serializar
	 * @param callback
	 *            clase que será invocada con la respuesta
	 */
	public <T, K> void call(@NotNull T request, Class<K> responseType,
			@NotNull WSCallBack<K> callback) {

		if (request == null) {
			throw new IllegalArgumentException("Request can not be null");
		}
		Info info = provider.getInfoByReturnType(request.getClass());
		call(request, info, callback);
	}

	/**
	 * Invoca al servicio definido en <code>info</code>, la invocación se
	 * realiza en otro hilo, encapsula todos los
	 * {@link WebServiceMessageCallback} definidos en
	 * {@link WSSecurityInterceptor#getWebServiceMessageCallback(Info)} para
	 * agregar la seguridad.
	 * <p>
	 * Este método es el punto de acceso a todos los servicios de Karaku.
	 * </p>
	 *
	 * @param request
	 *            objeto que será serializado para realizar la invocación
	 * @param info
	 *            información acerca del servicio.
	 * @param callBack
	 *            callBack que será invocado cuando se reciba la llamada del
	 *            servicio.
	 */
	public <T> void call(final Object request, final Info info,
			final WSCallBack<T> callBack) {

		if (callBack == null) {
			throw new IllegalArgumentException("CallBack no puede ser nulo");
		}

		executor.execute(new Runnable() {

			@Override
			public void run() {

				try {
					log.debug("Calling WebService with uri {}", info.getUrl());
					@SuppressWarnings("unchecked")
					T toRet = (T) template.marshalSendAndReceive(info.getUrl(),
							request,
							interceptor.getWebServiceMessageCallback(info));
					log.debug("Web service call ended");
					callBack.onSucess(toRet);
				} catch (Exception e) {
					callBack.onFailure(new KarakuException(e));
				}
			}
		});

	}

}
