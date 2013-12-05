/*
 * @DummyReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.server;

import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import py.una.med.base.replication.Shareable;

/**
 * {@link ReplicationProvider} que simplemente envía toda la tabla cada vez que
 * se solicita un cambio.
 * 
 * <p>
 * Solo funciona cuando el id del cambio es {@link Bundle#ZERO_ID}, en caso
 * contrario lanza una {@link NotImplementedException}
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
@Component
@Transactional
public class DummyReplicationProvider implements ReplicationProvider {

	@Autowired
	private FirstChangeProviderHandler firstChangeProviderHandler;

	@Override
	@Transactional(readOnly = true)
	public <T extends Shareable> Bundle<T> getChanges(Class<T> clazz,
			String lastId) {

		if (Bundle.ZERO_ID.equals(lastId)) {
			return firstChangeProviderHandler.getAll(clazz);
		}
		throw new NotImplementedException(
				"Dummy Replication provider dont support a ID != 'ZERO'");
	}
}
