/*
 * @ReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication;


/**
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 *
 */
public interface ReplicationProvider {

	<T extends Shareable> Bundle<T> getChanges(Class<T> clazz, String lastId);

}
