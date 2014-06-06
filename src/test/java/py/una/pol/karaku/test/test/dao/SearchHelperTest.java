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
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.SearchHelper;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.dao.where.DateClauses;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.test.util.layers.ITestDAO;
import py.una.pol.karaku.test.test.util.layers.TestChild;
import py.una.pol.karaku.test.test.util.layers.TestDAO;
import py.una.pol.karaku.test.test.util.layers.TestEntity;
import py.una.pol.karaku.test.test.util.layers.TestGrandChild;
import py.una.pol.karaku.test.util.TestUtils;

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
