/*
 * @ReplicationManagerTest.java 1.0 Nov 22, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.replication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Collection;
import java.util.Set;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.med.base.replication.client.IReplicationLogic;
import py.una.med.base.replication.client.ReplicationInfo;
import py.una.med.base.replication.client.ReplicationInfoDao;
import py.una.med.base.replication.client.ReplicationLogic;
import py.una.med.base.test.base.BaseTestWithDatabase;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.test.replication.layers.ReplicatedEntity;
import py.una.med.base.test.test.replication.layers.ReplicatedEntityChild;
import py.una.med.base.test.util.TestDateProvider;
import py.una.med.base.test.util.TestUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 22, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReplicationLogicTest extends BaseTestWithDatabase {

	/**
	 * 
	 */
	private static final String ZERO_ID = "0";

	@Configuration
	@EnableTransactionManagement()
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Bean
		public IReplicationLogic manager() {

			return new ReplicationLogic();
		}

		@Bean
		ReplicationInfoDao dao() {

			return new ReplicationInfoDao();
		}

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(ReplicationInfo.class);
		}

	}

	@Autowired
	private IReplicationLogic logic;

	@Autowired
	private TestDateProvider dateProvider;

	@Test
	public void testRetrieveInfo() throws Exception {

		Set<ReplicationInfo> infos = logic.getActiveReplications();

		assertNotNull(infos);

		ReplicationInfo info = logic.getByClass(ReplicatedEntity.class);

		assertNotNull(info);
		assertEquals(info.getEntityClassName(),
				ReplicatedEntity.class.getName());
		assertEquals(info.getEntityClazz(), ReplicatedEntity.class);

	}

	/**
	 * Prueba si las llamadas con varias entidades funciona de manera correcta.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSingleEntityTest() throws Exception {

		// minute 0
		assertContains(logic.getReplicationsToDo(), ReplicatedEntity.class);
		logic.notifyReplication(ReplicatedEntity.class, ZERO_ID);

		assertNotContains(logic.getReplicationsToDo(), ReplicatedEntity.class);

		dateProvider.forward(6);
		// minute 6
		assertContains(logic.getReplicationsToDo(), ReplicatedEntity.class);
		logic.notifyReplication(ReplicatedEntity.class, ZERO_ID);
		assertNotContains(logic.getReplicationsToDo(), ReplicatedEntity.class);

		logic.updateSyncTime(ReplicatedEntity.class, 2);
		assertNotContains(logic.getReplicationsToDo(), ReplicatedEntity.class);

		dateProvider.forward(3);
		// minute 9
		assertContains(logic.getReplicationsToDo(), ReplicatedEntity.class);
		logic.notifyReplication(ReplicatedEntity.class, ZERO_ID);
		assertNotContains(logic.getReplicationsToDo(), ReplicatedEntity.class);

		dateProvider.forward(1);
		// minute 10
		assertNotContains(logic.getReplicationsToDo(), ReplicatedEntity.class);

		dateProvider.forward(1);
		// minute 11
		assertContains(logic.getReplicationsToDo(), ReplicatedEntity.class);
		logic.notifyReplication(ReplicatedEntity.class, ZERO_ID);
		assertNotContains(logic.getReplicationsToDo(), ReplicatedEntity.class);

	}

	@Test
	public void testAllEntitiesTimer() throws Exception {

		Set<ReplicationInfo> replicated = logic.getReplicationsToDo();

		assertContains(replicated, ReplicatedEntity.class);
		assertContains(replicated, ReplicatedEntityChild.class);

		logic.notifyReplication(ReplicatedEntity.class, ZERO_ID);

		replicated = logic.getReplicationsToDo();
		assertContains(replicated, ReplicatedEntityChild.class);

		logic.notifyReplication(ReplicatedEntityChild.class, ZERO_ID);

		replicated = logic.getReplicationsToDo();
		assertTrue(replicated.isEmpty());

	}

	void assertContains(Collection<ReplicationInfo> entity, Class<?> clazz) {

		for (ReplicationInfo e : entity) {
			if (e.getEntityClazz().equals(clazz)) {
				return;
			}
		}
		fail("The clazz " + clazz.getSimpleName()
				+ " is market as not sync yet");
	}

	void assertNotContains(Collection<ReplicationInfo> entity, Class<?> clazz) {

		for (ReplicationInfo e : entity) {
			if (e.getEntityClazz().equals(clazz)) {
				fail("The clazz " + clazz.getSimpleName() + " has been sync");
			}
		}
	}
}
