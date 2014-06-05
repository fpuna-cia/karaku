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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.replication.ShareableWatcher;
import py.una.pol.karaku.replication.server.Bundle;
import py.una.pol.karaku.replication.server.DummyFirstChangeProvider;
import py.una.pol.karaku.replication.server.DummyReplicationProvider;
import py.una.pol.karaku.replication.server.FirstChangeProviderHandler;
import py.una.pol.karaku.replication.server.ReplicationProvider;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.test.replication.layers.ReplicatedEntity;
import py.una.pol.karaku.test.test.replication.layers.ReplicatedEntityDao;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.test.util.transaction.Sequences;

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
		ReplicatedEntityDao dao() {

			return new ReplicatedEntityDao();
		}

		@Bean
		ReplicationProvider provider() {

			return new DummyReplicationProvider();
		}

		@Bean
		ShareableWatcher shareableWatcher() {

			return new ShareableWatcher();
		}

		@Bean
		DummyFirstChangeProvider dummyFirstChangeProvider() {

			return new DummyFirstChangeProvider();
		}

		@Bean
		FirstChangeProviderHandler type() {

			return new FirstChangeProviderHandler();
		}

	}

	@Autowired
	private ReplicatedEntityDao dao;

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

		final String id = Bundle.ZERO_ID;
		assertThat("SQL created entities are not registered",
				rp.getChanges(ReplicatedEntity.class, id).size(), is(5));

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
