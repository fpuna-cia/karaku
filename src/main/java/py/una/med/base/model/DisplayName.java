package py.una.med.base.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacion que define la internacionalizacion para un field, con el
 * {@link DisplayName#path()} se setea la ubicacion de donde se sacara el valor
 * para ser mostrado
 * 
 * @author Arturo Volpe
 * @since 1.2
 * @version 1.0
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayName {

	/**
	 * Clave por la cual se buscara en el archivo de idiomas en el formato
	 * {CADENA}
	 * 
	 * @return Llave del archivo de idiomas
	 */
	String key();

	/**
	 * Expresion EL para ubicar el valor de la referencia, ejemplo: desde la
	 * entidad PersonaFisica, si queremos agregar un enlace al nombre de la
	 * nacionalidad ponemos: "nacionalidad.descripcion"
	 * 
	 * @return EL expression
	 */
	String path() default "";

}