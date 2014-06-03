/*
 * @KarakuPropertyNotFoundException.java 1.0 May 23, 2013
 * Sistema Integral de Gestion Hospitalaria
 * 
 */
package py.una.pol.karaku.exception;

/**
 * 
 * @author Arturo Volpe
 * @since 1.0
 * @version 1.0 May 23, 2013
 * 
 */
public class KarakuPropertyNotFoundException extends KarakuRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 387292724457782567L;
	private static final String MESSAGE = "Can't read property (%s) check the properties file";

	public KarakuPropertyNotFoundException(String property) {
		super(String.format(MESSAGE, property));
	}
}
