/*
 * @DummyFirstChangeProvider.java 1.0 Dec 4, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.server;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import py.una.med.base.replication.Shareable;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 4, 2013
 * 
 */
public class DummyFirstChangeProvider implements FirstChangeProvider<Shareable> {

	@Autowired
	SessionFactory factory;

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
