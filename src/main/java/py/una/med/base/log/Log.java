/*
 * @Log.java 1.0 Sep 13, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.Logger;

/**
 * 
 * Anotación que sirve para indicar que se debe inyectar un
 * {@link org.slf4j.Logger}.
 * <p>
 * La utilización debe ser como sigue:
 * 
 * <pre>
 * {@literal @}{@link Log}
 * Logger log;
 * </pre>
 * 
 * De esta forma, el {@link LogPostProcessor} se encargará automáticamente de
 * inyectar el {@link org.slf4j.Logger} (a través del
 * {@link org.slf4j.LoggerFactory}) pertinente al atributo.
 * </p>
 * <p>
 * Esto produce el mismo resultado que:
 * 
 * <pre>
 * Logger log = LoggerFactory.getLogger("py.una.clase")
 * </pre>
 * 
 * </p>
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 Sep 13, 2013
 * @see LogPostProcessor
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface Log {

	/**
	 * Nombre del log, si es <code>null</code>, entonces se utilizará un
	 * {@link Logger} con el nombre del bean.
	 *
	 * @return cadena que representa el nombre del {@link Logger},
	 *         <code>""</code> es interpretado como <code>null</code>.
	 */
	String name() default "";
}
