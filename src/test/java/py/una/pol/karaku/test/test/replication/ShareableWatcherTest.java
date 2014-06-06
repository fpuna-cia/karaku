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
import static org.junit.Assert.assertFalse;
import javax.persistence.Entity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.replication.ShareableWatcher;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 5, 2013
 *
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ShareableWatcherTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		ShareableWatcher shareableWatcher() {

			return new ShareableWatcher();
		}

	}

	@Autowired
	private ShareableWatcher watcher;

	@Test
	public void testRedirect() throws Exception {

		assertEquals("Watcher dont redirect righ C->C", Operation.CREATE,
				watcher.redirect(Operation.CREATE, g(true)));
		assertEquals("Watcher dont redirect righ U->U", Operation.UPDATE,
				watcher.redirect(Operation.UPDATE, g(true)));
		assertEquals("Watcher dont redirect righ D->U", Operation.UPDATE,
				watcher.redirect(Operation.DELETE, g(true)));
		assertEquals("Watcher dont redirect righ D->U", Operation.UPDATE,
				watcher.redirect(Operation.DELETE, g(false)));

	}

	@Test
	public void testProcess() throws Exception {

		assertFalse("Watcher dont change state",
				watcher.process(Operation.DELETE, Operation.UPDATE, g(false))
						.isActive());
		assertFalse("Watcher is not idempotent",
				watcher.process(Operation.DELETE, Operation.UPDATE, g(true))
						.isActive());
	}

	private EntityShareable g(boolean active) {

		EntityShareable ee = new EntityShareable();
		ee.state = active;
		return ee;
	}

	@Entity
	public static class EntityShareable implements Shareable {

		boolean state;

		@Override
		public String getUri() {

			return null;
		}

		@Override
		public void inactivate() {

			state = false;
		}

		@Override
		public void activate() {

			state = true;
		}

		@Override
		public boolean isActive() {

			return state;
		}

	}

	@Entity
	public static class EntityNotShareable {

	}
}
