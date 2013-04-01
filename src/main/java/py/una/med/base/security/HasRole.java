/*
 * @HasRole.java 1.0 Mar 14, 2013 Sistema Integral de Gestión Hospitalaria
 */
package py.una.med.base.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Arturo Volpe Volpe
 * @since 1.0
 * @version 1.0 Mar 14, 2013
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRole {

	String value();
}
