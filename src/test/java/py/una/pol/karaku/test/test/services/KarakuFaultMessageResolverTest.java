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
package py.una.pol.karaku.test.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import py.una.pol.karaku.exception.HTTPException;
import py.una.pol.karaku.services.client.KarakuFaultMessageResolver;
import py.una.pol.karaku.services.schemas.HTTPExceptionDTO;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 10, 2014
 * 
 */
public class KarakuFaultMessageResolverTest {

	/**
	 * Test method for
	 * {@link py.una.pol.karaku.services.client.KarakuFaultMessageResolver#resolveFault(org.springframework.ws.WebServiceMessage)}
	 * .
	 */
	@Test
	public void testResolveFault_HTTPException() throws Exception {

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(HTTPExceptionDTO.class);

		KarakuFaultMessageResolver resolver = new KarakuFaultMessageResolver(
				marshaller);
		// @formatter:off
		SOAPMessage message = getFromString(""
				+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"> " +
				"   <SOAP-ENV:Header/> " +
				"   <SOAP-ENV:Body> " +
				"      <SOAP-ENV:Fault> " +
				"         <faultcode>SOAP-ENV:Client</faultcode> " +
				"         <faultstring xml:lang=\"en\">Invalid request</faultstring> " +
				"         <detail> " +
				"            <ns4:HTTPException xmlns:ns4=\"http://sigh.med.una.py/2013/schemas/base\"> " +
				"               <code>1</code>" +
				"               <summary>2</summary>" +
				"            </ns4:HTTPException>" +
				"         </detail>" +
				"      </SOAP-ENV:Fault>" +
				"   </SOAP-ENV:Body>" +
				"</SOAP-ENV:Envelope>");
		// @formatter:on

		SaajSoapMessage ssm = new SaajSoapMessage(message);
		try {
			resolver.resolveFault(ssm);
			fail();
		} catch (HTTPException exception) {
			assertEquals("1", exception.getCode());
			assertEquals("2", exception.getShortDescription());
		}
	}

	@Test
	public void testResolvXteFault_NormalException() throws Exception {

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(HTTPExceptionDTO.class);

		KarakuFaultMessageResolver resolver = new KarakuFaultMessageResolver(
				marshaller);
		// @formatter:off
		SOAPMessage message = getFromString(""
				+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"> " +
				"   <SOAP-ENV:Header/> " +
				"   <SOAP-ENV:Body> " +
				"      <SOAP-ENV:Fault> " +
				"         <faultcode>SOAP-ENV:Client</faultcode> " +
				"         <faultstring xml:lang=\"en\">Invalid request</faultstring> " +
				"      </SOAP-ENV:Fault>" +
				"   </SOAP-ENV:Body>" +
				"</SOAP-ENV:Envelope>");
		// @formatter:on

		SaajSoapMessage ssm = new SaajSoapMessage(message);
		try {
			resolver.resolveFault(ssm);
			fail();
		} catch (SoapFaultClientException exception) {
			assertEquals("Client", exception.getFaultCode().getLocalPart());
			assertEquals("Invalid request", exception.getFaultStringOrReason());
		}
	}

	/**
	 * @return
	 * @throws OPException
	 * @throws Exception
	 */
	private SOAPMessage getFromString(String xml) throws Exception {

		MessageFactory factory = MessageFactory.newInstance();
		SOAPMessage message = factory
				.createMessage(
						new MimeHeaders(),
						new ByteArrayInputStream(xml.getBytes(Charset
								.forName("UTF-8"))));
		return message;
	}

}
