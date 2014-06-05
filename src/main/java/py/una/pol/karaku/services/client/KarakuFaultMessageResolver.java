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
package py.una.pol.karaku.services.client;

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
import py.una.pol.karaku.exception.HTTPException;
import py.una.pol.karaku.services.schemas.HTTPExceptionDTO;

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
