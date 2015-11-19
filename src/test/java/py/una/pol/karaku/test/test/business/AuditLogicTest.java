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

package py.una.pol.karaku.test.test.business;

import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.business.AuditLogic;
import py.una.pol.karaku.domain.AuditTrail;
import py.una.pol.karaku.domain.AuditTrailDetail;
import py.una.pol.karaku.repo.AuditTrailDao;
import py.una.pol.karaku.repo.AuditTrailDetailDao;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TestBeanCreator;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.util.TestUtils;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Feb 27, 2015
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class AuditLogicTest extends BaseTestWithDatabase {

    @Configuration
    static class ContextConfiguration extends TransactionTestConfiguration {

        @Override
        public Class<?>[] getEntityClasses() {

            return TestUtils.getReferencedClasses(AuditTrail.class);
        }

        @Bean
        TestBeanCreator getTestBeanCreator() {

            return new TestBeanCreator(TestUtils.getAsClassArray(
                    AuditTrailDao.class, AuditTrailDetailDao.class,
                    AuditLogic.class));
        }

    }

    @Autowired
    private AuditLogic logic;

    /**
     * Test method for {@link py.una.pol.karaku.business.AuditLogic#getDao()}.
     */
    @Test
    public void testGetDao() {

        assertNotNull(logic.getDao());
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.business.AuditLogic#saveAudit(py.una.pol.karaku.domain.AuditTrail, java.util.List)}
     * .
     */
    @Test
    public void testSaveAudit() {

        AuditTrail trail = new AuditTrail();
        AuditTrailDetail detail = new AuditTrailDetail();

        trail.setIp("10.1.1.1");
        trail.setMethodSignature("#metodo()");
        trail.setUsername("USUARIO");
        detail.setExpression("!@$%TQASD%");
        detail.setValue("adsf");
        List<AuditTrailDetail> detalles = new ArrayList<AuditTrailDetail>();
        trail.setDetails(detalles);

        logic.saveAudit(trail, detalles);
        detalles.add(detail);
        logic.saveAudit(trail, detalles);
    }
}
