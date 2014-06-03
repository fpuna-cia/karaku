/*
 * @URLNotFoundException.java 1.0 Jun 11, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.pol.karaku.services.client;

import py.una.pol.karaku.exception.KarakuRuntimeException;

/**
 * Excepción lanzada cuando no se puede recuperar información (en este caso URL)
 * de un servicio.
 * 
 * @author Arturo Volpe
 * @since 2.2
 * @version 1.0 Aug 8, 2013
 * 
 */
public class URLNotFoundException extends KarakuRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3342578024752694309L;

	public URLNotFoundException(String key) {

		this(key, null);
	}

	public URLNotFoundException(String key, Throwable exception) {

		super("URL not found for key " + key, exception);
	}
}
