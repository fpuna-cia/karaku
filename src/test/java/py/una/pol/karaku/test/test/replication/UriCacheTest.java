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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.domain.BaseEntity;
import py.una.pol.karaku.replication.EntityNotFoundException;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.util.TestDateProvider;
import py.una.pol.karaku.test.util.TestUriCache;
import py.una.pol.karaku.test.util.TestUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 27, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class UriCacheTest extends BaseTestWithDatabase {

	@Configuration
	@EnableTransactionManagement
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Bean
		TestUriCache cache() {

			return new TestUriCache();
		}

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getAsClassArray(Entity1.class, Entity2.class,
					Entity3.class);
		}

	}

	@Before
	public void clearCache() {

		cache.clearCache();
	}

	@Autowired
	private TestUriCache cache;

	@Autowired
	private TestDateProvider dateProvider;

	@Test
	public void testAddToCache() throws Exception {

		// MINUTE 0
		assertFalse(cache.hasInCache("entity/1"));
		Entity1 e = cache.getByUri(Entity1.class, "entity/1");
		assertNotNull("The entity can not been fetched", e);
		assertTrue(cache.hasInCache("entity/1"));
		// Solo se debe recuperar el ID
		assertNull("The entity must not have uri", e.getUri());
		assertThat("The entity has to have a ID=1", e.getId(), is(1L));
		assertTrue("The entity has not been cached now",
				cache.hasBeenAddedNow("entity/1"));

		// MINUTE 1
		dateProvider.forward(1);
		assertNotNull(cache.getByUri(Entity1.class, "entity/1"));
		assertFalse("The entity has been recached incorrectly",
				cache.hasBeenAddedNow("entity/1"));

		assertNotNull(cache.getByUri(Entity2.class, "entity/2/1").getId());
		assertTrue(cache.hasBeenAddedNow("entity/2/1"));
		dateProvider.forward(2);
		assertFalse(cache.hasBeenAddedNow("entity/2/1"));
		assertThat(cache.getCount(), is(2));

		assertNotNull(cache.getByUri(Entity2.class, "entity/2/2").getId());
		assertNotNull(cache.getByUri(Entity2.class, "entity/2/3").getId());
		assertNotNull(cache.getByUri(Entity2.class, "entity/2/4").getId());
		assertNotNull(cache.getByUri(Entity1.class, "entity/2").getId());
		assertNotNull(cache.getByUri(Entity1.class, "entity/3").getId());
		assertNotNull(cache.getByUri(Entity1.class, "entity/4").getId());

		assertThat(cache.getCount(), is(8));

	}

	@Test
	public void testClear() throws Exception {

		assertNotNull(cache.getByUri(Entity2.class, "entity/2/2").getId());
		assertNotEquals(0, cache.getCount());

		dateProvider.forward(1);
		cache.clearCache();
		assertEquals(0, cache.getCount());
		assertNotNull(cache.getByUri(Entity2.class, "entity/2/2").getId());
		assertTrue(cache.hasBeenAddedNow("entity/2/2"));
	}

	@Test
	public void testLoadTable() throws Exception {

		assertFalse(cache.hasInCache("entity/1"));
		assertFalse(cache.hasInCache("entity/2"));

		cache.loadTable(Entity1.class);

		assertTrue(cache.hasInCache("entity/1"));
		assertTrue(cache.hasInCache("entity/2"));

	}

	@Test(expected = RuntimeException.class)
	public void testDontCreateImposibleInstances() throws Exception {

		cache.getByUri(Entity3.class, "entity/3/1");
	}

	@Test(expected = EntityNotFoundException.class)
	public void testName() throws Exception {

		Entity2 e = cache.getByUri(Entity2.class, "asdklfjasdkl");
		System.out.println(e.getId());
	}

	@Entity
	@Table(name = "entity_1")
	public static class Entity1 extends BaseEntity implements Shareable {

		private static final long serialVersionUID = 7253465151311346449L;
		boolean active;
		String uri;
		@Id
		Long id;

		@Override
		public String getUri() {

			return uri;
		}

		@Override
		public void inactivate() {

			active = false;

		}

		@Override
		public void activate() {

			active = true;

		}

		@Override
		public boolean isActive() {

			return active;
		}

		@Override
		public void setId(Long id) {

			this.id = id;

		}

		@Override
		public Long getId() {

			return id;
		}

	}

	@Entity
	@Table(name = "entity_2")
	public static class Entity2 extends BaseEntity implements Shareable {

		private static final long serialVersionUID = -4125642538748883961L;
		boolean active;
		String uri;
		@Id
		Long id;

		@Override
		public String getUri() {

			return uri;
		}

		@Override
		public void inactivate() {

			active = false;

		}

		@Override
		public void activate() {

			active = true;

		}

		@Override
		public boolean isActive() {

			return active;
		}

		@Override
		public void setId(Long id) {

			this.id = id;

		}

		@Override
		public Long getId() {

			return id;
		}

	}

	@Entity
	@Table(name = "entity_3")
	public static class Entity3 extends BaseEntity implements Shareable {

		private static final long serialVersionUID = -4125642538748883961L;
		boolean active;
		String uri;
		@Id
		Long id;

		/**
		 * 
		 */
		public Entity3(Long id) {

		}

		@Override
		public String getUri() {

			return uri;
		}

		@Override
		public void inactivate() {

			active = false;

		}

		@Override
		public void activate() {

			active = true;

		}

		@Override
		public boolean isActive() {

			return active;
		}

		@Override
		public void setId(Long id) {

			this.id = id;

		}

		@Override
		public Long getId() {

			return id;
		}

	}
}
