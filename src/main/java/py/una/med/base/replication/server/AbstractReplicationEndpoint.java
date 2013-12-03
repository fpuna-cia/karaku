/*
 * @AbstractReplicationEndPoint.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.server;

import static py.una.med.base.util.Checker.notValid;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import py.una.med.base.replication.DTO;
import py.una.med.base.replication.Shareable;
import py.una.med.base.services.Converter;
import py.una.med.base.services.ConverterProvider;
import py.una.med.base.services.server.WebServiceDefinition;
import py.una.med.base.util.KarakuReflectionUtils;

/**
 * Clase base para los Endpoints que proveen replicación a través de WS. s
 * <p>
 * Facilta la implementación del mismo, por ejemplo, la replicación de una
 * entidad denominada Pais y su versión para enviar por la red PaisDTO*. Los
 * pasos necesarios para la creación del Endpoint son:
 * 
 * <ol>
 * <li>Crear los XSD de la clase PaisDTO.xsd</li>
 * <li>Crear los XSD para los mensajes, p.e.: PaisReplicationOperations.xsd</li>
 * <li>Generar las clases utilizando
 * 
 * <pre>
 * mvn jaxb2:xjc
 * </pre>
 * 
 * </li>
 * <li>Modificar la clase generada para que implemente la interfaz {@link DTO}</li>
 * <li>Crear un Endpoint (en el paquete py.una.med.<i>{@literal <}
 * sistema></i>.webservice.endpoint), con el nombre PaisReplicationEndpoint
 * 
 * <pre>
 * {@literal @}{@link WebServiceDefinition}(xsds =
 * 	{
 * 		"/META-INF/schemas/configuracion/pais/Pais.xsd",
 * 		"/META-INF/schemas/configuracion/pais/PaisReplicationOperations.xsd"
 * 	}
 * )
 * </pre>
 * 
 * </li>
 * <li>Agregar la anotación {@literal @}{@link WebServiceDefinition} y
 * configurar los <i>xsd's</i> anteriormente generados</li>
 * <li>Crear un método de la siguiente forma
 * 
 * <pre>
 * 
 * 
 * &#064;{@link PayloadRoot}(localPart = &quot;paisReplicationRequest&quot;, 
 * 	 namespace = "http://sigh.med.una.py/2013/schemas/configuracion")
 * &#064;{@link ResponsePayload}
 * public PaisReplicationResponse paisReplicationRequest(
 * 		&#064;{@link RequestPayload} PaisReplicationRequest request) {
 * 
 * 	notNull(request, &quot;Please provide a paisReplicationRequest object&quot;);
 * 	String lastId = notValid(request.getId(), &quot;Please provide a last ID&quot;);
 * 
 * 	PaisReplicationResponse response= new PaisReplicationResponse();
 * 
 * 	Bundle&lt;PaisDTO&gt; bundle = getChanges(lastId);
 * 	for (Change&lt;PaisDTO&gt; p : bundle) {
 * 		response.getPaisList().add(p.getEntity());
 * 	}
 * 	prr.setId(bundle.getLastId());
 * 
 * 	return response;
 * }
 * </pre>
 * 
 * <p>
 * Donde, la anotación PayloadRoot define el inicio del mensaje XML,
 * RequestPayload define lo que debe recibir como entrada, y ResponsePayload
 * define lo que retornara deberá ser convertido a XML.
 * </p>
 * <p>
 * Se nota que en la implementación, el único proceso manual que se realiza es
 * de validar los datos de entrada, crear un objeto respuesta y asignar cada
 * objeto del {@link Bundle} de cambios a la respuesta, además de asignarle un
 * ID.
 * </p>
 * 
 * </li>
 * <li>Se debe realizar un test de esto, para lo mismo se puede ver el ejemplo
 * de {@link import
 * py.una.med.base.test.test.replication.ReplicationEndpointTest}</li>
 * </ol>
 * </p>
 * 
 * <p>
 * Actualmente es el único cliente para {@link ReplicationProvider}.
 * </p>
 * 
 * <p>
 * * Siguiendo las convenciones sería una clase llamada Pais, por simplicidad
 * del ejemplo se utiliza el nombre PaisDTO.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
public class AbstractReplicationEndpoint<E extends Shareable, T extends DTO> {

	Class<E> clazzEntity;
	Class<T> clazzDTO;

	@Autowired
	ReplicationProvider replicationProvider;

	@Autowired
	private ConverterProvider converterProvider;

	Converter<E, T> converter;

	/**
	 * Crea una nueva instancia, recuperando información acerca de la entidad y
	 * su DTO (para obtener converters).
	 */
	public AbstractReplicationEndpoint() {

		clazzEntity = KarakuReflectionUtils.getParameterizedClass(this, 0);
		clazzDTO = KarakuReflectionUtils.getParameterizedClass(this, 1);

	}

	@PostConstruct
	void postConstruct() {

		converter = converterProvider.getConverter(clazzEntity, clazzDTO);
	}

	/**
	 * Retorna una lista de los cambios relacionados a la entidad.
	 * 
	 * <p>
	 * Se utiliza el {@link ReplicationProvider} para obtener todos los cambios.
	 * </p>
	 * 
	 * @param lastId
	 *            ultimo identificador
	 * @return {@link Bundle} de cambios, nunca <code>null</code>
	 */
	public Bundle<T> getChanges(String lastId) {

		notValid(lastId, "Can not get changes of invalid lastid");

		Bundle<E> changes = replicationProvider.getChanges(clazzEntity, lastId);

		Bundle<T> changesConverted = new Bundle<T>();

		if (changes != null) {
			for (Change<E> change : changes) {

				changesConverted.add(converter.toDTO(change.getEntity(), 0),
						change.getId());
			}
		}

		return changesConverted;
	}

	/**
	 * Retorna los cambios de un bundle como una lista de entidades.
	 * 
	 * @param bundle
	 *            set de cambios
	 * @return lista de objetos que cambiaron.
	 */
	public List<T> getAsList(Bundle<T> bundle) {

		List<T> toRet = new ArrayList<T>(bundle.size());
		for (Change<T> p : bundle) {
			toRet.add(p.getEntity());
		}
		return toRet;
	}
}
