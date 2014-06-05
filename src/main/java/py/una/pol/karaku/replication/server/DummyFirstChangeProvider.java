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
package py.una.pol.karaku.replication.server;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import py.una.pol.karaku.replication.Shareable;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 4, 2013
 * 
 */
@Component
public class DummyFirstChangeProvider implements FirstChangeProvider<Shareable> {

	@Autowired
	private SessionFactory factory;

	@SuppressWarnings("unchecked")
	private <T extends Shareable> List<T> getAll(Class<T> clazz) {

		List<T> entities = getSession().createCriteria(clazz)
				.addOrder(Order.asc("id")).list();
		List<T> toRet = new ArrayList<T>(entities.size());
		for (T entitie : entities) {
			toRet.add(entitie);
		}
		return toRet;

	}

	protected Session getSession() {

		return factory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Shareable> getChanges(Class<? extends Shareable> clazz) {

		return (List<Shareable>) getAll(clazz);
	}

	@Override
	public Class<Shareable> getSupportedClass() {

		return Shareable.class;
	}

	@Override
	public Integer getPriority() {

		return Integer.MIN_VALUE;
	}

}
