/*
 * @ReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.server;

import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.replication.Shareable;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
@Transactional
public class EnversReplicationProvider implements ReplicationProvider {

	@Autowired
	private SessionFactory factory;

	@Override
	@Transactional(readOnly = true)
	public <T extends Shareable> Bundle<T> getChanges(Class<T> clazz,
			String lastId) {

		// SI la sincronización no existe se retorna todo.
		if (Bundle.ZERO_ID.equals(lastId)) {
			return getAll(clazz);
		}
		throw new NotImplementedException();
	}

	@SuppressWarnings("unchecked")
	private <T extends Shareable> Bundle<T> getAll(Class<T> clazz) {

		List<T> entities = getSession().createCriteria(clazz).list();
		Bundle<T> toRet = new Bundle<T>();
		for (T entitie : entities) {
			toRet.add(entitie, Bundle.ZERO_ID);
		}
		return toRet;

	}

	protected Session getSession() {

		return factory.getCurrentSession();
	}

}
