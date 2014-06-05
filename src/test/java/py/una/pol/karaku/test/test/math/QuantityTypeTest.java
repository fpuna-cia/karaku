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
package py.una.pol.karaku.test.test.math;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import py.una.pol.karaku.dao.restrictions.Where;
import py.una.pol.karaku.dao.search.SearchParam;
import py.una.pol.karaku.dao.where.Clause;
import py.una.pol.karaku.dao.where.Clauses;
import py.una.pol.karaku.math.Quantity;
import py.una.pol.karaku.test.base.BaseTestWithDatabase;
import py.una.pol.karaku.test.configuration.TransactionTestConfiguration;
import py.una.pol.karaku.test.test.math.layers.EntityWithQuantity;
import py.una.pol.karaku.test.test.math.layers.EntityWithQuantityDAO;
import py.una.pol.karaku.test.test.math.layers.IEntityWithQuantityDAO;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 26, 2013
 * 
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class QuantityTypeTest extends BaseTestWithDatabase {

	@Configuration
	@EnableTransactionManagement()
	static class ContextConfiguration extends TransactionTestConfiguration {

		public java.lang.Class<?>[] getEntityClasses() {

			return new Class<?>[] { EntityWithQuantity.class };
		};

		@Bean
		IEntityWithQuantityDAO dao() {

			return new EntityWithQuantityDAO();
		}
	}

	@Autowired
	IEntityWithQuantityDAO dao;

	@Test
	public void testInjection() {

		assertNotNull(dao);
	}

	@Test
	public void testReadQ1() {

		SearchParam sp = new SearchParam();
		sp.addOrder("id");
		List<EntityWithQuantity> entities = dao.getAll(sp);
		assertEquals(new Quantity("100"), entities.get(0).getQ1());
		assertEquals(new Quantity("150"), entities.get(1).getQ1());
		assertEquals(new Quantity("200"), entities.get(2).getQ1());
		assertEquals(new Quantity("250"), entities.get(3).getQ1());
		assertEquals(new Quantity("300"), entities.get(4).getQ1());
		assertEquals(new Quantity("350"), entities.get(5).getQ1());
		assertEquals(new Quantity("4000000000000000000"), entities.get(6).getQ1());
	}

	@Test
	public void testReadQ2() {

		SearchParam sp = new SearchParam();
		sp.addOrder("id");
		List<EntityWithQuantity> entities = dao.getAll(sp);
		assertEquals(new Quantity("0.0020"), entities.get(0).getQ2());
		assertEquals(new Quantity("0.0040"), entities.get(1).getQ2());
		assertEquals(new Quantity("0.0060"), entities.get(2).getQ2());
		assertEquals(new Quantity("0.0001"), entities.get(3).getQ2());
		assertEquals(new Quantity("0.9547"), entities.get(4).getQ2());
		assertEquals(new Quantity("0.1235"), entities.get(5).getQ2());
		assertEquals(new Quantity("0.1235"), entities.get(6).getQ2());
	}

	@Test
	public void testAdd() {

		EntityWithQuantity ewq = new EntityWithQuantity();
		ewq.setQ1(new Quantity(666));
		ewq.setQ2(new Quantity("0.0003"));
		EntityWithQuantity ewq2 = dao.add(ewq);

		assertEquals(ewq, ewq2);
		assertEquals(new Quantity("666"), ewq2.getQ1());
		assertEquals(new Quantity("0.0003"), ewq2.getQ2());

		EntityWithQuantity ewq3 = new EntityWithQuantity();
		ewq3.setQ1(new Quantity("100000000000000"));
		ewq3.setQ2(new Quantity("0.00003"));
		ewq3 = dao.add(ewq3);

		assertEquals(new Quantity("100000000000000"), ewq3.getQ1());
		assertEquals(new Quantity("0"), ewq3.getQ2());
	}

	@Test
	public void testDelete() {

		dao.remove(1L);
		assertNull(dao.getById(1L));
	}

	@Test
	public void testUpdate() {

		EntityWithQuantity ewq = dao.getById(1L);
		ewq.setQ1(new Quantity("100.58978"));
		ewq = dao.update(ewq);

		EntityWithQuantity ewq2 = dao.getById(1L);
		assertEquals(new Quantity("100.5898"), ewq2.getQ1());
	}

	@Test
	public void testSearch() {

		assertThat(count(Clauses.eq("q1", new Quantity("100"))), is(1L));

		assertThat(count(Clauses.ge("q1", new Quantity("350"))), is(2L));

		assertThat(count(Clauses.between("q1", new Quantity("151"),
				new Quantity("201"))), is(1L));
	}

	private Long count(Clause c) {

		return dao.getCount(where(c));
	}

	private Where<EntityWithQuantity> where(Clause c) {

		Where<EntityWithQuantity> where = new Where<EntityWithQuantity>();
		where.addClause(c);
		return where;
	}

}
