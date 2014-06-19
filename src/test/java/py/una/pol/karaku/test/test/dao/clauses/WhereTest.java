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
package py.una.pol.karaku.test.test.dao.clauses;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.dao.BaseDAO;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.repo.KarakuBaseDao;
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
 * Test de integración de {@link Where} del {@link KarakuBaseDao}. Ver el
 * archivo WhereTest.SQL
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
	@SQLFiles("RemoveTest")
	public void remove() {

		Long idToTest = 100L;
		dao.remove(idToTest);
		assertNull(dao.getById(idToTest));

		idToTest = 101L;
		TestEntity te = dao.getById(idToTest);
		dao.remove(te);
		assertNull(dao.getById(idToTest));

	}

	@Test
	public void whereFetchTest() {

		Where<TestEntity> where;
		TestEntity te;

		where = Where.get();
		where.addClause(Clauses.eq("id", 1L));
		where.fetch("testChild.grandChilds");
		te = dao.getAll(where, null).get(0);
		assertTrue(Hibernate.isInitialized(te.getTestChild().getGrandChilds()));

	}

	@Test
	public void testGetClone() {

		Where<TestEntity> where = new Where<TestEntity>();
		where.addClause(Clauses.eq("1", "a"));
		Where<TestEntity> where2 = where.getClone();
		where2.addClause(Clauses.eq("2", "b"));
		assertFalse(where == where2);
		assertNotEquals(where.getClauses().size(), where2.getClauses().size());
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
