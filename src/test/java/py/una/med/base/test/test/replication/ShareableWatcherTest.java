/*
 * @WatcherTest.java 1.0 Nov 5, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.replication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import javax.persistence.Entity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.dao.entity.Operation;
import py.una.med.base.replication.Shareable;
import py.una.med.base.replication.ShareableWatcher;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

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
