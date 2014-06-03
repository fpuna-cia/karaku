/*
 * @SearchHelperTest.java 1.0 Sep 19, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ReflectionUtils;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.search.SearchHelper;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.dao.where.DateClauses;
import py.una.med.base.test.base.BaseTestWithDatabase;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.test.util.layers.TestChild;
import py.una.med.base.test.test.util.layers.TestDAO;
import py.una.med.base.test.test.util.layers.TestEntity;
import py.una.med.base.test.test.util.layers.TestGrandChild;
import py.una.med.base.test.util.TestUtils;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 19, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SearchHelperTest extends BaseTestWithDatabase {

	@Configuration
	@EnableTransactionManagement()
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		@SuppressWarnings("unchecked")
		public Class<?>[] getEntityClasses() {

			return TestUtils.getAsArray(TestEntity.class, TestChild.class,
					TestGrandChild.class);
		}

		@Bean
		public ITestDAO testDAO() {

			return new TestDAO();
		}

		@Bean
		public SearchHelper searchHelper() {

			return new SearchHelper();
		}

	}

	@Autowired
	SearchHelper sh;

	@Autowired
	ITestDAO testDAO;

	@Autowired
	DateClauses c;

	@Test
	public void testSearch() {

		assertThat(this.bw("description", "TO"), is(6L));

		assertThat(this.bw("costo", "45"), is(2L));

	}

	@Test
	public void testSearchDate() {

		assertThat(this.bw("fecha", "17-12-2013"), is(1L));

		assertThat(this.bw("fecha", "17-12-13"), is(1L));

	}

	@Test
	public void testChildSearch() {

		assertThat(this.bw("testChild.description", "COSTO"), is(3L));

		assertThat(this.bw("testChild.bigDecimal", "65"), is(2L));

		assertThat(this.bw("testChild.fecha", "17-12-2013"), is(1L));

		assertThat(this.bw("testChild.fecha", "17-12-13"), is(1L));

	}

	Map<String, String> field1;

	@Test
	public void testGrandChildSearch() {

		assertThat(this.bw("testChild.grandChilds.description", "COSTO"),
				is(2L));

		assertThat(this.bw("testChild.grandChilds.bigDecimal", "542"), is(2L));

		assertThat(this.bw("testChild.grandChilds.fecha", "13-12-2013"), is(2L));

		assertThat(this.bw("testChild.grandChilds.fecha", "13-12-13"), is(2L));

	}

	@Test
	public void testAddToWhere() {

		Where<TestEntity> where = new Where<TestEntity>().makeDistinct()
				.addClause(
						Clauses.or(Clauses.iLike("description", "PETOTE"),
								this.sh.getClause(TestEntity.class,
										"testChild.fecha", "13-12-13")));

		assertThat(this.countWhere(where), is(2L));
	}

	@Test
	public void testGetField() throws NoSuchFieldException, SecurityException {

		assertEquals(ReflectionUtils.findField(TestEntity.class, "costo"),
				SearchHelper.getFieldInfo(TestEntity.class, "costo").getField());

		assertEquals(ReflectionUtils.findField(TestChild.class, "fecha"),
				SearchHelper.getFieldInfo(TestEntity.class, "testChild.fecha")
						.getField());

		assertEquals(
				ReflectionUtils.findField(TestGrandChild.class, "fecha"),
				SearchHelper.getFieldInfo(TestEntity.class,
						"testChild.grandChilds.fecha").getField());

	}

	public Long bw(String property, String value) {

		Where<TestEntity> where = this.sh.buildWhere(TestEntity.class,
				property, value).makeDistinct();
		return this.countWhere(where);
	}

	public Long countWhere(Where<TestEntity> where) {

		return this.testDAO.getCount(where);
	}

}
