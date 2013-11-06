/*
 * @SQLFiles.java 1.0 Sep 11, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.test.util.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import py.una.med.base.exception.KarakuRuntimeException;

/**
 *
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Sep 11, 2013
 * @see DatabasePopulatorExecutionListener
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLFiles {

	public static final String DEFAULT = "default";

	/**
	 * En el caso de que no se quiere ejecutar ningún archivo, establecer esta
	 * cadena, como valor del atributo {@link #value()}.
	 */
	public static final String NONE = "none";

	/**
	 * Ubicación de los archivos sql para cargar.
	 * <p>
	 * Si el archivo no puede ser cargardo se busca en la ubicación del test. <br />
	 * La extensión <code>.sql</code> puede ser omitida. <br />
	 * Los valores inválidos (y repetidos) lanzaran una excepción del tipo
	 * {@link KarakuRuntimeException}
	 *
	 * </p>
	 *
	 * <p>
	 * <b>En caso de que esta anotación esté en el scope de una clase</b>, una
	 * cadena con que sea igual a {@link #DEFAULT} o a <code>""</code>, será
	 * interpretada como la ubicación por defecto. La ubicación por defecto es
	 * la misma donde se encuentra el test, con un archivo con el mismo nombre
	 * que el test, cambiando <code>java</code> por <code>sql</code>. <br>
	 * <b>Por ejemplo</b>
	 *
	 * <pre>
	 * 	py.una.med.sistema.test.casos.Test350.java
	 * Tendrá por archivo por defecto
	 * 	py/una/med/sistema/test/casos/Test350.sql
	 * </pre>
	 *
	 * </p>
	 *
	 * @return {@link String[]} con la ubicación de los archivos
	 */
	String[] value() default { DEFAULT };
}
