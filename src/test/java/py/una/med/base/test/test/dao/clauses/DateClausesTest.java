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
import py.una.med.base.test.test.util.layers.ITestDAO;
import py.una.med.base.test.util.transaction.SQLFiles;

/**
 * Clases de prueba para la {@link Clause} {@link ILike}
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * 
 */
@SQLFiles
public class DateClausesTest extends BaseClauseTest {

	/**
	 * 
	 */
	private static final String FECHA = "fecha";
	private static final String CFECHA = "testChild.fecha";
	private static final String GCFECHA = "testChild.grandChilds.fecha";
	@Autowired
	ITestDAO dao;

	@Autowired
	DateClauses dc;

	@Test
	public void testDateIn() {

		AssertBuilder datesInclusive = new AssertBuilder() {

			@Override
			Clause getClause(Date one, Date two) {

				return dc.between(FECHA, one, two, true);
			}
		};
		datesInclusive.whenDatesAre(date(13), date(13)).assertCountIs(1L);
		datesInclusive.whenDatesAre(date(13), date(19)).assertCountIs(7L);

	}

	@Test
	public void testDateEx() {

		AssertBuilder datesExclusive = new AssertBuilder() {

			@Override
			Clause getClause(Date one, Date two) {

				return dc.between(FECHA, one, two, false);
			}
		};
		datesExclusive.whenDatesAre(date(13), date(13)).assertCountIs(0L);
		datesExclusive.whenDatesAre(date(13), date(19)).assertCountIs(5L);

	}

	@Test
	public void testTimeInclusive() {

		AssertBuilder inclusive = new AssertBuilder() {

			@Override
			Clause getClause(Date one, Date two) {

				return dc.betweenTime(CFECHA, one, two, true);
			}
		};

		inclusive.whenDatesAre(time(8, 25), time(8, 35)).assertCountIs(1L);
		inclusive.whenDatesAre(time(8, 30), time(8, 30)).assertCountIs(1L);
		inclusive.whenDatesAre(time(18, 30), time(22, 59)).assertCountIs(2L);
		inclusive.whenDatesAre(time(8, 00), time(23, 59)).assertCountIs(9L);

	}

	@Test
	public void testTimeExclusive() {

		AssertBuilder exclusive = new AssertBuilder() {

			@Override
			Clause getClause(Date one, Date two) {

				return dc.betweenTime(CFECHA, one, two, false);
			}
		};
		exclusive.whenDatesAre(time(8, 25), time(8, 35)).assertCountIs(1L);
		exclusive.whenDatesAre(time(8, 30), time(8, 30)).assertCountIs(0L);
		exclusive.whenDatesAre(time(8, 00), time(23, 59)).assertCountIs(7L);
		exclusive.whenDatesAre(time(23, 00), time(23, 05)).assertCountIs(0L);

	}

	@Test
	public void testDateTimeExclusive() {

		AssertBuilder exclusive = new AssertBuilder() {

			@Override
			Clause getClause(Date one, Date two) {

				return dc.betweenDateTime(GCFECHA, one, two, false);
			}
		};

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(11, 8, 30))
				.assertCountIs(0L);

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(13, 9, 30))
				.assertCountIs(2L);

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(10, 8, 00))
				.assertCountIs(0L);

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(18, 23, 59))
				.assertCountIs(7L);
	}
	
	@Test
	public void testDateTimeInclusive() {

		AssertBuilder exclusive = new AssertBuilder() {

			@Override
			Clause getClause(Date one, Date two) {

				return dc.betweenDateTime(GCFECHA, one, two, true);
			}
		};

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(11, 8, 30))
				.assertCountIs(2L);

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(13, 9, 30))
				.assertCountIs(4L);

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(10, 8, 00))
				.assertCountIs(1L);

		exclusive.whenDatesAre(dateTime(10, 8, 00), dateTime(18, 23, 59))
				.assertCountIs(9L);
	}


	private Date date(int day) {

		return newTime(day, Calendar.DECEMBER, 2013, 0, 0);
	}

	private Date time(int hour, int minute) {

		return newTime(1, Calendar.JANUARY, 1970, hour, minute);
	}

	private Date dateTime(int day, int hour, int minute) {

		return newTime(day, Calendar.DECEMBER, 2013, hour, minute);
	}

	private Date newTime(int day, int month, int year, int hours, int minutes) {

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.HOUR_OF_DAY, hours);
		c.set(Calendar.MINUTE, minutes);

		return c.getTime();
	}

	abstract class AssertBuilder {

		private Long count;

		abstract Clause getClause(Date one, Date two);

		AssertBuilder whenDatesAre(Date one, Date two) {

			// esto puede mejorar y ser lazy.
			count = dao.getCount(where().addClause(getClause(one, two)));
			return this;
		}

		void assertCountIs(Long expected) {

			assertThat(count, is(expected));

		}
	}
}
