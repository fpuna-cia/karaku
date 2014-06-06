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
package py.una.pol.karaku.test.test.dao.searchParam;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.dao.search.ISearchParam;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.test.util.layers.ITestDAO;
import py.una.pol.karaku.test.test.util.layers.TestChild;
import py.una.pol.karaku.test.test.util.layers.TestDAO;
import py.una.pol.karaku.test.test.util.layers.TestEntity;
import py.una.pol.karaku.test.test.util.layers.TestGrandChild;
import py.una.pol.karaku.test.util.TestUtils;
import py.una.pol.karaku.test.util.transaction.SQLFiles;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Oct 7, 2013
 * 
 */
@SQLFiles
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class SearchParamTest extends BaseTestWithDatabase {

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

	}

	@Autowired
	ITestDAO dao;

	@Test
	public void simpleSortTest() {

		checkOrder(new SearchParam().addOrder("fecha", false), "TOTE",
				"PETOTE", "PERO");

		checkOrder(new SearchParam().addOrder("fecha"), "COSTO", "CASTO",
				"TESTO");

		checkOrder(new SearchParam().addOrder("bigDecimal", false), "TASTO",
				"PETOTE", "PERO");

		checkOrder(new SearchParam().addOrder("bigDecimal"), "COSTO", "PERO",
				"PETOTE", "TOTE");
	}

	@Test
	public void nestSortTest() {

		checkOrder(new SearchParam().addOrder("testChild.fecha", false),
				"TOTE", "PETOTE", "PERO");

		checkOrder(new SearchParam().addOrder("testChild.fecha"), "COSTO",
				"CASTO", "TESTO");

		checkOrder(new SearchParam().addOrder("testChild.bigDecimal", false),
				"TOTE", "PERO", "PETOTE");

		checkOrder(new SearchParam().addOrder("testChild.bigDecimal"), "COSTO",
				"TASTO", "TESTO", "CASTO");
	}

	private void checkOrder(ISearchParam sp, String ... descriptions) {

		TestUtils.checkOrder(dao.getAll(sp), descriptions);
	}

}
