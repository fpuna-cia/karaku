/*
 * @ILikeTest.java 1.0 Sep 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.dao.clauses;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.dao.where.MatchMode;
import py.una.med.base.dao.where.NumberLike;
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.test.util.layers.TestChild;
import py.una.med.base.test.test.util.layers.TestEntity;
import py.una.med.base.test.test.util.layers.TestGrandChild;

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
	public void main() {

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
	public void child() {

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
	public void grandChild() {

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
