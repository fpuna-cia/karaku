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
package py.una.pol.karaku.test.test.dao;

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
import py.una.pol.karaku.dao.entity.Operation;
import py.una.pol.karaku.dao.entity.watchers.AbstractWatcher;
import py.una.pol.karaku.dao.entity.watchers.WatcherHandler;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

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
