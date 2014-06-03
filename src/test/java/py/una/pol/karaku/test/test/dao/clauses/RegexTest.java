/*
 * @RegexTest.java 1.0 21/01/2014 Sistema Integral de Gestion Hospitalaria
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
