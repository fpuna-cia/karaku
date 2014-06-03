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
import py.una.med.base.test.test.util.layers.ITestDAO;

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
