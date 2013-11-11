/*
 * @AbstractReplicationEndPoint.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication;

import java.lang.reflect.ParameterizedType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 *
 */
public class AbstractReplicationEndPoint<T extends Shareable> {

	Class<T> clazz;

	@Autowired
	ReplicationProvider replicationProvider;

	@SuppressWarnings("unchecked")
	public Class<T> getClassOfT() {

		if (this.clazz == null) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			this.clazz = (Class<T>) type.getActualTypeArguments()[0];
		}

		return clazz;
	}

	public Bundle<T> getChanges(String lastId) {

		return replicationProvider.getChanges(getClassOfT(), lastId);
	}

}
