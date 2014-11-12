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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Esta clase es la encargada de crear los beans de los tipos indicados en
 * {@link BaseTestConfiguration}
 * 
 * 
 * @author Rainer Reyes
 * @since 1.0
 * @version 1.0 24/10/2014
 * 
 */

public class TestBeanCreator implements BeanFactoryPostProcessor {

	Class<?>[] classes;

	public TestBeanCreator(BaseTestConfiguration base) {
		this.classes = base.getCreateBeanClasses();
	}

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {

		if (classes == null)
			return;

		for (Class<?> clazz : classes) {
			try {
				DefaultListableBeanFactory bf = (DefaultListableBeanFactory) beanFactory;
				String beanName = getName(clazz);
				if (bf.containsBeanDefinition(beanName))
					continue;
				BeanDefinition bd = BeanDefinitionBuilder.rootBeanDefinition(
						clazz).getBeanDefinition();
				bf.registerBeanDefinition(beanName, bd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getName(Class<?> beanClass) {
		return beanClass.getSimpleName().substring(0, 1).toLowerCase()
				+ beanClass.getSimpleName().substring(1);
	}

}
