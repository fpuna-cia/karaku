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

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.services.AbstractConverter;
import py.una.pol.karaku.services.Converter;
import py.una.pol.karaku.services.ConverterProvider;
import py.una.pol.karaku.services.ReflectionConverter;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 11, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ConverterProviderTest extends BaseTest {

    @Configuration
    static class ContextConfiguration extends TransactionTestConfiguration {

        @Bean
        ConverterProvider converterProvider() {

            return new ConverterProvider();
        }

        @Bean
        Converter1 converter1() {

            return new Converter1();
        }

        @Bean
        Converter2 converter2() {

            return new Converter2();
        }

    }

    @Autowired
    private ConverterProvider converterProvider;

    @Test
    public void testGetDefault() throws Exception {

        assertTrue(converterProvider.getConverter(Shareable.class, DTO.class) instanceof ReflectionConverter);

    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testGetContextConverters() throws Exception {

        Converter c = converterProvider.getConverter(Sharable1.class,
                DTO1.class);
        assertTrue(c.getClass().equals(Converter1.class));

        c = converterProvider.getConverter(Sharable2.class, DTO2.class);
        assertTrue(c.getClass().equals(Converter2.class));
    }

    static class Converter1 extends AbstractConverter<Sharable1, DTO1> {

    }

    static class Converter2 extends AbstractConverter<Sharable2, DTO2> {

    }

    static class Sharable1 implements Shareable {

        @Override
        public String getUri() {

            return null;
        }

        @Override
        public void inactivate() {

        }

        @Override
        public void activate() {

        }

        @Override
        public boolean isActive() {

            return false;
        }

    }

    static class Sharable2 implements Shareable {

        @Override
        public String getUri() {

            return null;
        }

        @Override
        public void inactivate() {

        }

        @Override
        public void activate() {

        }

        @Override
        public boolean isActive() {

            return false;
        }

    }

    static class DTO1 implements DTO {

        @Override
        public String getUri() {

            return null;
        }

        @Override
        public boolean isActive() {

            return false;
        }

    }

    static class DTO2 implements DTO {

        @Override
        public String getUri() {

            return null;
        }

        @Override
        public boolean isActive() {

            return false;
        }

    }
}
