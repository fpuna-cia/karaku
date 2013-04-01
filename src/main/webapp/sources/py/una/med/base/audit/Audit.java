/*
 * @CaseSensitive.java 1.0 18/02/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacion que permite marcar un metodo para ser auditado automaticamente
 * mediante AOP.
 * 
 * @author Arturo Volpe, Romina Fernandez
 * @since 1.0
 * @version 1.1
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

	/**
	 * Representa todo aquello qu formando parte del bean actual, sera auditado.
	 * Como son expresiones de EL, no pueden ser metdoos ni atributos privados.
	 * 
	 * @return Vector de valores de la clase que seran auditadas, ejemplo {
	 *         "filterOption", "bean.pais" }
	 */
	String[] toAudit() default {};

	/**
	 * Representa todo aquello que forma parte de un parametro y desea ser
	 * auditado, los parametros se enumeran de 1 a N.
	 * 
	 * 
	 * @return Vector de valores que pertenecen al parametro, ejemplo {
	 *         "{1}.id", "{2}" }
	 */
	String[] paramsToAudit() default {};

}
