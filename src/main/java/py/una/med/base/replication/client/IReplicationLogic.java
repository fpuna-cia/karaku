/*
 * @IReplicationLogic.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import java.util.Set;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 *
 */
public interface IReplicationLogic {

	public abstract Set<ReplicationInfo> getActiveReplications();

	public abstract ReplicationInfo getByClass(Class<?> clazz);

	/**
	 * @return
	 */
	public abstract Set<ReplicationInfo> getReplicationsToDo();

	/**
	 * @param clazz
	 */
	public abstract void notifyReplication(Class<?> clazz, String id);

	/**
	 * @param class1
	 * @param i
	 */
	public abstract ReplicationInfo updateSyncTime(Class<?> class1, int i);

}
