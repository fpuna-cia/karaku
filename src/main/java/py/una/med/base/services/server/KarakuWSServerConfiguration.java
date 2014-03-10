/*
 * @KarakuWSServerConfiguration.java 1.0 Oct 29, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.services.server;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import py.una.med.base.exception.HTTPException;

/**
 * Configuración para ser proveedor de servicios.
 * 
 * <p>
 * Esta muy relacionada a la clase
 * {@link py.una.med.base.configuration.KarakuWSClientConfiguration} pues ella
 * es la que provee los beans marshaller y umarshaller necesarios para la
 * serialización y desserialización de XML.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 29, 2013
 * 
 */
@Configuration
public class KarakuWSServerConfiguration {

	@Bean
	public EndpointExceptionResolver exceptionResolver() {

		EndpointExceptionResolver toRet = new EndpointExceptionResolver();

		SoapFaultDefinition sfd = new SoapFaultDefinition();
		sfd.setFaultCode(SoapFaultDefinition.SERVER);
		toRet.setDefaultFault(sfd);

		Properties properties = new Properties();
		properties.put(HTTPException.class.getName(), "CLIENT,Invalid request");

		toRet.setExceptionMappings(properties);

		toRet.setOrder(1);

		return toRet;
	}
}
