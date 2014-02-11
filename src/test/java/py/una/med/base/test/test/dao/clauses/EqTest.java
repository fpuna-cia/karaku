/*
 * @ILikeTest.java 1.0 Sep 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.dao.clauses;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.math.BigDecimal;
import javax.annotation.Nonnull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.Clauses;
import py.una.med.base.dao.where.ILike;
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.test.util.layers.TestEntity;
import py.una.med.base.test.util.transaction.SQLFiles;

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
