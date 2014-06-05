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
package py.una.pol.karaku.test.test.replication;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static py.una.pol.karaku.util.Checker.notNull;
import javax.annotation.Nonnull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.replication.ShareableWatcher;
import py.una.pol.karaku.replication.server.Bundle;
import py.una.pol.karaku.replication.server.DummyFirstChangeProvider;
import py.una.pol.karaku.replication.server.EnversReplicationProvider;
import py.una.pol.karaku.replication.server.FirstChangeProviderHandler;
import py.una.pol.karaku.replication.server.ReplicationProvider;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.test.replication.layers.ReplicatedEntity;
import py.una.pol.karaku.test.test.replication.layers.ReplicatedEntityDao;
import py.una.pol.karaku.test.util.TestUriInterceptor;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.test.util.transaction.DatabasePopulatorExecutionListener;
import py.una.pol.karaku.test.util.transaction.SQLFiles;
import py.una.pol.karaku.test.util.transaction.Sequences;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 10, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Sequences("tt1")
@TestExecutionListeners({ DatabasePopulatorExecutionListener.class })
@SQLFiles
public class EnversReplicationProviderTest extends BaseTest {

	@Configuration
	@EnableTransactionManagement
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(ReplicatedEntity.class);
		};

		@Override
		protected boolean getWithEnvers() {

			return true;
		}

		@Bean
		ReplicatedEntityDao dao() {

			return new ReplicatedEntityDao();
		}

		@Bean
		SessionReplicationProvider replicationProvider() {

			return new TestEnversReplicationProvider();
		}

		@Bean
		ShareableWatcher shareableWatcher() {

			return new ShareableWatcher();
		}

		@Bean
		TestDummyFirstChangeProvider dummyFirstChangeProvider() {

			return new TestDummyFirstChangeProvider();
		}

		@Bean
		FirstChangeProviderHandler firstChangeProviderHandler() {

			return new FirstChangeProviderHandler();
		}

	}

	@Autowired
	private TestUriInterceptor uriInterceptor;

	@Autowired
	private SessionReplicationProvider provider;

	@Autowired
	private ReplicatedEntityDao dao;

	@Autowired
	private SessionFactory factory;

	private Session current;

	private Transaction transaction;

	@Autowired
	private TestDummyFirstChangeProvider changeProvider;

	@Test
	public void testGetAll() throws Exception {

		String zero_id = notNull(Bundle.ZERO_ID);

		String previusId;
		Bundle<ReplicatedEntity> bun;

		begin();
		bun = getBundle(zero_id);
		previusId = bun.getLastId();
		assertThat(bun.size(), is(5));
		commit();

		begin();
		ReplicatedEntity re = new ReplicatedEntity();
		re.setActive(true);
		re.setDescription("EEE1");
		re = dao.add(re);
		commit();

		begin();
		bun = getBundle(bun.getLastId());
		previusId = bun.getLastId();
		System.out.println("LLLLLLL id: " + previusId);
		assertFalse("0".equals(previusId));
		assertThat(bun.size(), is(1));
		assertTrue(bun.getEntities().contains(re));
		commit();

		begin();
		// logical deletion
		dao.remove(re);
		re = dao.getById(re.getId());
		commit();

		begin();
		bun = getBundle(bun.getLastId());
		System.out.println("LLLLLLL id: " + bun.getLastId());
		assertFalse(previusId.equals(bun.getLastId()));
		assertThat(bun.size(), is(1));
		ReplicatedEntity deleted = bun.getEntities().iterator().next();
		assertFalse(deleted.isActive());
		commit();

		begin();
		bun = getBundle(zero_id);
		previusId = bun.getLastId();
		assertThat(bun.size(), is(6));
		ReplicatedEntity fromEnvers = null;
		for (ReplicatedEntity e : bun.getEntities()) {
			if (e.getDescription().equals("EEE1")) {
				if (fromEnvers != null) {
					fail("Duplicated entity EEE1 was returned");
				}
				fromEnvers = e;
			}
		}
		assertNotNull(fromEnvers);
		assertFalse(fromEnvers.isActive());
		commit();

		begin();
		bun = getBundle("8888888");
		assertThat(bun.size(), is(6));
		assertThat(bun.getLastId(), is(previusId));
		commit();
	}

	private void begin() {

		current = factory.openSession();
		transaction = current.beginTransaction();
		uriInterceptor.setSession(current);
		provider.setSession(current);
		dao.setSession(current);
		changeProvider.setS(current);
	}

	private void commit() {

		current.flush();
		transaction.commit();
	}

	private Bundle<ReplicatedEntity> getBundle(@Nonnull String change) {

		return provider.getChanges(ReplicatedEntity.class, change);
	}

	static class TestEnversReplicationProvider extends
			EnversReplicationProvider implements SessionReplicationProvider {

		Session session;

		@Override
		protected Session getSession() {

			return session;
		}

		@Override
		public void setSession(Session session) {

			this.session = session;
		}

		@Override
		protected <T extends Shareable> Number getLastId(Class<T> clazz,
				String lastId) {

			return new Integer(super.getLastId(clazz, lastId).intValue());
		}

	}

	static interface SessionReplicationProvider extends ReplicationProvider {

		void setSession(Session session);
	}

	static class TestDummyFirstChangeProvider extends DummyFirstChangeProvider {

		Session s;

		/**
		 * @param s
		 *            s para setear
		 */
		public void setS(Session s) {

			this.s = s;
		}

		@Override
		protected Session getSession() {

			return s;
		}
	}
}
