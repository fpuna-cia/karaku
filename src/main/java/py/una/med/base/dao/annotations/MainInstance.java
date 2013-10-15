/**
 * @Principal.java 1.0 Feb 13, 2013 Sistema Integral de Gestion Hospitalaria
 *
 */
package py.una.med.base.dao.annotations;

import static javax.persistence.FetchType.EAGER;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.persistence.FetchType;

/**
 * Anotacio que permite definir un atributo de una lista como el atributo
 * principal. Por ejemplo, una {@link Persona} tiene una lista de
 * {@link PersonaDocumento}, pero solo uno de ellos es el atributo principal, el
 * cual se utiliza para visaualizar, esta anotacion modifica la carga de
 * elementos para que la instancia principal sea cargada.
 *
 * <br>
 * Por ejemplo: en la entidad <b>Persona</b>
 *
 * <pre>
 * &#064;Entity
 * public class Persona {
 * 	...
 * 	&#064;OneToMany(mappedBy="persona")
 * 	private List<Direccion> direcciones;
 *
 * 	&#064;Transient
 * 	&#064;MainInstance(attribute = "direcciones", path = "principal", value = "SI")
 * 	private Direccion direccion;
 * ...
 * }
 * &#064;Entity
 * public class Direccion {
 * 	...
 * 	&#064;ManyToOne
 * 	Persona persona;
 *
 * 	String principal;
 * 	...
 * }
 *
 * </pre>
 *
 * Obs.: Esta anotación es de solo lectura <br>
 * TODO : convertir esta anotación a escritura, para esto se debe
 * <ol>
 * <li>Crear siempre proxies</li>
 * <li>Capturar los setters y getters para refrescar la situación</li>
 * <li>Persistir automáticamente</li>
 * </ol>
 *
 * - Hacer que esta anotación soporte métodos
 *
 * @author Arturo Volpe Torres
 * @since 1.0
 * @version 1.0 Feb 13, 2013
 * @see py.una.med.base.dao.util.MainInstanceHelper
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MainInstance {

	/**
	 * Atributo de la entidad principal donde se encuentra la lista de
	 * elementos.
	 *
	 * @return Nombre del campo
	 */
	String attribute();

	/**
	 * {@link FetchType#EAGER} provoca la modificacion de la consulta original
	 * para traer el elemento, con {@link FetchType#LAZY}, se crean proxies para
	 * realizar la consulta bajo de manda.
	 *
	 * @return {@link FetchType} deseado
	 */
	FetchType fetch() default EAGER;

	/**
	 * Nombre del atributo de la entidad secundaria donde se almacena la
	 * informacion de cual entidad es el padre, solo soporta Strings, ejemplo
	 * {@link PersonaDocumento#getPrincipal()}
	 *
	 * @return nombre del field
	 */
	String path() default "principal";

	/**
	 * Valor de la variable {@link MainInstance#path()} para aquellos valores
	 * considerados como atributo principal
	 *
	 * @return cadena de caracteres unico.
	 * @see PersonaDocumento#PRINCIPAL
	 */
	String value() default "SI";
}
