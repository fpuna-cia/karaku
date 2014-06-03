package py.una.pol.karaku.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

}
