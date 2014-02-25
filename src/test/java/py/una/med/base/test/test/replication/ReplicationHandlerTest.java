/*
 * @ReplicationManagerTest.java 1.0 Nov 22, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.replication;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import py.una.med.base.model.SIGHRevisionEntity;
import py.una.med.base.replication.EntityNotFoundException;
import py.una.med.base.replication.client.CacheAll;
import py.una.med.base.replication.client.IReplicationLogic;
import py.una.med.base.replication.client.ReplicationHandler;
import py.una.med.base.replication.client.ReplicationInfo;
import py.una.med.base.replication.client.ReplicationInfoDao;
import py.una.med.base.replication.client.ReplicationLogic;
import py.una.med.base.replication.client.ReplicationRequestFactory;
import py.una.med.base.replication.client.ReplicationResponseHandler;
import py.una.med.base.services.AbstractConverter;
import py.una.med.base.services.ConverterProvider;
import py.una.med.base.services.client.WSSecurityInterceptor;
import py.una.med.base.test.base.BaseTestWithDatabase;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.test.replication.layers.ReplicatedEntity;
import py.una.med.base.test.test.replication.layers.ReplicatedEntityDao;
import py.una.med.base.test.test.replication.layers.ReplicatedEntityResponse;
import py.una.med.base.test.util.TestDateProvider;
import py.una.med.base.test.util.TestPropertiesUtil;
import py.una.med.base.test.util.TestUriCache;
import py.una.med.base.test.util.TestUtils;
import py.una.med.base.test.util.transaction.SQLFiles;
import py.una.med.base.util.DateProvider;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 22, 2013
 * 
 */
@SQLFiles
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class ReplicationHandlerTest extends BaseTestWithDatabase {

	@Configuration
	@EnableTransactionManagement()
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(ReplicatedEntity.class,
					ReplicationInfo.class, SIGHRevisionEntity.class);
		}

		@Bean
		ReplicationHandler replicationHandler() {

			return new ReplicationHandler();
		}

		@Bean
		DateProvider dateProvider() {

			return new TestDateProvider();
		}

		@Bean
		IReplicationLogic iReplicationLogic() {

			return new ReplicationLogic();
		}

		@Bean
		ReplicationInfoDao dao() {

			return new ReplicationInfoDao();
		}

		@Bean
		WSSecurityInterceptor interceptor() {

			return new WSSecurityInterceptor();
		}

		@Bean
		WebServiceTemplate serviceTemplate() {

			return new WSTemplate();
		}

		@Bean
		ConverterProvider converterProvider() {

			return new ConverterProvider();
		}

		@Bean
		ReplicatedEntityDao entityDao() {

			return new ReplicatedEntityDao();
		}

		@Bean
		ReplicationRequestFactory requestFactory() {

			return new ReplicationRequestFactory();
		}

		@Bean
		ReplicationResponseHandler responseHandler() {

			return new ReplicationResponseHandler();
		}

		@Override
		protected boolean getWithEnvers() {

			return true;
		}

		@Bean
		TestUriCache testUriCache() {

			return new TestUriCache();
		}

		@Bean
		ReplicatedEntityConverter replicatedEntityConverter() {

			return new ReplicatedEntityConverter();
		}

	}

	@Autowired
	private TestDateProvider dateProvider;

	@Autowired
	private ReplicationHandler replicationHandler;

	@Autowired
	private IReplicationLogic logic;

	@Autowired
	private ReplicatedEntityDao dao;

	@Autowired
	private WSTemplate template;
	@Autowired
	TestPropertiesUtil propertiesUtil;

	@Test
	public void testDoSync() throws Exception {

		propertiesUtil.put(ReplicationHandler.REPLICATION_ENABLED, "true");
		ReplicatedEntity re1 = new ReplicatedEntity();
		re1.setDescription("DESC1");
		re1.setUri("re1");

		ReplicatedEntity re2 = new ReplicatedEntity();
		re2.setUri("re2");
		re2.setDescription("222");

		template.addReplicatedEntity(re1);

		replicationHandler.doSync();
		// the first is skiped
		replicationHandler.doSync();
		assertTrue(logic.getReplicationsToDo().isEmpty());
		assertThat(dao.getCount(), is(1L));
		assertThat(dao.getAll(null), hasItem(re1));

		// minutos 2
		// limpiamos todo y agregamos dos, una de las cuales ya es vieja
		dateProvider.forward(2);
		template.clear();

		re1.setDescription("updated");

		template.addReplicatedEntity(re1);
		template.addReplicatedEntity(re2);

		assertFalse("dont have changes to sync", logic.getReplicationsToDo()
				.isEmpty());
		replicationHandler.doSync();
		assertTrue(logic.getReplicationsToDo().isEmpty());
		assertThat(dao.getCount(), is(2L));
		assertThat(dao.getAll(null), hasItems(re1, re2));

	}

	//
	@CacheAll
	public static class ReplicatedEntityConverter extends
			AbstractConverter<ReplicatedEntity, ReplicatedEntity> {

		@Override
		public ReplicatedEntity toEntity(ReplicatedEntity dto)
				throws EntityNotFoundException {

			return dto;
		}

		@Override
		public ReplicatedEntity toDTO(ReplicatedEntity entity, int depth) {

			return entity;
		}
	}

	private static class WSTemplate extends WebServiceTemplate {

		List<ReplicatedEntity> entities = new ArrayList<ReplicatedEntity>();

		@Override
		public Object marshalSendAndReceive(String uri, Object requestPayload,
				WebServiceMessageCallback requestCallback) {

			return getEntityResponse();
		}

		public void clear() {

			entities.clear();
		}

		public void addReplicatedEntity(ReplicatedEntity re) {

			entities.add(re);
		}

		private ReplicatedEntityResponse getEntityResponse() {

			ReplicatedEntityResponse toRet = new ReplicatedEntityResponse();
			toRet.setEntities(entities);
			return toRet;
		}
	}

}
