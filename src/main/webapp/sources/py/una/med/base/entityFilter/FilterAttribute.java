package py.una.med.base.entityFilter;

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
 * Sse setea como {@link FilterAttribute#name()} 'nombre' y en el
 * {@link FilterAttribute#path()} la expresion del lenguaje natural que
 * represente el valor a insertar en el parametro
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
	 * Path en Expression Language que sera evaluada, el resultado de esta
	 * expresion sera insertado como parametro, notar que para pasar valores de
	 * un parametro se utiliza la nomenclatura {0} para el primer parametro.
	 * 
	 * @return
	 */
	String path();
}
