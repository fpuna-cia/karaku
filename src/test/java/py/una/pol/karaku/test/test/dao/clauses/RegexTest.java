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
import py.una.pol.karaku.dao.where.Regex;
import py.una.pol.karaku.test.test.util.layers.ITestDAO;
import py.una.pol.karaku.test.test.util.layers.TestEntity;
import py.una.pol.karaku.test.util.transaction.SQLFiles;

/**
 * 
 * @author Arsenio Ferreira
 * @since 1.0
 * @version 1.0 21/01/2014
 * 
 */
@SQLFiles
public class RegexTest extends BaseClauseTest {

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
	public void testProperty() {

		assertThat(count(where().addClause(reg(DESCRIPTION, "^TOTE$"))), is(1L));
		assertThat(count(where().addClause(reg(DESCRIPTION, "^.*TE$"))), is(2L));
		assertThat(
				count(where().addClause(
						reg(DESCRIPTION, "^([0-9]+\\.?[0-9]*|\\.[0-9]+)$"))),
				is(1L));

	}

	/**
	 * Pruebas con atributos directos
	 * <p>
	 * {@link TestEntity}
	 * </p>
	 */
	@Test
	public void testNestedProperty() {

		assertThat(count(where().addClause(reg(c(DESCRIPTION), "^XXX$"))),
				is(1L));
		assertThat(count(where().addClause(reg(c(DESCRIPTION), "^.*CHILD$"))),
				is(3L));
		assertThat(
				count(where().addClause(
						reg(c(DESCRIPTION), "^([0-9]+\\.?[0-9]*|\\.[0-9]+)$"))),
				is(2L));

	}

	Clause reg(String path, String exp) {

		return new Regex(path, exp);
	}
}
