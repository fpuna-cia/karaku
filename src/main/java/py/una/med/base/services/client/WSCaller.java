/*
 * @WSCaller.java 1.0 Jun 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.services.client;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.med.base.exception.KarakuException;

/**
 * Clase que sirve de abstracción para llamada a servicios del tipo SOAP, provee
 * las funcionalidades básicas y debe ser heredada por cualquier Servicio que
 * desee realizar invocaciones.
 * 
 * El objetivo de esta clase es que todas las llamadas se realizen mediante
 * ella. Además asegura que todas las llamadas sean asíncronas, funcionamiento
 * que es deseado dentro de karaku.
 * 
 * Esta implementación inyecta un {@link Executor}, encapsula todas sus llamadas
 * (no validaciones) en un {@link Runnable} y lo lanza.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Jun 11, 2013
 * 
 */
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
	private WSURLProvider provider;

	@Autowired
	private Executor executor;

	public <T> void call(T request, WSCallBack<?> callback) {

		if (request == null) {
			throw new IllegalArgumentException("Request can not be null");
		}
		String url = provider.getByReturnType(request.getClass());
		call(request, url, callback);
	}

	public <T, K> void call(T request, Class<K> responseType,
			WSCallBack<K> callback) {

		if (request == null) {
			throw new IllegalArgumentException("Request can not be null");
		}
		String url = provider.getByReturnType(request.getClass());
		call(request, url, callback);
	}

	public <T> void call(final Object request, final String url,
			final WSCallBack<T> callBack) {

		if (callBack == null) {
			throw new IllegalArgumentException("CallBack no puede ser nulo");
		}

		executor.execute(new Runnable() {

			@Override
			public void run() {

				try {
					log.debug("Calling WebService with uri %d", url);
					@SuppressWarnings("unchecked")
					T toRet = (T) template.marshalSendAndReceive(url, request);
					log.debug("Web service call ended");
					callBack.onSucess(toRet);
				} catch (Exception e) {
					callBack.onFailure(new KarakuException(e));
				}
			}
		});
	}

}
