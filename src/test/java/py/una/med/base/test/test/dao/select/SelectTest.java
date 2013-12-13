/*
 * @SelectTest.java 1.0 Oct 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.dao.select;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.SearchParam;
import py.una.med.base.dao.select.Select;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.test.base.BaseTestWithDatabase;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.test.util.layers.TestDaoLayers;
import py.una.med.base.test.test.util.layers.TestEntity;
import py.una.med.base.test.test.util.layers.TestGrandChild;
import py.una.med.base.test.util.TestUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 13, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SelectTest extends BaseTestWithDatabase {

	@Configuration
	@EnableTransactionManagement
	@ComponentScan(basePackageClasses = TestDaoLayers.class)
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		public Class<?>[] getEntityClasses() {

			return TestUtils.getReferencedClasses(TestEntity.class);
		};

	}

	@Autowired
	ITestDAO dao;

	@Test
	public void testSelect() {

		Select s = Select.build("id", "costo");
		List<TestEntity> entities = dao.get(s);
		assertNotNull(entities);
		for (TestEntity te : entities) {
			assertNotNull(te.getId());
			assertNotNull(te.getCosto());
			assertNull(te.getBigDecimal());
		}
	}

	@Test
	public void testSelectJoin() {

		Select s = Select.build("id", "testChild.id",
				"testChild.grandChilds.id");
		List<TestEntity> entities = dao.get(s);
		assertNotNull(entities);
		for (TestEntity te : entities) {
			assertNotNull(te.getId());
			assertNotNull(te.getTestChild());
			assertNotNull(te.getTestChild().getId());
			assertNull(te.getBigDecimal());

			assertNotNull(te.getTestChild().getGrandChilds());
			for (TestGrandChild tgc : te.getTestChild().getGrandChilds()) {

				assertNotNull(tgc.getId());
			}
		}
	}

	@Test
	public void testSelectJoinLost() {

		Select s = Select.build("id", "testChild.grandChilds.id");
		List<TestEntity> entities = dao.get(s);
		assertNotNull(entities);
		for (TestEntity te : entities) {
			assertNotNull(te.getId());
			assertNotNull(te.getTestChild());
			assertNull(te.getBigDecimal());
			assertNotNull(te.getTestChild().getGrandChilds());
			for (TestGrandChild tgc : te.getTestChild().getGrandChilds()) {

				assertNotNull(tgc.getId());
			}
		}
	}

	@Test
	public void testSelectWhere() {

		List<TestEntity> entitites = dao.get(Select.build("description"),
				new Where<TestEntity>().addClause(Clauses.iLike("description",
						"TO")), new SearchParam().addOrder("description"));

		// List<TestEntity> entitites =
		// SqlBuilder.with(dao).select("description").and("id")
		// .where().iLike("description", "TO")).orderBy("description");
		// List<TestEntity> entitites2 = dao.select("description", "id").where()
		// .contain("description", "TO").orderBy("description");

		assertThat(entitites.size(), is(6));
		TestUtils.checkOrder(entitites, "CASTO", "COSTO", "PETOTE", "TASTO",
				"TESTO", "TOTE");
	}
}
