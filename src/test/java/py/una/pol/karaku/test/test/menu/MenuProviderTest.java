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
package py.una.pol.karaku.test.test.menu;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.menu.client.AbstractMenuProvider;
import py.una.pol.karaku.menu.client.MenuWSCaller;
import py.una.pol.karaku.menu.client.WSMenuProvider;
import py.una.pol.karaku.menu.schemas.Menu;
import py.una.pol.karaku.menu.schemas.MenuRequest;
import py.una.pol.karaku.menu.schemas.MenuResponse;
import py.una.pol.karaku.menu.server.MenuServerLogic;
import py.una.pol.karaku.menu.server.MenuServiceEndpoint;
import py.una.pol.karaku.services.client.JsonURLProvider;
import py.una.pol.karaku.services.client.WSInformationProvider;
import py.una.pol.karaku.test.base.BaseTestWebService;
import py.una.pol.karaku.test.configuration.WebServiceTestConfiguration;
import py.una.pol.karaku.test.util.TestI18nHelper;
import py.una.pol.karaku.test.util.TestPropertiesUtil;
import py.una.pol.karaku.test.util.TestUtils;

import static org.junit.Assert.*;

/**
 * Clase de Test para {@link WSMenuProvider}
 *
 * @author Arturo Volpe
 * @author Diego Ramírez
 * @version 1.0 Oct 18, 2013
 * @since 2.2.8
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MenuProviderTest extends BaseTestWebService {

    @Configuration
    static class ContextConfiguration extends WebServiceTestConfiguration {

        @Bean
        MenuServerLogic menuServerLogic() {

            return new MenuServerLogic();
        }

        @Bean
        MenuServiceEndpoint menuServiceEndpoint() {

            return new MenuServiceEndpoint();
        }

        @Bean
        AbstractMenuProvider menuClientLogic() {

            return new WSMenuProvider();
        }

        @Bean
        @Override
        protected WSInformationProvider wsInformationProvider()
                throws IOException {

            return new JsonURLProvider(TestUtils
                    .getSiblingResource(MenuProviderTest.class, "urls.json")
                    .getInputStream());
        }

        @Bean
        MenuWSCaller menuWSCaller() {

            return new MenuWSCaller();
        }

        @Override
        public Class<?>[] getClassesToBound() {

            return TestUtils
                    .getAsClassArray(MenuRequest.class, MenuResponse.class);
        }

    }

    @Autowired
    WSInformationProvider wsInformationProvider;

    @Autowired
    TestPropertiesUtil propertiesUtil;

    @Autowired
    TestI18nHelper helper;

    @Autowired
    AbstractMenuProvider menuClientLogic;

    @Autowired
    MenuServerLogic serverLogic;

    @Before
    public void before() throws IOException {

        propertiesUtil.put("karaku.ws.client.enabled","true");
        propertiesUtil.put(MenuServerLogic.KARAKU_MENU_LOCATION_KEY,
                TestUtils.getSiblingResourceName(getClass(), "testMenu.xml"));

        helper.addString("TEST_STRING", "test");
    }

    @Test
    public void testCallMocked() {

        List<Menu> menu = menuClientLogic.getMenu();
        assertFalse(menu.isEmpty());
        // TODO comparar el menu con el proveido directamente por serverlogic
    }

    @Test
    public void testGetMenu() {

        WSMenuProvider wsMenuProvider = (WSMenuProvider) menuClientLogic;
        // isDirty = true luego del postConstruct(), fuerza la reconstrucción de la lista de menus
        List<Menu> menu0 = wsMenuProvider.getMenu();
        wsMenuProvider.notifyMenuChange();

        // isDirty = false no se vuelve a reconstruir la lista de menus
        List<Menu> menu1 = wsMenuProvider.getMenu();
        assertEquals("Las listas de menus deben ser las mismas", menu0, menu1);
    }

    @Test
    public void testCall() {

        WSMenuProvider wsMenuProvider = (WSMenuProvider) menuClientLogic;
        assertEquals("Debe existir solo un menu en la lista",1,wsMenuProvider.getMenu().size());
        wsMenuProvider.call();
        assertEquals("Deben existir 3 menus en la lista",3,wsMenuProvider.getMenu().size());
    }
}
