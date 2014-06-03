/*
 * @Checker.java 1.0 Nov 21, 2013 Sistema Integral de Gestion Hospitalaria
 */
package py.una.med.base.util;

import java.util.List;
import javax.annotation.Nonnull;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 21, 2013
 * 
 */
public final class Checker {

	private Checker() {

	}

	/**
	 * Chequea si un parámetro es nulo.
	 * 
	 * <p>
	 * Si el parámetro es nulo, entonces lanza una excepción del tipo
	 * {@link IllegalArgumentException}.
	 * </p>
	 * 
	 * @param object
	 *            objeto a verificar
	 */
	@Nonnull
	public static <T> T notNull(T object) {

		return notNull(object, "Object not null is not allowed");
	}

	@Nonnull
	public static <T> T notNull(T object, String message, Object ... arguments) {

		if (object == null) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return object;
	}

	/**
	 * Chequea que una lista no posea elementos.
	 * 
	 * @param list
	 *            lista a examinar
	 * @param message
	 *            mensaje, con <code>%s</code> como <i>wildcard</i> para los
	 *            argumentos.
	 * @param arguments
	 *            argumentos para formatear la cadena
	 * @return la misma lista
	 */
	public static <T> List<T> isEmpty(List<T> list, String message,
			Object ... arguments) {

		if (ListHelper.hasElements(list)) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return list;
	}

	/**
	 * Chequea si una lista posee elementos.
	 * 
	 * @param list
	 *            lista a verificar
	 * @param message
	 *            mensaje, con <code>%s</code> como <i>wildcard</i> para los
	 *            argumentos.
	 * @param arguments
	 *            argumentos para formatear la cadena
	 * @throws IllegalArgumentException
	 *             si la cadena no es válida
	 * @return la lista pasada
	 */
	public static <T> List<T> isNotEmpty(List<T> list, String message,
			Object ... arguments) {

		if (!ListHelper.hasElements(list)) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return list;
	}

	/**
	 * @deprecated use {@link #isValid(CharSequence, String, Object...)}
	 * @param string
	 * @param message
	 * @param arguments
	 * @return
	 */
	@Nonnull
	@Deprecated
	public static <T extends CharSequence> T notValid(T string, String message,
			Object ... arguments) {

		if (string == null || StringUtils.isInvalid(string.toString())) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return string;
	}

	/**
	 * Verifica que una cadena sea válida.
	 * <p>
	 * Dentro de este contexto, válida significa que no sea nula y que tenga al
	 * menos un carácter distinto a un espacio vacío.
	 * </p>
	 * 
	 * @param string
	 *            cadena a verificar
	 * @param message
	 *            mensaje, con <code>%s</code> como <i>wildcard</i> para los
	 *            argumentos.
	 * @param arguments
	 *            argumentos para formatear la cadena
	 * @return la cadena validada.
	 * @throws IllegalArgumentException
	 *             si la cadena no es válida
	 */
	@Nonnull
	public static <T extends CharSequence> T isValid(T string, String message,
			Object ... arguments) {

		if (string == null || StringUtils.isInvalid(string.toString())) {
			throw new IllegalArgumentException(format(message, arguments));
		}
		return string;
	}

	public static String format(String message, Object ... arguments) {

		// TODO cambiar por un mecanismo mas eficiente, ver guava Preconditions.
		return String.format(message, arguments);
	}
}
