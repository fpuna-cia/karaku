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
package py.una.pol.karaku.services.server;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import py.una.pol.karaku.exception.HTTPException;

/**
 * Configuración para ser proveedor de servicios.
 * 
 * <p>
 * Esta muy relacionada a la clase
 * {@link py.una.pol.karaku.configuration.KarakuWSClientConfiguration} pues ella
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
