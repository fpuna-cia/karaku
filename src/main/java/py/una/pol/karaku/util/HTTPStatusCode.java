package py.una.pol.karaku.util;

/**
 * Lista de codigos de respuesta del HTTP. Estos codigos de estatus estan
 * especificados en el RFC 2616.
 *
 * @author Uriel Gonzalez
 *
 */
public final class HTTPStatusCode {

	private HTTPStatusCode() {

	}

	/**
	 * El servidor no ha encontrado nada que coincida con el Request-URI
	 */
	public static final String NOT_FOUND = "404";

	/**
	 * Respuesta estandar para peticiones correctas.
	 */
	public static final String OK = "200";

	public static final String BAD_REQUEST = "400";

}
