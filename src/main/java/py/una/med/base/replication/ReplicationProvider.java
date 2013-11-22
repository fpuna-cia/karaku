/*
 * @ReplicationProvider.java 1.0 Nov 7, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication;

/**
 * Generador de deltas de bases de datos.
 * 
 * <p>
 * Se encarga de proveer la diferencia entre dos estados de la base de datos,
 * relacioandos a una simple tabla y sin tener en cuenta datos estructurales, es
 * decir, es un complemento a liquibase para cambios que ocurren durante la
 * ejecución del sistema.
 * </p>
 * 
 * <p>
 * No es necesario que varios {@link ReplicationProvider} existan al mismo
 * tiempo, y la inter-operabilidad entre los mismos no es requerida (si
 * deseable), en el peor de los casos un {@link ReplicationProvider} debería
 * retornar todas las entidades si encuentra un ID que no conoce (los
 * identificadores por defecto están {@link Bundle})
 * </p>
 * 
 * <p>
 * Un {@link ReplicationProvider} no se debe encargar de temas relacionados como
 * la conversión, ni compresión ni detalles de transporte, su función se limita
 * a recuperar información de la base da datos.
 * </p>
 * <p>
 * Idealmente cada cambio debería tener un ID único, así un cliente que no puede
 * ejecutar todo un {@link Bundle} completo (pero sí una parte), puede
 * continuar.
 * </p>
 * 
 * <p>
 * Las actuales implementaciónes de {@link AbstractReplicationEndpoint}, -único
 * cliente de esta interfaz actualmente- por el momento no utilizan los id's de
 * cada {@link Change}, sino más bien utilizan solo el último, proveído por
 * {@link Bundle#getLastId()}
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 7, 2013
 * 
 */
public interface ReplicationProvider {

	/**
	 * Retorna una lista de cambios.
	 * 
	 * <p>
	 * Utiliza el <i>lastId</id> para recuperar los registros que fueron
	 * modificados desde ese identificador y los retorna en forma de un
	 * {@link Bundle}
	 * </p>
	 * 
	 * @param clazz
	 *            entidad que se busca replicar
	 * @param lastId
	 *            ultimo id replicado
	 * @return {@link Bundle} con cambios, como máximo vacío y con
	 *         {@link Bundle#getLastId()}
	 */
	<T extends Shareable> Bundle<T> getChanges(Class<T> clazz, String lastId);

}
