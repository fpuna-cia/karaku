/*
 * @KarakuRuntimeException.java 1.0 May 23, 2013
 * Sistema Integral de Gestion Hospitalaria
 * 
 */
package py.una.pol.karaku.exception;

/**
 * Clase base para todas las excepciones que no deben ser controladas
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 23, 2013
 * 
 */
public class KarakuRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	public KarakuRuntimeException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KarakuRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public KarakuRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public KarakuRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
