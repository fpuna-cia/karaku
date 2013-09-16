/*
 * @WhereTest.java 1.0 Sep 10, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import java.math.BigDecimal;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.med.base.dao.BaseDAO;
import py.una.med.base.dao.restrictions.Where;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.repo.SIGHBaseDao;
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
 * Test de integración de {@link Where} del {@link SIGHBaseDao}. Ver el archivo
 * WhereTest.SQL
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 10, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class WhereTest extends BaseTestWithDatabase {

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

	/**
	 * Prueba si la configuración es correcta, probando la inyección de
	 * dependencias.
	 */
	@Test
	public void injection() {

		assertNotNull(dao);
	}

	/**
	 * Prueba la función de contar con tres métodos:
	 * 
	 * <ol>
	 * <li>{@link #byDescription(String)}</li>
	 * <li>{@link #byDescriptionOfChild(String)}</li>
	 * <li>{@link #byDescriptionOfGrandChild(String)}</li>
	 * 
	 * </ol>
	 */
	@Test
	public void count() {

		assertThat(dao.getCount(null), is(7L));

		assertThat(dao.getCount(byDescription("COSTO")), is(1L));

		assertThat(dao.getCount(byDescriptionOfChild("COSTO2_CHILD")), is(1L));

		assertThat(
				dao.getCount(byDescriptionOfGrandChild("COSTO")).longValue(),
				is(4L));

	}

	/**
	 * Prueba si puede acceder al primer registro creado
	 */
	@Test
	public void byId() {

		assertNotNull(dao.getById(1L));

		assertNull(dao.getById(1000L));
	}

	/**
	 * Prueba si se puede crear un registro
	 * 
	 * <p>
	 * Podemos ver que una manera mas fácil de probar seria crear el registro y
	 * llamar a {@link BaseDAO#getCount()}, pero esto esta conceptualmente mal
	 * por que si el método {@link BaseDAO#getCount()} esta mal, fallarán los
	 * métodos {@link #count()} y {@link #add()}, sin embargo, la creación de
	 * registro no tendría ningún problema.
	 * </p>
	 */
	@Test
	public void add() {

		String description = "UNIQUE DESCRIPTION";
		TestEntity te = new TestEntity();
		te.setDescription(description);
		te.setCosto(BigDecimal.ONE);

		TestEntity saved = dao.add(te);

		assertNotNull(saved);
		assertNotNull(saved.getId());
		assertEquals(saved, te);

		// assertThat(saved, is(logic.getAll(byDescription(description), null)
		// .get(0)));
	}

	/**
	 * TODO ver como hacer para que no dependa del test de {@link #byId()}
	 */
	@Test
	public void update() {

		TestEntity te = dao.getById(1L);
		String previus = te.getDescription();
		String next = previus + "NEXT_GEM";
		te.setDescription(next);

		TestEntity updated = dao.update(te);

		assertThat(updated.getDescription(), is(not(previus)));
		assertThat(updated.getDescription(), is(next));
		assertThat(updated, is(te));

	}

	/**
	 * Se prueban los métodos {@link BaseDAO#remove(Object)} y
	 * {@link BaseDAO#remove(java.io.Serializable)}
	 * <p>
	 * TODO ver como hacer para que no dependa del test de {@link #byId()}
	 * </p>
	 */
	@Test
	@SQLFiles(filesToLoad = "RemoveTest")
	public void remove() {

		Long idToTest = 100L;
		dao.remove(idToTest);
		assertNull(dao.getById(idToTest));

		idToTest = 101L;
		TestEntity te = dao.getById(idToTest);
		dao.remove(te);
		assertNull(dao.getById(idToTest));

	}

	/**
	 * Retorna un {@link Where} que busca por la descripción de
	 * {@link TestGrandChild}
	 * 
	 * @return {@link Where}
	 */
	private Where<TestEntity> byDescriptionOfGrandChild(String description) {

		Where<TestEntity> byGrandChild = new Where<TestEntity>();
		byGrandChild.addClause(Clauses.iLike(
				"testChild.grandChilds.description", description));
		return byGrandChild;
	}

	/**
	 * Retorna un {@link Where} que busca por la descripción de
	 * {@link TestChild}
	 * 
	 * @return {@link Where}
	 */
	private Where<TestEntity> byDescriptionOfChild(String description) {

		Where<TestEntity> byDescriptionOfChild = new Where<TestEntity>();
		byDescriptionOfChild.addClause(Clauses.iLike("testChild.description",
				description));
		return byDescriptionOfChild;
	}

	/**
	 * Retorna un {@link Where} que busca por la descripción de
	 * {@link TestEntity}
	 * 
	 * @return {@link Where}
	 */
	private Where<TestEntity> byDescription(String description) {

		Where<TestEntity> byDescription = new Where<TestEntity>();
		byDescription.addClause(Clauses.eq("description", description));
		return byDescription;
	}
}
