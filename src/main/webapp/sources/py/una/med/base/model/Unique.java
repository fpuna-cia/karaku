/*
 * Unique.java 1.0 19/02/2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacion que sirve para marcar metodos que son unicos en la base de datos,
 * se utiliza cuando se necesita asociar una restriccion del tipo Unique en la
 * base de datos a un determinado campo (o conjunto de campos).<br>
 * 
 * 
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 19, 2013
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {

	/**
	 * Determina el nombre de la restriccion a la cual esta atado el field
	 * 
	 * @return nombre del constraint
	 */
	String[] value();

}
