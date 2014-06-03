/*
 * @CacheAll.java 1.0 Feb 21, 2014 Sistema Integral de Gestion Hospitalaria
 */
package py.una.pol.karaku.replication.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import py.una.pol.karaku.replication.Shareable;

/**
 * Todas las entidades de este tipo serán precargadas.
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Feb 21, 2014
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheAll {

	/**
	 * Entidad a ser recargada.
	 * 
	 * <p>
	 * Su valor por defecto es <code>null</code>, y significa que será cargada
	 * la entidad definida por {@link Converter#getEntityType()}
	 * </p>
	 * 
	 * @return clase de la entidad, o <code>null</code> si se desea cargar la
	 *         entidad definida en el converter.
	 */
	Class<? extends Shareable>[] value() default Shareable.class;
}
