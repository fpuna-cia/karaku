/*
 * @Audit.java 1.0 18/02/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación que permite marcar un método para ser auditado automáticamente
 * mediante AOP.
 * 
 * @author Arturo Volpe
 * @author Romina Fernandez
 * @since 1.0
 * @version 1.1
 * @see SIGHAudit encargado de interceptar el método
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

	/**
	 * Representa todo aquello que formando parte del bean actual, será
	 * auditado. Como son expresiones de EL, no pueden ser métodos ni atributos
	 * privados.
	 * <p>
	 * 
	 * </p>
	 * 
	 * @return Vector de valores de la clase que serán auditadas, ejemplo {
	 *         "filterOption", "bean.pais" }
	 */
	String[] toAudit() default {};

	/**
	 * Representa todo aquello que forma parte de un parámetro y desea ser
	 * auditado, los parámetros se enumeran de 1 a N.
	 * 
	 * 
	 * @return Vector de valores que pertenecen al parámetro, ejemplo {
	 *         "{1}.id", "{2}" }
	 */
	String[] paramsToAudit() default {};

}
