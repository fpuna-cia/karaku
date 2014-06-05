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

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;
import py.una.pol.karaku.exception.KarakuRuntimeException;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 18, 2013
 * 
 */
@Component
public class ServiceDefinitionRegister implements BeanFactoryPostProcessor {

	private static final Logger LOG = LoggerFactory
			.getLogger(ServiceDefinitionRegister.class);

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

		LOG.info("Registering @WebServices");
		String[] beans = bf.getBeanDefinitionNames();
		for (String s : beans) {
			Class<?> beanType = bf.getType(s);
			WebServiceDefinition ws = AnnotationUtils.findAnnotation(beanType,
					WebServiceDefinition.class);
			if (ws != null) {
				String name = getName(s);
				DefaultWsdl11Definition newWS = createWebService(name,
						ws.xsds());

				bf.registerSingleton(name, newWS);
				LOG.info("Web service: {} has been added", name);
			}
		}

	}

	private String getName(String beanName) {

		return beanName
				.substring(0, beanName.toUpperCase().indexOf("ENDPOINT"));
	}

	/**
	 * @param path1
	 * @param path2
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private DefaultWsdl11Definition createWebService(String name,
			String ... strings) {

		LOG.debug("Creating {} Web Service", name);
		DefaultWsdl11Definition toRet = new DefaultWsdl11Definition();
		toRet.setPortTypeName(name);
		toRet.setServiceName(name);

		Resource[] resources = new Resource[strings.length];
		for (int i = 0; i < strings.length; i++) {
			resources[i] = new ClassPathResource(strings[i]);
			if (!resources[i].exists()) {
				throw new KarakuRuntimeException("XSD with url: " + strings[i]
						+ " doesn't exist!");
			}
		}
		CommonsXsdSchemaCollection collection = new CommonsXsdSchemaCollection();
		collection.setInline(true);
		collection.setXsds(resources);
		try {
			collection.afterPropertiesSet();
			toRet.setSchemaCollection(collection);
			toRet.setLocationUri("/endpoints");
			toRet.afterPropertiesSet();
		} catch (IOException e) {
			throw new KarakuRuntimeException("Check XML location", e);
		} catch (Exception e) {
			throw new KarakuRuntimeException("Cant create WSDL11 Definition", e);
		}

		return toRet;
	}
}
