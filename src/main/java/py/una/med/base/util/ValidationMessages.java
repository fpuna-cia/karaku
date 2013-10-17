/*
 * @ValidationMessages.java 1.0 Oct 17, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.util;

/**
 * Utilidad para acceder a los diferentes mensajes de validación.
 *
 * <p>
 * Se listan todos los mensajes disponibles para agregar en una entidad.
 * </p>
 *
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Oct 17, 2013
 *
 */
public final class ValidationMessages {

	private ValidationMessages() {

	}

	/**
	 * Mensaje para longitud máxima.
	 */
	public static final String LENGTH_MAX = "{LENGTH_MAX}";

	/**
	 * Mensaje para longitud.
	 */
	public static final String LENGTH = "{LENGTH}";

	/**
	 * Mensaje para definir que solo se admiten cadenas.
	 *
	 * @see ValidationConstants#GN_WORDS_SPE
	 */
	public static final String ONLY_STRING = "{ONLY_STRING}";
}
