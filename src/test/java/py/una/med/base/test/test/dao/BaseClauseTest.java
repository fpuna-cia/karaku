/*
 * @BaseClauseTest.java 1.0 Aug 19, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.dao;

import static org.junit.Assert.assertNotNull;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.test.base.BaseTestWithDatabase;
import py.una.med.base.test.configuration.TransactionTestConfiguration;
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.test.util.layers.TestChild;
import py.una.med.base.test.test.util.layers.TestDAO;
import py.una.med.base.test.test.util.layers.TestEntity;
import py.una.med.base.test.test.util.layers.TestGrandChild;
import py.una.med.base.test.util.Util;
import py.una.med.base.test.util.transaction.SQLFiles;

/**
 * Clase que se utiliza para facilitar la realización de test de los diferentes
 * tipos de {@link Clause}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
@SQLFiles(filesToLoad = "WhereTest")
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class BaseClauseTest extends BaseTestWithDatabase {

	@Configuration
	@EnableTransactionManagement()
	static class ContextConfiguration extends TransactionTestConfiguration {

		@Override
		@SuppressWarnings("unchecked")
		public Class<?>[] getEntityClasses() {

			return Util.getAsArray(TestEntity.class, TestChild.class,
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
	 * Prueban si los métodos por defecto se ejecutan en una transacción
	 */
	@Test
	public void testTransaction() {

		assertNotNull(sessionFactory.getCurrentSession());
	}

	/**
	 * Retorna un {@link Where} que busca por la descripción de
	 * {@link TestGrandChild}
	 * 
	 * @return {@link Where}
	 */
	String g(String property) {

		return "testChild.grandChilds." + property;
	}

	/**
	 * Retorna un {@link Where} que busca por la descripción de
	 * {@link TestChild}
	 * 
	 * @return {@link Where}
	 */
	String c(String property) {

		return "testChild." + property;
	}

}
