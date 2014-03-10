/*
 * @KarakuaultMessageResolver.java 1.0 Mar 6, 2014 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.services.client;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;
import py.una.med.base.exception.HTTPException;
import py.una.med.base.services.schemas.HTTPExceptionDTO;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 6, 2014
 * 
 */
public final class KarakuFaultMessageResolver implements FaultMessageResolver {

	private static final Logger LOG = LoggerFactory
			.getLogger(KarakuFaultMessageResolver.class);

	private Unmarshaller unmarshaller;

	public KarakuFaultMessageResolver(Unmarshaller unmarshaller) {

		this.unmarshaller = unmarshaller;
	}

	@Override
	public void resolveFault(WebServiceMessage message) throws IOException {

		SoapMessage message2 = (SoapMessage) message;

		SoapBody body = message2.getSoapBody();
		// No Fault object, go to fallback
		if (body.getFault() == null
				|| body.getFault().getFaultDetail() == null
				|| !body.getFault().getFaultDetail().getDetailEntries()
						.hasNext()) {
			fallBack(message2);
		}

		SoapFaultDetail sfd = body.getFault().getFaultDetail();

		tryHttpException(message2, sfd.getDetailEntries().next());

	}

	private void tryHttpException(SoapMessage message, SoapFaultDetailElement el)
			throws IOException {

		HTTPExceptionDTO dto;
		try {
			dto = (HTTPExceptionDTO) unmarshaller.unmarshal(el.getSource());
			throw new HTTPException(dto.getCode(), dto.getSummary());
		} catch (XmlMappingException e) {
			LOG.trace("Not HTTPException found", e);
		} catch (IOException e) {
			throw e;
		}
		fallBack(message);

	}

	private void fallBack(SoapMessage message) {

		throw new SoapFaultClientException(message);
	}
}
