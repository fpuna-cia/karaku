/*
 * @WatcherTest.java 1.0 Nov 6, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.dao;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.med.base.dao.entity.Operation;
import py.una.med.base.dao.entity.watchers.AbstractWatcher;
import py.una.med.base.dao.entity.watchers.WatcherHandler;
import py.una.med.base.test.base.BaseTest;
import py.una.med.base.test.configuration.BaseTestConfiguration;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 6, 2013
 *
 */

@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class WatcherHandlerTest extends BaseTest {

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		WatcherHandler watcher() {

			return new WatcherHandler();
		}

		@Bean
		Watcher1 watcher1() {

			return new Watcher1();
		}

		@Bean
		Watcher2 watcher2() {

			return new Watcher2();
		}

	}

	@Autowired
	private WatcherHandler handler;

	@Autowired
	private Watcher1 watcher1;

	@Autowired
	private Watcher2 watcher2;

	@Test
	public void testInjection() throws Exception {

		assertNotNull(handler);
	}

	@Test
	public void testRegistration() throws Exception {

		assertThat(handler.getWatchers(), hasItem(watcher1));
		assertThat(handler.getWatchers(), hasItem(watcher2));

	}

	@Test
	public void testRedirect() throws Exception {

		assertEquals(handler.redirect(Operation.DELETE, DummyClass.class,
				new DummyClass()), Operation.UPDATE);
		assertEquals(handler.redirect(Operation.UPDATE, DummyClass.class,
				new DummyClass()), Operation.UPDATE);
		assertEquals(handler.redirect(Operation.CREATE, DummyClass.class,
				new DummyClass()), Operation.CREATE);

		assertEquals(handler.redirect(Operation.DELETE, DummyCalendar.class,
				new DummyCalendar()), Operation.DELETE);
		assertEquals(handler.redirect(Operation.UPDATE, DummyCalendar.class,
				new DummyCalendar()), Operation.UPDATE);
		assertEquals(handler.redirect(Operation.CREATE, DummyCalendar.class,
				new DummyCalendar()), Operation.CREATE);

		// assertThat(handler.verify(Operation.CREATE, DummyCalendar.class, mut)
		// .get(Calendar.YEAR));
		// assertThat(handler.handle(Operation.CREATE, DummyCalendar.class, mut)
		// .get(Calendar.YEAR), is(1900));

	}

	@Test
	public void testProcess() throws Exception {

		DummyClass dc = new DummyClass();
		dc.active = true;

		assertFalse(handler.process(Operation.DELETE, Operation.UPDATE,
				DummyClass.class, dc).isActive());

		assertEquals(
				2000,
				handler.process(Operation.DELETE, Operation.UPDATE,
						DummyCalendar.class, new DummyCalendar()).get(
						Calendar.YEAR));

	}

	static class DummyClass implements Dummy {

		boolean active = false;

		@Override
		public void toogle() {

			active = !active;
		}

		@Override
		public boolean isActive() {

			return active;
		}

	}

	static interface Dummy {

		void toogle();

		boolean isActive();
	}

	static class Watcher1 extends AbstractWatcher<Dummy> {

		@Override
		public Dummy process(Operation origin, Operation redirected, Dummy bean) {

			if ((origin == Operation.DELETE)
					&& (redirected == Operation.UPDATE)) {
				bean.toogle();
			}
			return bean;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Operation redirect(Operation operation, Dummy bean) {

			if (operation == Operation.DELETE) {
				return Operation.UPDATE;
			}
			return operation;
		}

	}

	static class Watcher2 extends AbstractWatcher<Calendar> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Calendar process(Operation origin, Operation redirected,
				Calendar bean) {

			bean.set(Calendar.YEAR, 2000);
			return bean;
		}

	}

	static class DummyCalendar extends GregorianCalendar {

		/**
		 *
		 */
		private static final long serialVersionUID = 6211845507699352942L;

	}
}
