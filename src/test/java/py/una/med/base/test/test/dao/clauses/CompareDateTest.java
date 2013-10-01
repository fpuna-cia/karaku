/*
 * @ILikeTest.java 1.0 Sep 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.test.dao.clauses;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.dao.where.Clause;
import py.una.med.base.dao.where.DateClauses;
import py.una.med.base.dao.where.ILike;
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.test.util.layers.TestEntity;

/**
 * Clases de prueba para la {@link Clause} {@link ILike}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
public class CompareDateTest extends BaseClauseTest {

	/**
	 * 
	 */
	private static final String FECHA = "fecha";
	@Autowired
	ITestDAO dao;

	@Autowired
	DateClauses dc;

	/**
	 * Pruebas con atributos directos
	 * <p>
	 * {@link TestEntity}
	 * </p>
	 */
	@Test
	public void main() {

		assertThat(
				this.dao.getCount(this.where().addClause(
						this.in(this.d(13), this.d(13)))), is(1L));
		assertThat(
				this.dao.getCount(this.where().addClause(
						this.ex(this.d(13), this.d(13)))), is(0L));

		assertThat(
				this.dao.getCount(this.where().addClause(
						this.in(this.d(13), this.d(19)))), is(7L));
		assertThat(
				this.dao.getCount(this.where().addClause(
						this.ex(this.d(13), this.d(19)))), is(5L));
	}

	private Clause in(Date one, Date two) {

		return this.dc.between(FECHA, one, two, true);

	}

	private Clause ex(Date one, Date two) {

		return this.dc.between(FECHA, one, two, false);

	}

	private Date d(int day) {

		return this.d(day, Calendar.DECEMBER, 2013);
	}

	private Date d(int day, int month, int year) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.YEAR, year);

		return c.getTime();
	}
}
