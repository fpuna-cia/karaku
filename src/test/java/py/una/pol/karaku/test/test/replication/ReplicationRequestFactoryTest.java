/*
 * @ReplicationRequestFactoryTest.java 1.0 Nov 26, 2013 Sistema Integral de
 * Gestion Hospitalaria
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
