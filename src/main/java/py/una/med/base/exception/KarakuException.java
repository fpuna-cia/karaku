/*
 * @KarakuException.java 1.0 May 23, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.exception;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 23, 2013
 * 
 */
public class KarakuException extends Exception {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public KarakuException() {

		super();
	}

	/**
	 * @param cause
	 */
	public KarakuException(Throwable cause) {

		super(cause);
	}

	/**
	 * @param string
	 */
	public KarakuException(String string) {

		super(string);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KarakuException(String message, Throwable cause) {

		super(message, cause);
	}

}
