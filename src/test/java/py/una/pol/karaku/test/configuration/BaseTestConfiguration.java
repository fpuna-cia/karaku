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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import py.una.pol.karaku.log.LogPostProcessor;
import py.una.pol.karaku.math.MathContextProvider;
import py.una.pol.karaku.services.util.NumberAdapter;
import py.una.pol.karaku.test.util.TestI18nHelper;
import py.una.pol.karaku.test.util.TestPropertiesUtil;
import py.una.pol.karaku.util.FormatProvider;
import py.una.pol.karaku.util.I18nHelper;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 19, 2013
 * 
 */
@Configuration
@Profile(BaseTestConfiguration.TEST_PROFILE)
public class BaseTestConfiguration {

	/**
	 * Ubicacion del archivo de configuraciones
	 */
	public static final String CONFIG_LOCATION = "karaku.properties";

	/**
	 * Cadena que representa el profile de test
	 */
	public static final String TEST_PROFILE = "test";

	protected static TestPropertiesUtil properties;

	@Bean
	public static TestPropertiesUtil propertiesUtil() {

		properties = new TestPropertiesUtil();
		properties.setLocation(new ClassPathResource(CONFIG_LOCATION));
		return properties;
	}

	@Bean
	LogPostProcessor logPostProcessor() {

		return new LogPostProcessor();
	}

	@Bean(name = "fp")
	FormatProvider formatProvider() {

		return new FormatProvider();
	}

	@Bean
	MathContextProvider mathContextProvider() {

		return MathContextProvider.INSTANCE;
	}

	@Bean
	NumberAdapter quantityAdapter() {

		return NumberAdapter.INSTANCE;
	}

	@Bean
	I18nHelper i18nHelper() {

		return new TestI18nHelper();
	}

}
