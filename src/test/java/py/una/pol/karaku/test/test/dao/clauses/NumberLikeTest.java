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
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.dao.where.MatchMode;
import py.una.pol.karaku.test.test.util.layers.ITestDAO;

/**
 * Clases de prueba para la {@link Clause} {@link NumberLike}
 *
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 *
 */
public class NumberLikeTest extends BaseClauseTest {

	/**
	 *
	 */
	private static final String BIG_DECIMAL = "bigDecimal";
	@Autowired
	ITestDAO dao;

	/**
	 * Pruebas con atributos directos
	 * <p>
	 * {@link TestEntity}
	 * </p>
	 */
	@Test
	public void testEntity() {

		assertThat(
				count(where().addClause(Clauses.numberLike(BIG_DECIMAL, 65))),
				is(2L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(BIG_DECIMAL, 1, MatchMode.BEGIN))),
				is(4L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(BIG_DECIMAL, 1287, MatchMode.EQUAL))),
				is(1L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(BIG_DECIMAL, "2", MatchMode.END))),
				is(2L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(BIG_DECIMAL, "1", MatchMode.BEGIN),
						Clauses.numberLike(BIG_DECIMAL, 5, MatchMode.END))),
				is(1L));
	}

	/**
	 * Pruebas con un nivel de join *
	 * <p>
	 * {@link TestEntity} -> {@link TestChild}
	 * </p>
	 */
	@Test
	public void testChild() {

		assertThat(
				count(where().addClause(Clauses.numberLike(c(BIG_DECIMAL), 65))),
				is(2L));

		// Son tres registros, que corresponden a 2 TestEntity
		assertThat(
				count(where().addClause(
						Clauses.numberLike(c(BIG_DECIMAL), "54",
								MatchMode.BEGIN))), is(2L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(c(BIG_DECIMAL), "1000",
								MatchMode.EQUAL))), is(1L));

		assertThat(
				count(where()
						.addClause(
								Clauses.numberLike(c(BIG_DECIMAL), "65",
										MatchMode.END))), is(2L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(c(BIG_DECIMAL), "23",
								MatchMode.BEGIN),
						Clauses.numberLike(c(BIG_DECIMAL), 57, MatchMode.END))),
				is(1L));
	}

	/**
	 * Pruebas con dos niveles de join
	 * <p>
	 * {@link TestEntity} -> {@link TestChild} -> {@link TestGrandChild}
	 * </p>
	 */
	@Test
	public void testGrandChild() {

		assertThat(
				count(where().addClause(Clauses.numberLike(g(BIG_DECIMAL), 5))),
				is(3L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(g(BIG_DECIMAL), "54",
								MatchMode.BEGIN))), is(2L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(g(BIG_DECIMAL), "1000",
								MatchMode.EQUAL))), is(1L));

		assertThat(
				count(where()
						.addClause(
								Clauses.numberLike(g(BIG_DECIMAL), "420",
										MatchMode.END))), is(1L));

		assertThat(
				count(where().addClause(
						Clauses.numberLike(g(BIG_DECIMAL), "5",
								MatchMode.BEGIN),
						Clauses.numberLike(g(BIG_DECIMAL), 20, MatchMode.END))),
				is(1L));
	}
}
