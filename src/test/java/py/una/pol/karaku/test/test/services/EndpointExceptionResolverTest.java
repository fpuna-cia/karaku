/*
 * @EndpointEsceptionResolverTest.java 1.0 Mar 10, 2014 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.pol.karaku.test.test.services;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.xml.transform.StringResult;
import py.una.pol.karaku.exception.HTTPException;
import py.una.pol.karaku.services.schemas.HTTPExceptionDTO;
import py.una.pol.karaku.services.server.EndpointExceptionResolver;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Mar 10, 2014
 * 
 */
public class EndpointExceptionResolverTest {

	/**
	 * Test method for
	 * {@link py.una.pol.karaku.services.server.EndpointExceptionResolver#customizeFault(java.lang.Object, java.lang.Exception, org.springframework.ws.soap.SoapFault)}
	 * .
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@Test
	public void testCustomizeFaultObjectExceptionSoapFault() throws Exception {

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(HTTPExceptionDTO.class);

		EndpointExceptionResolver eer = new EndpointExceptionResolver();
		eer.setMarshaller(marshaller);

		Method m = Endpoint.class.getMethod("call", String.class);

		MethodEndpoint me = new MethodEndpoint(new Endpoint(), m);

		Fault f = new Fault();
		eer.customizeFault(me, new HTTPException("1", "code"), f);

		StringResult result = f.getFaultDetail().getResult();

		// @formatter:off
		String expectedResult = ""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<ns2:HTTPException xmlns:ns2=\"http://sigh.med.una.py/2013/schemas/base\">"
				+ "<code>1</code>"
				+ "<summary>code</summary>"
				+ "</ns2:HTTPException>";
		// @formatter:on
		assertEquals(result.toString(), expectedResult);

	}

	static class Endpoint {

		public void call(String a) {

		}
	}

	static class Fault implements SoapFault {

		FaultDetail fd;

		@Override
		public QName getName() {

			return null;
		}

		@Override
		public Source getSource() {

			return null;
		}

		@Override
		public void addAttribute(QName name, String value) {

		}

		@Override
		public void removeAttribute(QName name) {

		}

		@Override
		public String getAttributeValue(QName name) {

			return null;
		}

		@Override
		public Iterator<QName> getAllAttributes() {

			return null;
		}

		@Override
		public void addNamespaceDeclaration(String prefix, String namespaceUri) {

		}

		@Override
		public QName getFaultCode() {

			return null;
		}

		@Override
		public String getFaultStringOrReason() {

			return null;
		}

		@Override
		public String getFaultActorOrRole() {

			return null;
		}

		@Override
		public void setFaultActorOrRole(String faultActor) {

		}

		@Override
		public FaultDetail getFaultDetail() {

			return fd;
		}

		@Override
		public SoapFaultDetail addFaultDetail() {

			if (fd == null) {
				fd = new FaultDetail();
			}

			return fd;
		}

	}

	static class FaultDetail extends Fault implements SoapFaultDetail {

		StringResult result;

		/**
		 * 
		 */
		public FaultDetail() {

			result = new StringResult();
		}

		@Override
		public SoapFaultDetailElement addFaultDetailElement(QName name) {

			return null;
		}

		@Override
		public StringResult getResult() {

			return result;
		}

		@Override
		public Iterator<SoapFaultDetailElement> getDetailEntries() {

			return null;
		}

	}
}
