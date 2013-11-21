/*
 * @ReplicationEndpointTest.java 1.0 Nov 8, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.replication;

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
import py.una.med.base.replication.Bundle;
import py.una.med.base.replication.DummyReplicationProvider;
import py.una.med.base.replication.ReplicationProvider;
import py.una.med.base.test.base.BaseTestWebService;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.configuration.WebServiceTestConfiguration;
import py.una.med.base.test.test.replication.layers.ReplicatedEntity;
import py.una.med.base.test.test.replication.layers.ReplicationEntityEndpoint;
import py.una.med.base.test.test.replication.layers.ReplicationEntityRequest;
import py.una.med.base.test.test.replication.layers.ReplicationEntityResponse;
import py.una.med.base.test.util.TestUtils;
import py.una.med.base.test.util.transaction.DatabasePopulatorExecutionListener;
import py.una.med.base.test.util.transaction.SQLFiles;

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
