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
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import py.una.pol.karaku.replication.DTO;
import py.una.pol.karaku.replication.Shareable;
import py.una.pol.karaku.replication.client.ReplicationInfo;
import py.una.pol.karaku.replication.client.ReplicationRequestFactory;
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
public class ReplicationRequestFactoryTest extends BaseTest {

	/**
	 * 
	 */
	private static final String ZERO = "ZERO";

	@Configuration
	static class ContextConfiguration extends BaseTestConfiguration {

		@Bean
		ReplicationRequestFactory type() {

			return new ReplicationRequestFactory();
		}

	}

	@Autowired
	private ReplicationRequestFactory factory;

	@Test
	public void testRequestMessage() throws Exception {

		Request1 r = factory.createMessage(gri(Request1.class, ZERO));
		assertEquals(ZERO, r.id);

		Request2 r2 = factory.createMessage(gri(Request2.class, ZERO));
		assertEquals(ZERO, r2.lastId);
	}

	/**
	 * @param class1
	 * @param zero2
	 * @return
	 */
	private ReplicationInfo gri(Class<?> class1, String zero2) {

		return new ReplicationInfo(1L, zero2, Shareable.class, DTO.class,
				class1, class1, 1);
	}

	public static class Request1 {

		String id;
	}

	public static class Request2 {

		String lastId;
	}
}
