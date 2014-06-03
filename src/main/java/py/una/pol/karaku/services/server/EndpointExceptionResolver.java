package py.una.med.base.services.server;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import py.una.med.base.exception.HTTPException;
import py.una.med.base.services.schemas.HTTPExceptionDTO;

/**
 * Exception resolver para los endpoints. Permite personalizar la propiedad
 * Fault, tales como la adicion de detalles.
 * 
 * @author Uriel Gonzalez
 * @author Arturo Volpe
 * 
 */
public class EndpointExceptionResolver extends
		SoapFaultMappingExceptionResolver {

	/**
	 * 
	 */
	private static final String ERROR_MARSHALLING_EXCEPTION = "Error marshalling exception";

	private Marshaller marshaller;

	private static final Logger LOG = LoggerFactory
			.getLogger(EndpointExceptionResolver.class);

	private static final QName CODE = new QName("code");
	private static final QName DESCRIPTION = new QName("description");

	/**
	 * Permite personalizar la propiedad Fault.
	 * 
	 * @param endopoint
	 *            Endpoint ejecutado.
	 * @param ex
	 *            Excepcion a ser manejada.
	 * @param fault
	 *            Fault creado
	 */
	@Override
	public void customizeFault(Object endpoint, Exception ex, SoapFault fault) {

		Object realEndPoint;
		if (endpoint instanceof MethodEndpoint) {
			realEndPoint = ((MethodEndpoint) endpoint).getBean();
		} else {
			realEndPoint = endpoint;
		}

		if (ex instanceof HTTPException) {
			handleHTTPException(fault, (HTTPException) ex);
		} else {
			logException(realEndPoint, ex);
		}
	}

	/**
	 * @param endpoint
	 * @param ex
	 */
	private void logException(Object endpoint, Exception ex) {

		LOG.warn("Error in the web service {}", endpoint.getClass().getName(),
				ex);
	}

	/**
	 * Maneja una excepción de ltipo {@link HTTPException}.
	 * 
	 * <p>
	 * Estas excepciones son controladas, igual deben ser logeadas.
	 * </p>
	 * 
	 * @param endpoint
	 * 
	 * @param fault
	 *            mensaje a retornar
	 * @param ee
	 *            excepción.
	 */
	private void handleHTTPException(SoapFault fault, HTTPException ee) {

		HTTPExceptionDTO dto = new HTTPExceptionDTO();
		dto.setCode(ee.getCode());
		dto.setSummary(ee.getShortDescription());

		SoapFaultDetail detail = fault.addFaultDetail();
		Result result = detail.getResult();
		try {
			marshaller.marshal(dto, result);
		} catch (XmlMappingException e) {
			LOG.warn(ERROR_MARSHALLING_EXCEPTION, e);
			fallBack(detail, ee);
		} catch (IOException e) {
			LOG.warn(ERROR_MARSHALLING_EXCEPTION, e);
			fallBack(detail, ee);
		}

	}

	/**
	 * @param ee
	 * @param detail
	 */
	private void fallBack(SoapFaultDetail detail, HTTPException ee) {

		detail.addFaultDetailElement(CODE).addText(ee.getCode());
		detail.addFaultDetailElement(DESCRIPTION).addText(
				ee.getShortDescription());
	}

	/**
	 * @param marshaller
	 *            marshaller para setear
	 */
	@Autowired
	public void setMarshaller(Marshaller marshaller) {

		this.marshaller = marshaller;
	}
}
