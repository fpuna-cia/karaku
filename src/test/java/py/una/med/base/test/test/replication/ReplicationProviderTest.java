/*
 * @WatcherTest.java 1.0 Nov 4, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.replication;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.med.base.replication.Bundle;
import py.una.med.base.replication.EnversReplicationProvider;
import py.una.med.base.replication.ReplicationProvider;
import py.una.med.base.replication.ShareableWatcher;
import py.una.med.base.test.base.BaseTestWithDatabase;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.test.replication.layers.EntityDao;
import py.una.med.base.test.test.replication.layers.ReplicatedEntity;
import py.una.med.base.test.util.TestUtils;
import py.una.med.base.test.util.transaction.Sequences;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 4, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@Sequences("tt1")
public class ReplicationProviderTest extends BaseTestWithDatabase {

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
		EntityDao dao() {

			return new EntityDao();
		}

		@Bean
		ReplicationProvider provider() {

			return new EnversReplicationProvider();
		}

		@Bean
		ShareableWatcher shareableWatcher() {

			return new ShareableWatcher();
		}

	}

	@Autowired
	private EntityDao dao;

	@Autowired
	private ReplicationProvider rp;

	@Test
	public void testLogicalDeletion() throws Exception {

		assertTrue("New entity (created by SQL) is not active", dao.getById(1L)
				.isActive());
		dao.remove(1L);
		ReplicatedEntity re = dao.getById(1L);
		assertFalse("Deleted entity is active", re.isActive());
	}

	@Test
	public void testEventRegistration() throws Exception {

		assertThat("SQL created entities are not registered",
				rp.getChanges(ReplicatedEntity.class, Bundle.ZERO_ID).size(),
				is(5));

		// /////////////////////////////////////////
		// Se descarto la idea de retornar un delta.
		// /////////////////////////////////////////
		// String lastId = rp.getChanges(ReplicatedEntity.class, Bundle.ZERO_ID)
		// .getLastId();
		//
		// assertThat("Informer#getChanges is not idempotent",
		// rp.getChanges(ReplicatedEntity.class, lastId).size(), is(0));
		//
		// dao.remove(1L);
		//
		// assertThat("A logical deletion is ommited",
		// rp.getChanges(ReplicatedEntity.class, lastId).size(), is(1));
		//
		// Bundle<ReplicatedEntity> b = rp.getChanges(ReplicatedEntity.class,
		// lastId);
		// ReplicatedEntity re = b.iterator().next().getEntity();
		// assertFalse("Informer produces wrong entities", re.isActive());

	}
}
