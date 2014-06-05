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
import java.math.BigDecimal;
import javax.annotation.Nonnull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.pol.karaku.dao.where.Clause;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.dao.where.ILike;
import py.una.pol.karaku.test.test.util.layers.ITestDAO;
import py.una.pol.karaku.test.test.util.layers.TestEntity;
import py.una.pol.karaku.test.util.transaction.SQLFiles;

/**
 * Clases de prueba para la {@link Clause} {@link ILike}
 * <p>
 * Este es un test de integraciÃ³n, que depende de que {@link ILikeTest}
 * funcione correctamente.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
@SQLFiles
public class EqTest extends BaseClauseTest {

	@Nonnull
	private static final String BD = "bigDecimal";
	@Autowired
	ITestDAO dao;

	/**
	 * Pruebas con atributos directos
	 * <p>
	 * {@link TestEntity}
	 * </p>
	 */
	@Test
	public void testOneLevel() {

		assertThat(count(where().addClause(eq(BD, null))), is(4L));

		assertThat(count(where().addClause(eq(BD, n(2)))), is(1L));

		assertThat(count(where().addClause(eq(BD, n(1)))), is(2L));

	}

	Clause eq(@Nonnull String path, Object exp) {

		return Clauses.eq(path, exp);
	}

	BigDecimal n(int number) {

		return new BigDecimal(number);
	}
}
