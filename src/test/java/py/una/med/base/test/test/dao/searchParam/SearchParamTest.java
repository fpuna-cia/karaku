/*
 * @SearchParamTest.java 1.0 Oct 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.dao.searchParam;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.med.base.dao.search.ISearchParam;
import py.una.med.base.dao.search.SearchParam;
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

		checkOrder(new SearchParam().addOrder("testChild.fecha", false), "TOTE",
				"PETOTE", "PERO");

		checkOrder(new SearchParam().addOrder("testChild.fecha"), "COSTO", "CASTO",
				"TESTO");

		checkOrder(new SearchParam().addOrder("testChild.bigDecimal", false), "TOTE",
				"PERO", "PETOTE");

		checkOrder(new SearchParam().addOrder("testChild.bigDecimal"), "COSTO", "TASTO",
				"TESTO", "CASTO");
	}

	private void checkOrder(ISearchParam sp, String ... descriptions) {

		List<TestEntity> all = dao.getAll(sp);
		checkOrder(all, descriptions.length, descriptions);
	}

	private void checkOrder(List<TestEntity> list, int count,
			String ... descriptions) {

		for (int i = 0; i < count; i++) {
			System.out.println("Esperando: " + descriptions[i] + " vino: "
					+ list.get(i).getDescription());
			assertEquals(descriptions[i], list.get(i).getDescription());
		}
		System.out.println("=---=");
	}
}
