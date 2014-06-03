/*
 * @ReplicationEndpointTest.java 1.0 Nov 8, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.test.test.replication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.test.server.ResponseMatchers;
import py.una.pol.karaku.replication.server.Bundle;
import py.una.pol.karaku.replication.server.DummyFirstChangeProvider;
import py.una.pol.karaku.replication.server.DummyReplicationProvider;
import py.una.pol.karaku.replication.server.FirstChangeProviderHandler;
import py.una.pol.karaku.replication.server.ReplicationProvider;
import py.una.pol.karaku.services.ConverterProvider;
import py.una.pol.karaku.test.base.BaseTestWebService;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.configuration.WebServiceTestConfiguration;
import py.una.pol.karaku.test.test.replication.layers.ReplicatedEntity;
import py.una.pol.karaku.test.test.replication.layers.ReplicationEntityEndpoint;
import py.una.pol.karaku.test.test.replication.layers.ReplicationEntityRequest;
import py.una.pol.karaku.test.test.replication.layers.ReplicationEntityResponse;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.test.util.transaction.DatabasePopulatorExecutionListener;
import py.una.pol.karaku.test.util.transaction.SQLFiles;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 8, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@SQLFiles("ReplicationProviderTest")
@TestExecutionListeners({ TransactionalTestExecutionListener.class,
		DatabasePopulatorExecutionListener.class })
public class ReplicationEndpointTest extends BaseTestWebService {

	@Configuration
	public static class ContextConfiguration extends
			WebServiceTestConfiguration {

		@Override
		public Class<?>[] getClassesToBound() {

			return TestUtils.getAsClassArray(ReplicationEntityRequest.class,
					ReplicationEntityResponse.class);
		}

		@Bean
		ReplicationEntityEndpoint endpoint() {

			return new ReplicationEntityEndpoint();
		}

		@Bean
		ReplicationProvider provider() {

			return new DummyReplicationProvider();
		}

		@Bean
		ConverterProvider converterProvider() {

			return new ConverterProvider();
		}

		@Bean
		FirstChangeProviderHandler changeProviderHandler() {

			return new FirstChangeProviderHandler();
		}

		@Bean
		DummyFirstChangeProvider dummyFirstChangeProvider() {

			return new DummyFirstChangeProvider();
		}
	}

	@Configuration
	public static class TransactionConfiguration extends
			TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(ReplicatedEntity.class);
		}
	}

	@Test
	@Transactional
	public void testCallMocked() {

		ReplicationEntityRequest mr = new ReplicationEntityRequest();
		mr.setLastId(Bundle.ZERO_ID);
		sendRequest(mr).andExpect(ResponseMatchers.noFault());
		ReplicationEntityResponse response = getResponse();
		assertNotNull("Null response", response);
		assertNotNull("Response with null entities", response.getEntities());
		assertEquals(5, response.getEntities().size());
	}

}
