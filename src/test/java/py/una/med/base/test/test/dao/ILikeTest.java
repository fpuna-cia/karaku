/*
 * @ILikeTest.java 1.0 Sep 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.dao.where.ILike;
import py.una.med.base.dao.where.MatchMode;
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.test.util.layers.TestChild;
import py.una.med.base.test.test.util.layers.TestEntity;
import py.una.med.base.test.test.util.layers.TestGrandChild;

/**
 * Clases de prueba para la {@link Clause} {@link ILike}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
public class ILikeTest extends BaseClauseTest {

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
						Clauses.iLike(DESCRIPTION, "TE", MatchMode.CONTAIN))),
				is(3L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(DESCRIPTION, "TE", MatchMode.BEGIN))),
				is(1L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(DESCRIPTION, "TO", MatchMode.END))),
				is(4L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(DESCRIPTION, "TOTE", MatchMode.EQUAL))),
				is(1L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(DESCRIPTION, "TO", MatchMode.BEGIN),
						Clauses.iLike(DESCRIPTION, "TE", MatchMode.END))),
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
				count(where().addClause(
						Clauses.iLike(c(DESCRIPTION), "TE", MatchMode.CONTAIN))),
				is(4L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(c(DESCRIPTION), "TE", MatchMode.BEGIN))),
				is(1L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(c(DESCRIPTION), "TO", MatchMode.END))),
				is(2L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(c(DESCRIPTION), "COSTO_CHILD",
								MatchMode.EQUAL))), is(2L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(c(DESCRIPTION), "TO", MatchMode.BEGIN),
						Clauses.iLike(c(DESCRIPTION), "TE", MatchMode.END))),
				is(2L));
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
						Clauses.iLike(g(DESCRIPTION), "1", MatchMode.CONTAIN))),
				is(2L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(g(DESCRIPTION), "1", MatchMode.BEGIN))),
				is(1L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(g(DESCRIPTION), "XX", MatchMode.END))),
				is(3L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(g(DESCRIPTION), "COSTO_CHILD_CHILD",
								MatchMode.EQUAL))), is(2L));

		assertThat(
				count(where().addClause(
						Clauses.iLike(g(DESCRIPTION), "1.", MatchMode.BEGIN),
						Clauses.iLike(g(DESCRIPTION), "X2", MatchMode.END))),
				is(1L));
	}
}
