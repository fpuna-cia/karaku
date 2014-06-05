/*-
 * Copyright (c)
 *
 * 		2012-2014, Facultad Polit閏nica, Universidad Nacional de Asunci髇.
 * 		2012-2014, Facultad de Ciencias M閐icas, Universidad Nacional de Asunci髇.
 * 		2012-2013, Centro Nacional de Computaci髇, Universidad Nacional de Asunci髇.
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
package py.una.pol.karaku.test.test.dao.clauses;

import static org.junit.Assert.assertNotNull;
import javax.annotation.Nonnull;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clause;
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
 * Clase que se utiliza para facilitar la realizaci贸n de test de los diferentes
 * tipos de {@link Clause}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
@SQLFiles("WhereTest")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class BaseClauseTest extends BaseTestWithDatabase {

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

	@Autowired
	SessionFactory sessionFactory;

	Where<TestEntity> where() {

		return new Where<TestEntity>().makeDistinct();
	}

	Long count(Where<TestEntity> where) {

		return dao.getCount(where);
	}

	/**
	 * Prueba si los elementos necesarios se pueden injectar.
	 */
	@Test
	public void testInjection() {

		assertNotNull(dao);
		assertNotNull(sessionFactory);
	}

	/**
	 * Prueban si los m茅todos por defecto se ejecutan en una transacci贸n
	 */
	@Test
	public void testTransaction() {

		assertNotNull(sessionFactory.getCurrentSession());
	}

	/**
	 * Retorna un {@link Where} que busca por la descripci贸n de
	 * {@link TestGrandChild}
	 * 
	 * @return {@link Where}
	 */
	@Nonnull
	String g(String property) {

		return "testChild.grandChilds." + property;
	}

	/**
	 * Retorna un {@link Where} que busca por la descripci贸n de
	 * {@link TestChild}
	 * 
	 * @return {@link Where}
	 */
	@Nonnull
	String c(String property) {

		return "testChild." + property;
	}

}
