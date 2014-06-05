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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.replication.client.ReplicationResponseHandler;
import py.una.pol.karaku.test.base.BaseTest;
import py.una.pol.karaku.test.configuration.BaseTestConfiguration;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 26, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReplicationResponseHandlerTest extends BaseTest {

	/**
	 * 
	 */
	private static final String ZERO = "ZERO";

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		ReplicationResponseHandler type() {

			return new ReplicationResponseHandler();
		}

	}

	@Autowired
	private ReplicationResponseHandler handler;

	@Test
	public void testHandleResponse() throws Exception {

		ReplicationResponseTest1 t1 = new ReplicationResponseTest1();
		List<Object> td1 = new ArrayList<Object>();
		t1.data = td1;
		t1.id = ZERO;

		Pair<String, Collection<?>> changes = handler.getChanges(t1);
		assertEquals(changes.getKey(), ZERO);
		assertEquals(changes.getValue(), td1);

		ReplicationResponseTest2 t2 = new ReplicationResponseTest2();
		Set<Object> td2 = new HashSet<Object>();
		t2.entitiess = td2;
		t2.lastId = ZERO;

		Pair<String, Collection<?>> changes2 = handler.getChanges(t1);
		assertEquals(changes2.getKey(), ZERO);
		assertEquals(changes2.getValue(), td1);
	}

	public static class ReplicationResponseTest1 {

		String id;
		List<Object> data;
	}

	public static class ReplicationResponseTest2 {

		String lastId;
		Set<Object> entitiess;
	}
}
