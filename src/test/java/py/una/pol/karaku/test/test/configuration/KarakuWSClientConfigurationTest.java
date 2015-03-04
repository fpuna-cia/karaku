/*-
 * Copyright (c)
 *
 * 		2012-2015, Facultad Politécnica, Universidad Nacional de Asunción.
 * 		2012-2015, Facultad de Ciencias Médicas, Universidad Nacional de Asunción.
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

package py.una.pol.karaku.test.test.configuration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.lang.reflect.Field;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.pol.karaku.configuration.KarakuWSClientConfiguration;
import py.una.pol.karaku.configuration.PropertiesUtil;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Mar 3, 2015
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class KarakuWSClientConfigurationTest extends BaseTest {

    @Configuration
    @Profile(BaseTestConfiguration.TEST_PROFILE)
    static class ContextConfiguration {

        @Bean
        public static PropertiesUtil propertiesUtil() {

            return new PropertiesUtil2();
        }

    }

    @Autowired
    private PropertiesUtil2 properties;

    private final KarakuWSClientConfiguration wsConf;

    public KarakuWSClientConfigurationTest() {

        wsConf = new KarakuWSClientConfiguration();
    }

    @Before
    public void initialize() {

        Field[] campos = wsConf.getClass().getDeclaredFields();

        for (Field f : campos) {
            if ("properties".equals(f.getName())) {
                f.setAccessible(true);
                try {
                    f.set(wsConf, properties);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.configuration.KarakuWSClientConfiguration#webServiceTemplate()}
     * .
     */
    @Test
    public void testWebServiceTemplate() {

        properties.setFalsificar(true);
        WebServiceTemplate template = wsConf.webServiceTemplate();

        assertNotNull(template);

        properties.setFalsificar(false);

        template = wsConf.webServiceTemplate();
        assertNull(template);

    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.configuration.KarakuWSClientConfiguration#wsInformationProvider()}
     * .
     */
    @Test
    public void testWsInformationProvider() {

        properties.setFalsificar(true);
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.configuration.KarakuWSClientConfiguration#marshaller()}
     * .
     */
    @Test
    public void testMarshaller() {

        properties.setFalsificar(true);
        properties.setFailGet(true);

        Marshaller m = wsConf.marshaller();

        assertNotNull(m);

        properties.setFalsificar(false);
        m = wsConf.marshaller();
        assertNull(m);

    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.configuration.KarakuWSClientConfiguration#unmarshaller()}
     * .
     */
    @Test
    public void testUnmarshaller() {

        properties.setFalsificar(true);
        Unmarshaller u = wsConf.unmarshaller();

        assertNotNull(u);
    }
}

class PropertiesUtil2 extends PropertiesUtil {

    private boolean falsificar;
    private boolean failGet;

    /*
     * (non-Javadoc)
     * 
     * @see py.una.pol.karaku.configuration.PropertiesUtil#getBoolean
     * (java.lang.String, boolean)
     */
    @Override
    public boolean getBoolean(String key, boolean def) {

        return isFalsificar();
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.una.pol.karaku.configuration.PropertiesUtil#mergeProperties
     * (java.util.Properties)
     */
    @Override
    protected Properties mergeProperties(Properties main) {

        return new Properties();
    }

    /**
     * @return falsificar
     */
    public boolean isFalsificar() {

        return falsificar;
    }

    /**
     * @param falsificar
     *            falsificar para setear
     */
    public void setFalsificar(boolean falsificar) {

        this.falsificar = falsificar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see py.una.pol.karaku.configuration.PropertiesUtil#get(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String get(String key, String def) {

        if ("karaku.ws.client.packages_to_scan.with_spaces".equals(key)
                && !isFailGet()) {
            return "py.una.pol.karaku";
        }

        if ("karaku.ws.client.packages_to_scan".equals(key) && isFalsificar()) {
            return ".*karaku.*";
        }

        return "";
    }

    /**
     * @return failGet
     */
    public boolean isFailGet() {

        return failGet;
    }

    /**
     * @param failGet
     *            failGet para setear
     */
    public void setFailGet(boolean failGet) {

        this.failGet = failGet;
    }
}
