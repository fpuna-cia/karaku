/*
 * @ReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
	SessionFactory factory;

	@Override
	@Transactional(readOnly = true)
	public <T extends Shareable> Bundle<T> getChanges(Class<T> clazz,
			String lastId) {

		// SI la sincronización no existe se retorna todo.
		if (Bundle.ZERO_ID.equals(lastId)) {
			return getAll(clazz);
		}
		return getById(clazz, Long.valueOf(lastId));
	}

	private <T extends Shareable> Bundle<T> getById(Class<T> clazz,
			Number number) {

		// AuditReader reader = getReader();
		// AuditQuery query = reader.createQuery().forRevisionsOfEntity(clazz,
		// false, true);

		return null;
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
	// private AuditReader getReader() {
	//
	// return AuditReaderFactory.get(factory.getCurrentSession());
	// }

}
