/*
 * @ReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.replication.server;

import javax.annotation.Nonnull;
import py.una.pol.karaku.replication.Shareable;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
public interface ReplicationProvider {

	@Nonnull
	<T extends Shareable> Bundle<T> getChanges(@Nonnull Class<T> clazz,
			@Nonnull String lastId);

}
