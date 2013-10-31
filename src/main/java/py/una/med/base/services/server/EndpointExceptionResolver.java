package py.una.med.base.services.server;

import javax.xml.namespace.QName;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import py.una.med.base.exception.HTTPException;

/**
 * Exception resolver para los endpoints. Permite personalizar la propiedad
 * Fault, tales como la adicion de detalles.
 * 
 * @author Uriel Gonzalez
 * 
 */
public class EndpointExceptionResolver extends
		SoapFaultMappingExceptionResolver {

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
	protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {

		if (ex instanceof HTTPException) {
			HTTPException ee = (HTTPException) ex;
			SoapFaultDetail detail = fault.addFaultDetail();
			detail.addFaultDetailElement(CODE).addText(ee.getCode());
			detail.addFaultDetailElement(DESCRIPTION).addText(
					ee.getShortDescription());
		}
	}
}
