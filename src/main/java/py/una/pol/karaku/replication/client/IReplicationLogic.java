/*
 * @IReplicationLogic.java 1.0 Nov 25, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.client;

import java.util.Set;
import javax.annotation.Nonnull;

/**
 * Interfaz que define las operaciones lógicas de replicación.
 * 
 * <p>
 * Esta interfaz define todo lo necesario para que se puedan realizar
 * replicaciónes, si bien no hace el trabajo por si misma, es la encargada de
 * determinar que, cuando y cada tanto una entidad debe ser replicada.
 * </p>
 * <p>
 * Se considera que una entidad debe ser replicada cuando:
 * <ol>
 * <li>Su {@link ReplicationInfo} esta activo</li>
 * <li>El tiempo de espera para la siguiente replicación ha cauducado</li>
 * </ol>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 25, 2013
 * 
 */
public interface IReplicationLogic {

	/**
	 * Retorna la lista de replicaciones actualmente activas.
	 * 
	 * <p>
	 * Si se desea una lista de todas las replicaciones ver
	 * {@link IReplicationInfoDao}
	 * </p>
	 * 
	 * @return set de replicaciones, o un set vacío si no se encuentra ningúna.
	 */
	Set<ReplicationInfo> getActiveReplications();

	/**
	 * Información de replicación dada una entidad.
	 * 
	 * <p>
	 * Retorna la información sobre una entidad pedida, cauduco o no el tiempo
	 * requerido de espera entre intentos de replicación.
	 * </p>
	 * <p>
	 * Esta búsqueda es por entidad, no por DTO, ni por Respuesta o Petición
	 * (Response/Request)
	 * </p>
	 * 
	 * 
	 * @param clazz
	 *            clase de la entidad a buscar .
	 * @return si no se encuentra la clase entre la lista de entidades a
	 *         replicar, retorna <code>null</code>, si se encuentra una entidad
	 *         retorna la misma, y si se encuentran más de una debe emitir una
	 *         alerta y retornar el primer objeto retornado.
	 */
	ReplicationInfo getByClass(@Nonnull Class<?> clazz);

	/**
	 * Retorna la lista de entidades que se deben replicar.
	 * 
	 * <p>
	 * Solamente las entidades que deben ser replicadas en un momento dado son
	 * retornadas por este método.
	 * <p>
	 * 
	 * @return {@link Set} de replicaciones, un {@link Set} vacío si no hay nada
	 *         que replicar. Nunca <code>null</code>.
	 */
	Set<ReplicationInfo> getReplicationsToDo();

	/**
	 * Notifica que una clase ha sido replicada satisfactoriamente.
	 * 
	 * <p>
	 * De esta forma, una entidad es extraida de la lista de entidades a
	 * replicar. Solamente debe ser invocado si la replicación fue exitosa, de
	 * caso contrario, no se reintentara hasta que se venza el plazo de espera.
	 * </p>
	 * 
	 * @param clazz
	 *            clase a replicar
	 * @param id
	 *            identificador de la ultima replicación
	 */
	void notifyReplication(@Nonnull Class<?> clazz, String id);

	/**
	 * Actualiza el tiempo entre actualizaciones de una entidad.
	 * <p>
	 * Notar que es la entidad la que se usa como identificaor.
	 * </p>
	 * 
	 * @param entity
	 *            entidad a actualizar el tiempo.
	 * @param interval
	 *            nuevo intervalo de tiempo.
	 */
	ReplicationInfo updateSyncTime(@Nonnull Class<?> entity, int interval);

	/**
	 * Actualiza una {@link ReplicationInfo} y la deja lista para el proceso de
	 * sincronización.
	 * 
	 * <p>
	 * En este proceso se actualizan las clases apuntadas por al info, como
	 * {@link ReplicationInfo#getEntityClazz()}.
	 * 
	 * @param info
	 *            información no nula a actualizar.
	 */
	void configureInfo(ReplicationInfo info);

}
