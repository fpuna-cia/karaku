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
package py.una.pol.karaku.test.configuration;

import java.io.IOException;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.server.MockWebServiceClient;
import py.una.pol.karaku.exception.KarakuRuntimeException;
import py.una.pol.karaku.services.client.JsonURLProvider;
import py.una.pol.karaku.services.client.WSCaller;
import py.una.pol.karaku.services.client.WSInformationProvider;
import py.una.pol.karaku.services.client.WSSecurityInterceptor;
import py.una.pol.karaku.services.server.WebServiceDefinition;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.test.util.TestWSCaller;

/**
 * Clase base de configuración para test que utilizan
 * {@link WebServiceDefinition}.
 * 
 * <p>
 * Disponibiliza dos beans para serialización/desserialización, los cuales son
 * {@link #marshaller()} y {@link #unmarshaller()} respectivamente.
 * <p>
 * 
 * <p>
 * El método {@link #getClassesToBound()} debe retornar las clases que se
 * pueden marshalizar/desmarshalizar
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * 
 */
public abstract class WebServiceTestConfiguration extends BaseTestConfiguration
		implements AsyncConfigurer {

	private Jaxb2Marshaller marshaller;

	public abstract Class<?>[] getClassesToBound();

	private final Logger log = LoggerFactory
			.getLogger(WebServiceTestConfiguration.class);

	@Bean
	Marshaller marshaller() {

		return jaxb2Marshaller();
	}

	@Bean
	Unmarshaller unmarshaller() {

		return jaxb2Marshaller();
	}

	/**
	 * @return
	 */
	private Jaxb2Marshaller jaxb2Marshaller() {

		if (marshaller == null) {
			marshaller = new Jaxb2Marshaller();
			marshaller.setClassesToBeBound(getClassesToBound());
		}
		return marshaller;
	}

	@Bean
	WSSecurityInterceptor wsSecurityInterceptor() {

		return new WSSecurityInterceptor();
	}

	@Override
	public Executor getAsyncExecutor() {

		return executor();
	}

	@Bean
	Executor executor() {

		return new SyncTaskExecutor();
	}

	@Bean
	public SyncTaskExecutor syncExecutor() {

		return new SyncTaskExecutor();
	}

	@Bean
	public WebServiceTemplate webServiceTemplate() {

		WebServiceTemplate wst = new WebServiceTemplate();
		wst.setMarshaller(marshaller());
		wst.setUnmarshaller(unmarshaller());
		return wst;

	}

	@Bean
	@Autowired
	public MockWebServiceClient mockWebServiceClient(
			ApplicationContext applicationContext) {

		return MockWebServiceClient.createClient(applicationContext);
	}

	@Bean
	WSCaller wsCaller() {

		return new TestWSCaller();
	}

	@Bean
	protected WSInformationProvider wsInformationProvider() throws IOException {

		try {
			Class<?> clazz = getClass();
			if (getClass().getEnclosingClass() != null) {
				clazz = getClass().getEnclosingClass();
			}
			return new JsonURLProvider(TestUtils.getSiblingResource(clazz,
					"urls.json").getInputStream());
		} catch (KarakuRuntimeException kre) {
			log.debug(
					"Can not find the file urls.json for this test, disabling urlprovider (%s)",
					kre.getMessage());
			return null;
		} catch (IOException io) {
			log.debug(
					"Can not find the file urls.json for this test, disabling urlprovider (%s)",
					io.getMessage());
			return null;
		}
	}
}
