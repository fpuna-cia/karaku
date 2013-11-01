/*
 * @Sequences.java 1.0 Nov 1, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.util.transaction;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define un conjunto de secuencias que deben ser ejecutadas antes de la clase.
 * 
 * 
 * <p>
 * Las secuencias son generadas antes, y se eliminan después de cada clase, se
 * garantiza que sean ejecutadas antes de los scripts definidos con
 * {@link SQLFiles}
 * </p>
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 1, 2013
 * 
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sequences {

	/**
	 * Nombre de las secuencias que deben ser ejecutadas antes de cada test.
	 * 
	 * 
	 * @return lista posiblemente nula o vacía de cadenas que representan los
	 *         nombres de las secuencias.
	 */
	String[] value();
}
