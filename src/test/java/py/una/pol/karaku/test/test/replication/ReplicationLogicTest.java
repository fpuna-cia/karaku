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
import py.una.pol.karaku.replication.client.IReplicationLogic;
import py.una.pol.karaku.replication.client.ReplicationInfo;
import py.una.pol.karaku.replication.client.ReplicationInfoDao;
import py.una.pol.karaku.replication.client.ReplicationLogic;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.test.replication.layers.ReplicatedEntity;
import py.una.pol.karaku.test.test.replication.layers.ReplicatedEntityChild;
import py.una.pol.karaku.test.util.TestDateProvider;
import py.una.pol.karaku.test.util.TestUtils;

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
