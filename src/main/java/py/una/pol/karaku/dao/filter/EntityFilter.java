package py.una.pol.karaku.dao.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface que automatiza el uso de filtros hibernate
 * 
 * 
 * @author Arturo Volpe
 * @see FilterAttribute
 * @see SIGHEntityFilterHandler
 * @since 1.0
 * @version 1.0
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityFilter {

	/**
	 * Nombre del filter que debe estar en la entidad, o definido en algun
	 * {@link FilterDef}
	 * 
	 */
	String filter();

	/**
	 * Vector de Atributos de filtro,
	 * 
	 * @see FilterAttribute
	 */
	FilterAttribute[] params() default {};
}
