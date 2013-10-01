package py.una.med.base.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Anotación que define la internacionalizacion para un field, con el
 * {@link DisplayName#path()} se asigna la ubicación de donde se sacara el valor
 * para ser mostrado.
 * <p>
 * Esta anotación también sirve para dar información detallada acerca del campo,
 * especialmente útil en los casos donde se crean filtros automáticos.
 * </p>
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
	 * Clave por la cual se buscara en el archivo de idiomas en el formato<br/>
	 * 
	 * <pre>
	 * &quot;{CADENA}&quot;
	 * </pre>
	 * 
	 * Ejemplo
	 * 
	 * <pre>
	 * {@literal @}DisplayName(key="NACIONALIDAD_DESCRIPCION")
	 * Nacionalidad nacionalidad;
	 * </pre>
	 * 
	 * @return Llave del archivo de idiomas
	 */
	String key() default "";

	/**
	 * Expresión para ubicar el valor de la referencia.<br/>
	 * Ejemplo: desde la entidad PersonaFisica, si queremos agregar un enlace a
	 * la descripción de la nacionalidad ponemos en el atributo
	 * <code>nacionalidad</code> la anotación de la siguiente forma:
	 * 
	 * <pre>
	 * {@literal @}DisplayName(path="descripcion")
	 * Nacionalidad nacionalidad;
	 * </pre>
	 * 
	 * @return EL expression
	 */
	String path() default "";

	/**
	 * <b>Solo útil cuando se trabaja con clases que implementan
	 * {@link Collection}, </b> esté método define el tipo de una colección, ya
	 * que el mismo no puede ser recuperado mediante reflection.
	 * <p>
	 * 
	 * <pre>
	 * {@literal @}DisplayName(path="descripcion", class=Nacionalidad.class)
	 * List<Nacionalidad> nacionalidades;
	 * </pre>
	 * 
	 * En el ejemplo anterior, se buscara en el atributo
	 * <code>descripción</code> de la clase <code>Nacionalidad</code>.
	 * 
	 * <p>
	 * El valor por defecto es {@link Void} pues, una lisa carece de sentido si
	 * es del tipo {@link Void}
	 * </p>
	 * 
	 * @return {@link Class} parametrizada en la {@link Collection}
	 */
	Class<?> clazz() default DEFAULT.class;

	/**
	 * Clase por defecto del parámetro {@link DisplayName#clazz()}
	 * 
	 * @author Arturo Volpe
	 * @since 1.0
	 * @version 1.0 Sep 25, 2013
	 * 
	 */
	public static final class DEFAULT {}
}
