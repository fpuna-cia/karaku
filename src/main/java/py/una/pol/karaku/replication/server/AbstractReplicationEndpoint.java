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
package py.una.pol.karaku.replication.server;

import static py.una.pol.karaku.util.Checker.isValid;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.services.Converter;
import py.una.pol.karaku.services.ConverterProvider;
import py.una.pol.karaku.util.KarakuReflectionUtils;

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
 * {@literal @}{@link py.una.pol.karaku.services.server.WebServiceDefinition}(xsds =
 * 	{
 * 		"/META-INF/schemas/configuracion/pais/Pais.xsd",
 * 		"/META-INF/schemas/configuracion/pais/PaisReplicationOperations.xsd"
 * 	}
 * )
 * </pre>
 * 
 * </li>
 * <li>Agregar la anotación {@literal @}
 * {@link py.una.pol.karaku.services.server.WebServiceDefinition} y configurar los
 * <i>xsd's</i> anteriormente generados</li>
 * <li>Crear un método de la siguiente forma
 * 
 * <pre>
 * 
 * 
 * &#064;{@link org.springframework.ws.server.endpoint.annotation.PayloadRoot}(localPart = &quot;paisReplicationRequest&quot;, 
 * 	 namespace = "http://sigh.med.una.py/2013/schemas/configuracion")
 * &#064;{@link org.springframework.ws.server.endpoint.annotation.ResponsePayload}
 * public PaisReplicationResponse paisReplicationRequest(
 * 		&#064;{@link org.springframework.ws.server.endpoint.annotation.RequestPayload} PaisReplicationRequest request) {
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
 * py.una.pol.karaku.test.test.replication.ReplicationEndpointTest}</li>
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

	@Nonnull
	private Class<E> clazzEntity;
	@Nonnull
	private Class<T> clazzDTO;

	@Autowired
	private ReplicationProvider replicationProvider;

	@Autowired
	private ConverterProvider converterProvider;

	private Converter<E, T> converter;

	/**
	 * Crea una nueva instancia, recuperando información acerca de la entidad y
	 * su DTO (para obtener converters).
	 */
	public AbstractReplicationEndpoint() {

		clazzEntity = KarakuReflectionUtils.getParameterizedClass(this, 0);
		clazzDTO = KarakuReflectionUtils.getParameterizedClass(this, 1);

	}

	/**
	 * Crea una nueva instancia, recuperando información acerca de la entidad y
	 * su DTO (para obtener converters).
	 * 
	 * <p>
	 * Initialize puede ser false, si no se desea que se obtenga por reflexión
	 * los tipos parameterizados, si se utiliza este camino, se debe invocar a
	 * {@link #setClazzDTO(Class)} y {@link #setClazzEntity(Class)} para que el
	 * componente se mantenga consistente.
	 * </p>
	 * <p>
	 * Es útil utilizar este método con <code>false</code> para poder hacer que
	 * el componente mismo sea transaccional.
	 * </p>
	 */
	public AbstractReplicationEndpoint(@Nonnull Class<E> entity,
			@Nonnull Class<T> dto) {

		clazzEntity = entity;
		clazzDTO = dto;
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

		String id = isValid(lastId, "Can not get changes of invalid lastid");

		Bundle<E> changes = replicationProvider.getChanges(clazzEntity, id);

		Bundle<T> changesConverted = new Bundle<T>(changes.getLastId());

		for (Change<E> change : changes) {
			if (change == null) {
				continue;
			}
			changesConverted.add(converter.toDTO(change.getEntity(), 0),
					change.getId());
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

	/**
	 * Converter de entidad a DTO.
	 * 
	 * <p>
	 * El converter definido para convertir desde {@link #clazzEntity} a
	 * {@link #clazzDTO}, necesario para el envio por servicios.
	 * </p>
	 * 
	 * @return converter, nunca <code>null</code>.
	 */
	public Converter<E, T> getConverter() {

		return converter;
	}

}
