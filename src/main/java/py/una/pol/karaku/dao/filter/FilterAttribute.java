/*
 * 
 */
package py.una.pol.karaku.dao.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Representa un atributo dado del filtro, por ejemplo el filter
 * 
 * <pre>
 * {@code Select p from
 * Pais p where p.nombre :nombre}
 * </pre>
 * 
 * Se setea como {@link FilterAttribute#name()} 'nombre' y en el
 * {@link FilterAttribute#path()} la expresión del lenguaje natural que
 * represente el valor a insertar en el parámetro
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterAttribute {

	/**
	 * Nombre del atributo del filter
	 * 
	 * @see Filter
	 */
	String name();

	/**
	 * Path en Expression Language que será evaluada, el resultado de esta
	 * expresión será insertado como parámetro, notar que para pasar valores de
	 * un parámetro se utiliza la nomenclatura {0} para el primer parámetro.
	 * 
	 * @return
	 */
	String path();
}
