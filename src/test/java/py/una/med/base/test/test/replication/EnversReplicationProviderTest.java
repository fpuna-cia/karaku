/*
 * @EnversReplicationProviderTest.java 1.0 Dec 10, 2013 Sistema Integral de
 * Gestion Hospitalaria
 */
package py.una.med.base.test.test.replication;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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
import py.una.med.base.replication.Shareable;
import py.una.med.base.replication.ShareableWatcher;
import py.una.med.base.replication.server.Bundle;
import py.una.med.base.replication.server.DummyFirstChangeProvider;
import py.una.med.base.replication.server.EnversReplicationProvider;
import py.una.med.base.replication.server.FirstChangeProviderHandler;
import py.una.med.base.replication.server.ReplicationProvider;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.test.replication.layers.ReplicatedEntity;
import py.una.med.base.test.test.replication.layers.ReplicatedEntityDao;
import py.una.med.base.test.util.TestUriInterceptor;
import py.una.med.base.test.util.TestUtils;
import py.una.med.base.test.util.transaction.DatabasePopulatorExecutionListener;
import py.una.med.base.test.util.transaction.SQLFiles;
import py.una.med.base.test.util.transaction.Sequences;

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

		String previusId;
		Bundle<ReplicatedEntity> bun;

		begin();
		bun = getBundle(Bundle.ZERO_ID);
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
		bun = getBundle(Bundle.ZERO_ID);
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

	private Bundle<ReplicatedEntity> getBundle(String change) {

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
