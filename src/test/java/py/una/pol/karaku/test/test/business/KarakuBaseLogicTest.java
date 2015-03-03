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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import javax.persistence.Id;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.business.KarakuBaseLogic;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.repo.IKarakuBaseDao;
import py.una.pol.karaku.repo.KarakuBaseDao;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;
import py.una.pol.karaku.test.configuration.TestBeanCreator;
import py.una.pol.karaku.test.util.TestUtils;

/**
 * 
 * @author Diego Ramírez
 * @since 1.0
 * @version 1.0 Mar 2, 2015
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class KarakuBaseLogicTest extends BaseTest {

    @Configuration
    static class ContextConfiguration extends BaseTestConfiguration {

        @Bean
        TestBeanCreator getTestBeanCreator() {

            return new TestBeanCreator(TestUtils.getAsClassArray());
        }

    }

    public static class DummyWhitId {

        @Id
        private Long id;

        public DummyWhitId() {

            this.id = 0L;
        }

        public Long getId() {

            return id;
        }

        public void setId(Long id) {

            this.id = id;
        }

    }

    public static class DummyWhithoutID implements Shareable {

        private Long id;
        private boolean active;

        public DummyWhithoutID() {

            this.id = 0L;
            this.active = false;
        }

        public void setActive(boolean active) {

            this.active = active;
        }

        public Long getId() {

            return id;
        }

        public void setId(Long id) {

            this.id = id;
        }

        /*
         * (non-Javadoc)
         * 
         * @see py.una.pol.karaku.replication.Shareable#getUri()
         */
        @Override
        public String getUri() {

            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see py.una.pol.karaku.replication.Shareable#inactivate()
         */
        @Override
        public void inactivate() {

            this.setActive(false);

        }

        /*
         * (non-Javadoc)
         * 
         * @see py.una.pol.karaku.replication.Shareable#activate()
         */
        @Override
        public void activate() {

            this.setActive(true);

        }

        /*
         * (non-Javadoc)
         * 
         * @see py.una.pol.karaku.replication.Shareable#isActive()
         */
        @Override
        public boolean isActive() {

            return this.active;
        }
    }

    class DummyDaoWhitID extends KarakuBaseDao<DummyWhitId, Long> implements
            IKarakuBaseDao<DummyWhitId, Long> {

    }

    class DummyDaoWhithoutId extends KarakuBaseDao<DummyWhithoutID, Long>
            implements IKarakuBaseDao<DummyWhithoutID, Long> {

    }

    class DummyLogicWhithoutId extends KarakuBaseLogic<DummyWhithoutID, Long> {

        /*
         * (non-Javadoc)
         * 
         * @see py.una.pol.karaku.business.IKarakuBaseLogic#getDao()
         */
        @Override
        public IKarakuBaseDao<DummyWhithoutID, Long> getDao() {

            return new DummyDaoWhithoutId();
        }

    }

    class DummyLogic extends KarakuBaseLogic<DummyWhitId, Long> {

        /*
         * (non-Javadoc)
         * 
         * @see py.una.pol.karaku.business.IKarakuBaseLogic#getDao()
         */
        @Override
        public IKarakuBaseDao<DummyWhitId, Long> getDao() {

            return new DummyDaoWhitID();
        }

    }

    private DummyLogic logic;

    /**
     * Test method for
     * {@link py.una.pol.karaku.business.KarakuBaseLogic#getIdValue(java.lang.Object)}
     * .
     */
    @Test
    public void testGetIdValue() {

        logic = new DummyLogic();

        DummyWhitId dummy = new DummyWhitId();
        dummy.setId(27L);

        Long x = logic.getIdValue(dummy);
        assertEquals(new Long(27L), x);

        /*
         * Test Whithout ID
         */
        DummyLogicWhithoutId logic2 = new DummyLogicWhithoutId();
        DummyWhithoutID dummy2 = new DummyWhithoutID();
        dummy2.setId(27L);

        Long y = logic2.getIdValue(dummy2);
        assertNull(y);
    }

    /**
     * Test method for
     * {@link py.una.pol.karaku.business.KarakuBaseLogic#getNewInstance()}.
     */
    @Test
    public void testGetNewInstance() {

        logic = new DummyLogic();

        DummyWhitId d = logic.getNewInstance();
        assertNotNull(d);

        DummyLogicWhithoutId logic2 = new DummyLogicWhithoutId();
        DummyWhithoutID d2 = logic2.getNewInstance();
        assertTrue(d2.isActive());

    }
}
