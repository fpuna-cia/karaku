/*
 * @FirstChangeProvider.java 1.0 Dec 4, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.replication.server;

import java.util.Collection;
import py.una.med.base.replication.Shareable;

/**
 * Define un proveedor de cambios para la primera vez que se sincronizan.
 * 
 * 
 * <p>
 * El problema de la primera existe cuando una entidad necesita ser insertada en
 * un orden, los problemas que llevan a esto pueden ser:
 * <ol>
 * <li>Un elemento necesita que otro del mismo tipo este presente, esto ocurre
 * cuando se utilizan árboles (una tabla se referencia a sí misma).</li>
 * <li>Existe un orden por el cual se deban cargar y no existe otro mecanismo de
 * definir el mismo.</li>
 * </ol>
 * 
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Dec 4, 2013
 * 
 */
public interface FirstChangeProvider<T extends Shareable> {

	static final Integer MAX_PRIORITY = Integer.MAX_VALUE;

	Collection<? extends T> getChanges(Class<? extends T> clazz);

	Class<T> getSupportedClass();

	Integer getPriority();

}
