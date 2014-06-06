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
import py.una.pol.karaku.dao.where.Clause;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.dao.where.MatchMode;
import py.una.pol.karaku.test.test.util.layers.ITestDAO;

/**
 * Clases de prueba para la {@link Clause} {@link ILike}
 * <p>
 * Este es un test de integración, que depende de que {@link ILikeTest}
 * funcione correctamente.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
public class NotTest extends BaseClauseTest {

	/**
	 * 
	 */
	private static final String DESCRIPTION = "description";
	@Autowired
	ITestDAO dao;

	/**
	 * Pruebas con atributos directos
	 * <p>
	 * {@link TestEntity}
	 * </p>
	 */
	@Test
	public void main() {

		assertThat(
				count(where().addClause(
						n(like(DESCRIPTION, "TE", MatchMode.CONTAIN)))), is(4L));

		assertThat(
				count(where().addClause(
						n(like(DESCRIPTION, "TE", MatchMode.BEGIN)))), is(6L));

		assertThat(
				count(where().addClause(
						n(like(DESCRIPTION, "TO", MatchMode.END)))), is(3L));

		assertThat(
				count(where().addClause(
						n(like(DESCRIPTION, "TOTE", MatchMode.EQUAL)))), is(6L));

	}

	/**
	 * Pruebas con un nivel de join *
	 * <p>
	 * {@link TestEntity} -> {@link TestChild}
	 * </p>
	 */
	@Test
	public void child() {

		assertThat(
				count(where().addClause(
						n(like(c(DESCRIPTION), "TE", MatchMode.CONTAIN)))),
				is(4L));

		assertThat(
				count(where().addClause(
						n(like(c(DESCRIPTION), "TE", MatchMode.BEGIN)))),
				is(6L));

		assertThat(
				count(where().addClause(
						n(like(c(DESCRIPTION), "TO", MatchMode.END)))), is(6L));

		assertThat(
				count(where()
						.addClause(
								n(like(c(DESCRIPTION), "COSTO_CHILD",
										MatchMode.EQUAL)))), is(6L));

	}

	/**
	 * Pruebas con dos niveles de join
	 * <p>
	 * {@link TestEntity} -> {@link TestChild} -> {@link TestGrandChild}
	 * </p>
	 */
	@Test
	public void grandChild() {

		assertThat(
				count(where().addClause(
						n(like(g(DESCRIPTION), "1", MatchMode.CONTAIN)))),
				is(3L));

		assertThat(
				count(where().addClause(
						n(like(g(DESCRIPTION), "1", MatchMode.BEGIN)))), is(3L));

		assertThat(
				count(where().addClause(
						n(like(g(DESCRIPTION), "XX", MatchMode.END)))), is(3L));

		assertThat(
				count(where().addClause(
						n(like(g(DESCRIPTION), "COSTO_CHILD_CHILD",
								MatchMode.EQUAL)))), is(4L));

	}

	Clause n(Clause s) {

		return Clauses.not(s);
	}

	Clause like(String path, String exp, MatchMode mm) {

		return Clauses.iLike(path, exp, mm);
	}
}
