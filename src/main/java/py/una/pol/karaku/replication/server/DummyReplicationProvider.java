/*
 * @DummyReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.replication.server;

import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import py.una.pol.karaku.replication.Shareable;

/**
 * {@link ReplicationProvider} que simplemente envía toda la tabla cada vez que
 * se solicita un cambio.
 * 
 * <p>
 * Solo funciona cuando el id del cambio es {@link Bundle#ZERO_ID}.
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
public class DummyReplicationProvider implements ReplicationProvider {

	@Autowired
	private FirstChangeProviderHandler firstChangeProviderHandler;

	@Override
	@Nonnull
	@Transactional(readOnly = true)
	public <T extends Shareable> Bundle<T> getChanges(@Nonnull Class<T> clazz,
			String lastId) {

		return firstChangeProviderHandler.getAll(clazz);
	}
}
